package com.projects.spring.udemy.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.projects.spring.udemy.oauth.dto.LoginResponse;
import com.projects.spring.udemy.oauth.dto.RegisterForm;
import com.projects.spring.udemy.oauth.dto.LoginForm;
import org.keycloak.adapters.authentication.ClientCredentialsProvider;
import org.keycloak.adapters.authentication.ClientCredentialsProviderUtils;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OAuthService {
    @Autowired
    private Keycloak keycloakClient;
    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;
    @Value("${keycloak.realm}")
    private String realmName;
    @Value("${keycloak.resource}")
    private String resource;
    @Value("${keycloak.credentials.secret}")
    private String secret;

    private static final Logger logger = LoggerFactory.getLogger(OAuthService.class);

    List<UserRepresentation> getAllUsers() {
        return keycloakClient.realm(realmName).users().list();
    }

    LoginResponse login(LoginForm loginForm) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // default media type for HTML forms

        String urlName = keycloakUrl + "/realms/" + realmName + "/protocol/openid-connect/token";
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("grant_type", "password");
        request.add("username", loginForm.getName());
        request.add("password", loginForm.getPassword());
        request.add("client_id", resource);
        request.add("client_secret", secret);

        HttpEntity httpEntity = new HttpEntity(request, headers);

        try{
            ResponseEntity<String> response = restTemplate.postForEntity(urlName, httpEntity, String.class);
            String body = response.getBody();
            ObjectMapper mapper = new ObjectMapper();
            mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            TokenData tokenData = mapper.readValue(body, TokenData.class);
            return new LoginResponse(tokenData.getAccessToken());
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }
    }

    Boolean register(RegisterForm source) {
        CredentialRepresentation cR = preparePasswordRepresentation(source.getPassword());
        UserRepresentation uR = prepareUserRepresentation(source.getName(), cR);
        var result = keycloakClient.realm(realmName).users().create(uR);
        logger.info(result.readEntity(String.class));
        return result.getStatus() == 201;
    }

    private CredentialRepresentation preparePasswordRepresentation(String password) {
        CredentialRepresentation cR = new CredentialRepresentation();
        cR.setTemporary(false);
        cR.setType(CredentialRepresentation.PASSWORD);
        cR.setValue(password);

        return cR;
    }

    private UserRepresentation prepareUserRepresentation(String email, CredentialRepresentation cR) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(email);
        user.setEnabled(true);
        user.setCredentials(List.of(cR));

        Map<String, List<String>> attributes = new HashMap<>();
        // in future maybe usable?

        user.setAttributes(attributes);

        return user;
    }
}
