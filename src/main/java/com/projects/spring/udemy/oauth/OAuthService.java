package com.projects.spring.udemy.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.projects.spring.udemy.author.Author;
import com.projects.spring.udemy.author.AuthorRepository;
import com.projects.spring.udemy.file.ImageClass;
import com.projects.spring.udemy.oauth.dto.AuthorForm;
import com.projects.spring.udemy.oauth.dto.LoginResponse;
import com.projects.spring.udemy.oauth.dto.UserForm;
import com.projects.spring.udemy.user.User;
import com.projects.spring.udemy.user.UserRepository;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;
import java.util.*;

@Service
public class OAuthService {
    // FIXME: inject by construtor
    @Autowired
    private Keycloak keycloakClient;
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;
    @Value("${keycloak.realm}")
    private String realmName;
    @Value("${keycloak.resource}")
    private String resource;
    @Value("${keycloak.credentials.secret}")
    private String secret;

    private static final Logger logger = LoggerFactory.getLogger(OAuthService.class);

    public OAuthService(UserRepository userRepository, AuthorRepository authorRepository) {
        this.userRepository = userRepository;
        this.authorRepository = authorRepository;
    }

    List<UserRepresentation> getAllUsers() {
        return keycloakClient.realm(realmName).users().list();
    }

    LoginResponse login(UserForm source) {
        String name = source.getName();
        Optional<?> person = source instanceof AuthorForm ?
                authorRepository.findByName(name) : userRepository.findByName(name);
        if(!person.isPresent())
            throw new BadRequestException("Invalid nick or password :(");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // default media type for HTML forms

        String urlName = keycloakUrl + "/realms/" + realmName + "/protocol/openid-connect/token";
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("grant_type", "password");
        request.add("username", source.getName());
        request.add("password", source.getPassword());
        request.add("client_id", resource);
        request.add("client_secret", secret);

        HttpEntity httpEntity = new HttpEntity(request, headers);

        try{
            ResponseEntity<String> response = restTemplate.postForEntity(urlName, httpEntity, String.class);
            String body = response.getBody();
            ObjectMapper mapper = new ObjectMapper();
            mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            TokenData tokenData = mapper.readValue(body, TokenData.class);
            return new LoginResponse(tokenData, (ImageClass) person.get());
        } catch (Exception e) {
            throw new InternalServerErrorException("Internal server error when logging in");
        }
    }

    LoginResponse register(UserForm source, HttpServletRequest request) {
        // user's/author's name should be unique
        String name = source.getName();
        boolean isAuthor = source instanceof AuthorForm;
        boolean nameExists = isAuthor ?
                authorRepository.existsByName(name) : userRepository.existsByName(name);
        if(nameExists)
            throw new NickAlreadyExistsException("User/Author with such a nick already exists");

        CredentialRepresentation cR = preparePasswordRepresentation(source.getPassword());
        UserRepresentation uR = prepareUserRepresentation(source.getName(), cR);

        //FIXME ( see commit 2c5c212ca8a7e6f0e954312cb2ec00922ac36af8 )
        // add role in keycloak to new user/author
//        String roleName = isAuthor ? "author" : "user";
//        String userId = null;
//        assignRole(prepareRoleRepresentation(roleName), userId);
        // save user to keycloak
        Response response = keycloakClient.realm(realmName).users().create(uR);


        // save user/author to DB in order to make relations between users table and others
        // TODO: add secure password entry to DB
        if(isAuthor) {
            // before saving cast source to AuthorForm to get access to e.g. getDescription()
            AuthorForm authorSource = (AuthorForm) source;
            authorRepository.save(new Author(name, authorSource.getDescription(), authorSource.getOccupation()));
        }
        else
            userRepository.save(new User(name));

        return login(source);
    }

    private RoleRepresentation prepareRoleRepresentation(String name) {
        return keycloakClient.realm(realmName).roles().get(name).toRepresentation();
    }

    private void assignRole(RoleRepresentation roleRepresentation, String id) {
        keycloakClient.realm(realmName).users().get(id).roles().realmLevel().add(Arrays.asList(roleRepresentation));
    }

    private CredentialRepresentation preparePasswordRepresentation(String password) {
        CredentialRepresentation cR = new CredentialRepresentation();
        cR.setTemporary(false);
        cR.setType(CredentialRepresentation.PASSWORD);
        cR.setValue(password);
        return cR;
    }

    private UserRepresentation prepareUserRepresentation(String name, CredentialRepresentation cR) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(name);
        user.setEnabled(true);
        user.setCredentials(List.of(cR));

        Map<String, List<String>> attributes = new HashMap<>();
        // in future maybe usable?

        user.setAttributes(attributes);

        return user;
    }
}
