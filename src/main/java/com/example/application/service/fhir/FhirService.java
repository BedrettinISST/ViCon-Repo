package com.example.application.service.fhir;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Resource;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface FhirService {

    IGenericClient getFhirClient();

    /**
     * Ensures fhir operations are executed in the background and results are returned to the UI thread.
     * Furthermore transfers the SecurityContext to the background thread.
     * Never to be used from a background thread!
     * @param fhirOperation The fhir operation to be executed: create, update, delete
     * @param uiCallbackOperation Operation that is executed on the UI thread, after receiving the results
     * @param onErrorOperation Exceptions are returned to the UI thread
     */
    void executeMethodOutcomeAsync(
            Supplier<MethodOutcome> fhirOperation,
            Consumer<MethodOutcome> uiCallbackOperation,
            Consumer<Exception> onErrorOperation);

    /**
     * Same as executeMethodOutcomeAsync but for bundle return types
     */
    void executeBundleAsync(
            Supplier<Bundle> fhirOperation,
            Consumer<Bundle> uiCallbackOperation,
            Consumer<Exception> onErrorOperation);

    /**
     * Same as executeMethodOutcomeAsync but for single resource types
     */
    <T extends Resource> void executeReadAsync(
            Supplier<T> fhirOperation,
            Consumer<T> uiCallbackOperation,
            Consumer<Exception> onErrorOperation);

    /**
     * Same as executeMethodOutcomeAsync but for transactions
     */
    <T extends Resource> void executeTransactionAsync(
            Supplier<Bundle> fhirOperation,
            Consumer<Bundle> uiCallbackOperation,
            Consumer<Exception> onErrorOperation);

}