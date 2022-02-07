package com.projects.spring.udemy.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.projects.spring.udemy.oauth.dto.LoginResponse;
import com.projects.spring.udemy.oauth.dto.UserForm;
import com.projects.spring.udemy.user.User;
import com.projects.spring.udemy.user.UserRepository;
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
    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;
    @Value("${keycloak.realm}")
    private String realmName;
    @Value("${keycloak.resource}")
    private String resource;
    @Value("${keycloak.credentials.secret}")
    private String secret;

    private static final Logger logger = LoggerFactory.getLogger(OAuthService.class);

    public OAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    List<UserRepresentation> getAllUsers() {
        return keycloakClient.realm(realmName).users().list();
    }

    LoginResponse login(UserForm userForm) {
        Optional<User> user = userRepository.findByName(userForm.getName());
        if(!user.isPresent())
            throw new BadRequestException("Invalid nick or password :(");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // default media type for HTML forms

        String urlName = keycloakUrl + "/realms/" + realmName + "/protocol/openid-connect/token";
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("grant_type", "password");
        request.add("username", userForm.getName());
        request.add("password", userForm.getPassword());
        request.add("client_id", resource);
        request.add("client_secret", secret);

        HttpEntity httpEntity = new HttpEntity(request, headers);

        try{
            ResponseEntity<String> response = restTemplate.postForEntity(urlName, httpEntity, String.class);
            String body = response.getBody();
            ObjectMapper mapper = new ObjectMapper();
            mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            TokenData tokenData = mapper.readValue(body, TokenData.class);
            return new LoginResponse(tokenData, user.get());
        } catch (Exception e) {
            throw new InternalServerErrorException("Internal server error when logging in");
        }
    }

    // FIXME: change this method so that after registration the token data is returned
     LoginResponse register(UserForm source) {
        // username should be unique
        Boolean usernameExists = userRepository.existsByName(source.getName());
        if(usernameExists)
            throw new NickAlreadyExistsException("User with such a nick already exists");

        // save user to DB in order to make relations between users table and others
        // TODO: add secure password entry to DB
        userRepository.save(new User(source.getName()));

        CredentialRepresentation cR = preparePasswordRepresentation(source.getPassword());
        UserRepresentation uR = prepareUserRepresentation(source.getName(), cR);

        // save user to keycloak
        Response response = keycloakClient.realm(realmName).users().create(uR);

        //FIXME ( see commit 2c5c212ca8a7e6f0e954312cb2ec00922ac36af8 )
        // add 'user' role in keycloak to new user
//        assignRole(prepareRoleRepresentation("user"), uR);

        return login(source);
    }

    private RoleRepresentation prepareRoleRepresentation(String name) {
        return keycloakClient.realm(realmName).roles().get(name).toRepresentation();
    }

    private void assignRole(RoleRepresentation roleRepresentation, UserRepresentation userRepresentation) {
        String userId = userRepresentation.getId();
        keycloakClient.realm(realmName).users().get(userId).roles().realmLevel().add(Arrays.asList(roleRepresentation));
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
