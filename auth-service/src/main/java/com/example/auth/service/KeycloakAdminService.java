package com.example.auth.service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class KeycloakAdminService {
    private final Keycloak keycloak;

    public boolean createUser(String username, String password) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEnabled(true);
        user.setRealmRoles(Collections.singletonList("USER"));

        Response resp = keycloak.realm("diploma-realm").users().create(user);
        if (resp.getStatus() != 201) return false;

        String id = getCreatedId(resp);
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(password);
        cred.setTemporary(false);

        keycloak.realm("diploma-realm").users().get(id).resetPassword(cred);

        return true;
    }

    private String getCreatedId(Response resp) {
        String location = resp.getHeaderString("Location");
        return location.substring(location.lastIndexOf("/") + 1);
    }
}
