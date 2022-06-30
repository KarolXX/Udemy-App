package com.projects.spring.udemy.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.projects.spring.udemy.AppUserTemplate;
import com.projects.spring.udemy.author.Author;
import com.projects.spring.udemy.author.AuthorRepository;
import com.projects.spring.udemy.file.ImageClass;
import com.projects.spring.udemy.oauth.dto.AuthorForm;
import com.projects.spring.udemy.oauth.dto.LoginResponse;
import com.projects.spring.udemy.oauth.dto.UserForm;
import com.projects.spring.udemy.user.User;
import com.projects.spring.udemy.user.UserRepository;
import net.bytebuddy.utility.RandomString;
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
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public OAuthService(UserRepository userRepository, AuthorRepository authorRepository) {
        this.userRepository = userRepository;
        this.authorRepository = authorRepository;
    }

    List<UserRepresentation> getAllUsers() {
        return keycloakClient.realm(realmName).users().list();
    }

    LoginResponse login(UserForm source) {
        String name = source.getName();
        String urlName = keycloakUrl + "/realms/" + realmName + "/protocol/openid-connect/token";

        // check if source is user or author and then find it
        Optional<?> person = source instanceof AuthorForm ?
                authorRepository.findByName(name) : userRepository.findByName(name);
        // if input is invalid
        if(person.isEmpty())
            throw new BadRequestException("Invalid nick or password :(");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // default media type for HTML forms

        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("grant_type", "password");
        request.add("username", source.getName());
        request.add("password", source.getPassword());
        request.add("client_id", resource);
        request.add("client_secret", secret);

        HttpEntity httpEntity = new HttpEntity(request, headers);

        // send request to keycloak by restTemplate
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

    LoginResponse register(UserForm source) {
        // user's/author's name should be unique
        String name = source.getName();
        boolean isAuthor = source instanceof AuthorForm;
        boolean nameExists = isAuthor ?
                authorRepository.existsByName(name) : userRepository.existsByName(name);
        if(nameExists)
            throw new NickAlreadyExistsException("User/Author with such a nick already exists");

        // prepare user/author to be saved in keycloak
        CredentialRepresentation cR = preparePasswordRepresentation(source.getPassword());
        UserRepresentation uR = prepareUserRepresentation(source.getName(), cR);

        // save user/author to keycloak
        Response response = keycloakClient.realm(realmName).users().create(uR);

        // add role describing who is registered: user or author to keycloak
        String roleName = isAuthor ? "author" : "user";
        String keycloakId = KeycloakHelper.getCreatedId(response);
        assignRole(prepareRoleRepresentation(roleName), keycloakId);

        // save user/author to DB in order to make relations between users table and others
        PersonType personType = isAuthor ? PersonType.AUTHOR : PersonType.USER;
        String salt = getRandomSalt(personType);
        String hashedPassword = hash(source.getPassword(), roleName);

        if(isAuthor) {
            // before saving cast source to AuthorForm to get access to e.g. getDescription()
            AuthorForm authorSource = (AuthorForm) source;
            Author author = new Author(name, hashedPassword, salt, authorSource.getDescription(), authorSource.getOccupation());
            authorRepository.save(author);
        }
        else
            userRepository.save(new User(name, hashedPassword, salt));

        return login(source);
    }

    // functions for keycloak
    private RoleRepresentation prepareRoleRepresentation(String roleName) {
        return keycloakClient.realm(realmName).roles().get(roleName).toRepresentation();
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

    private static class KeycloakHelper {
        public static String getCreatedId(Response response) {
            URI location = response.getLocation();

            if (!response.getStatusInfo().equals(Response.Status.CREATED)) {
                return null;
            }

            if (location == null) {
                return null;
            }

            String path = location.getPath();
            return path.substring(path.lastIndexOf('/') + 1);
        }
    }

    // functions for MySQL hashing
    private String hash(String password, String salt) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            String passWithSalt = password + salt;
            byte[] passBytes = passWithSalt.getBytes();
            byte[] passHash = sha256.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < passHash.length; i++) {
                sb.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));
            }
            String generatedPassword = sb.toString();
            return generatedPassword;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private enum PersonType {
        USER,
        AUTHOR
    }

    private String getRandomSalt(PersonType personType) {
        RandomString salt = new RandomString(32);
        String result = null;

        while (result == null || personType.equals(PersonType.AUTHOR) ? userRepository.existsBySalt(result) : authorRepository.existsBySalt(result)) {
            result = salt.nextString();
        }

        return result;
    }
}

