package com.example.application.service.keycloak;

import ca.uhn.fhir.rest.api.MethodOutcome;
import com.example.application.service.fhir.AbstractFhirResourceAsyncService;
import com.example.application.service.fhir.FhirResourceAsyncService;
import com.example.application.service.keycloak.AuthenticationService;
import com.vaadin.flow.component.UI;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.IDToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {


    private static final Logger LOGGER = Logger.getLogger(AuthenticationServiceImpl.class.getName());

    private AbstractFhirResourceAsyncService<Patient> patientFhirService;


    @Override
    public boolean isFirstLogin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof KeycloakPrincipal keycloakPrincipal) {
            AccessToken token = keycloakPrincipal.getKeycloakSecurityContext().getToken();
            return token.getOtherClaims().get(CLAIM_FIRST_LOGIN) == null;
        } else {
            UI.getCurrent().getPage().open("http://localhost:8111/sso/login", "_self");
            throw new RuntimeException("No authentication context available");
        }
    }

    @Autowired
    public AuthenticationServiceImpl(AbstractFhirResourceAsyncService<Patient> patientFhirService) {
        this.patientFhirService = patientFhirService;
    }

    @Override
    public void updateFirstLoginClaim() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof KeycloakPrincipal keycloakPrincipal) {
            AccessToken token = keycloakPrincipal.getKeycloakSecurityContext().getToken();
            token.getOtherClaims().put(CLAIM_FIRST_LOGIN, "true");
        } else {
            UI.getCurrent().getPage().open("http://localhost:8111/sso/login", "_self");
            throw new RuntimeException("No authentication context available");
        }
    }
    @Override
    public boolean isUserLoggedIn() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal instanceof KeycloakPrincipal;
    }
    @Override
    public String getOrganization() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof KeycloakPrincipal keycloakPrincipal) {
            AccessToken token = keycloakPrincipal.getKeycloakSecurityContext().getToken();
            return (String) token.getOtherClaims().get(CLAIM_ORGANIZATION);
        } else {
            throw new RuntimeException("No authentication context available");
        }
    }

    @Override
    public String getUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof KeycloakPrincipal keycloakPrincipal) {
            return keycloakPrincipal.getKeycloakSecurityContext().getIdToken().getPreferredUsername();
        } else {
            throw new RuntimeException("No authentication context available");
        }
    }
    @Override
    public void getPatient(PatientListener listener) {
        patientFhirService.load(
                Collections.singletonList(Patient.IDENTIFIER.exactly().code(getUserId())),
                new FhirResourceAsyncService.FhirSearchListener<>() {
                    @Override
                    public void onRequestExecuted(List<Patient> resourceList) {
                        LOGGER.log(Level.INFO, resourceList.toString());
                        if (resourceList.isEmpty()) {
                            createPatient(listener);
                        } else {
                            listener.onPatientFetched(resourceList.get(0));
                        }
                    }

                    @Override
                    public void onError(Exception exception) {
                        LOGGER.log(Level.INFO, exception.getMessage());
                        listener.onError(exception);
                    }
                }
        );
    }

    private void createPatient(PatientListener listener) {
        Patient patient = new Patient();
        patient.setName(Collections.singletonList(new HumanName().setText(getUsername())));
        patient.setIdentifier(Collections.singletonList(new Identifier().setValue(getUserId())));
        patientFhirService.create(patient, new FhirResourceAsyncService.FhirUpdateListener() {
            @Override
            public void onRequestExecuted(MethodOutcome methodOutcome) {
                Patient patient = (Patient) methodOutcome.getResource();
                listener.onPatientFetched(patient);
            }

            @Override
            public void onError(Exception exception) {
                listener.onError(exception);
            }
        });
    }

    @Override
    public String getUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof KeycloakPrincipal keycloakPrincipal) {
            IDToken idToken = keycloakPrincipal.getKeycloakSecurityContext().getIdToken();
            return idToken.getSubject();
        } else {
            throw new RuntimeException("No authentication context available");
        }
    }

}
