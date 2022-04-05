package com.example.application.service.fhir;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.DeleteCascadeModeEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.gclient.ICriterion;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Resource;

import java.util.List;
import java.util.Map;

public interface FhirResourceAsyncService<T extends Resource> {

    void create(T resource, FhirUpdateListener listener);

    void update(T resource, FhirUpdateListener listener);

    void delete(T resource, DeleteCascadeModeEnum deleteCascadeModeEnum, FhirUpdateListener listener);

    void transaction(Bundle bundle, FhirTransactionListener listener);

    void load(List<ICriterion> criterionList, FhirSearchListener<T> searchListener);

    void load(
            List<ICriterion> criterionList,
            List<Include> includeList,
            FhirSearchIncludeListener searchIncludeListener);

    void read(String resourceId, FhirSearchIdListener<T> searchIdListener);

    interface FhirUpdateListener {
        void onRequestExecuted(MethodOutcome methodOutcome);
        void onError(Exception exception);
    }

    interface FhirTransactionListener {
        void onRequestExecuted(Bundle bundle);
        void onError(Exception exception);
    }

    interface FhirSearchIdListener<T extends Resource> {
        void onRequestExecuted(T resource);
        void onError(Exception exception);
    }

    interface FhirSearchListener<T extends Resource> {
        void onRequestExecuted(List<T> resourceList);
        void onError(Exception exception);
    }

    interface FhirSearchIncludeListener {
        void onRequestExecuted(Map<String, Resource> resourceMap);
        void onError(Exception exception);
    }
}