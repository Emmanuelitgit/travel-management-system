package user_service.external;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import user_service.exception.BadRequestException;
import user_service.models.User;

import javax.ws.rs.core.Response;
import java.util.Collections;

@Component
public class KeycloakService {

    private final KeycloakConfig keycloakConfig;
    private RealmResource realmInstance;

    @Autowired
    public KeycloakService(KeycloakConfig keycloakConfig) {
        this.keycloakConfig = keycloakConfig;
        this.realmInstance = keycloakConfig.getInstance().realm("travelManagement");
    }


    private CredentialRepresentation credentialRepresentation(String password){
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(true);
        credentialRepresentation.setValue(password);

        return credentialRepresentation;
    }

    public void saveUserToKeycloak(User user){
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(true);
        userRepresentation.setEmail(userRepresentation.getEmail());
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation(user.getPassword())));
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        Response response = realmInstance.users().create(userRepresentation);
        if (response.getStatus()!=200){
            throw new BadRequestException("exception occurred");
        }
    }
}
