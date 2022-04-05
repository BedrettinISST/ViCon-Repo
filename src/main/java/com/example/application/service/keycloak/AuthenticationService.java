package com.example.application.service.keycloak;

import org.hl7.fhir.r4.model.Patient;

public interface AuthenticationService
{
    String CLAIM_FIRST_LOGIN = "firstLogin";
    String CLAIM_ORGANIZATION = "organization";

    boolean isFirstLogin();
    boolean isUserLoggedIn();
    String KEYCLOAK_SERVER_URL_PROPERTY = "keycloak.auth-server-url";
    String KEYCLOAK_REALM_PROPERTY = "keycloak.realm";
    String KEYCLOAK_CLIENT_ID_PROPERTY = "keycloak.resource";
    String KEYCLOAK_CLIENT_SECRET_PROPERTY = "keycloak.credentials.secret";
    String getUsername();

    String getUserId();

    String getOrganization();
    void getPatient(PatientListener listener);
    interface PatientListener {
    void onPatientFetched(Patient patient);
    void onError(Exception e);
}
    void updateFirstLoginClaim();
}
