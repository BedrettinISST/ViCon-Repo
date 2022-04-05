package com.example.application.service.fhir;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.DeleteCascadeModeEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.rest.gclient.IQuery;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Resource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractFhirResourceAsyncService<T extends Resource> implements FhirResourceAsyncService<T> {

    private final FhirService fhirService;
    private final IGenericClient fhirClient;
    private final Class<T> clazz;

    public AbstractFhirResourceAsyncService(
            FhirService fhirService,
            Class<T> clazz) {
        this.fhirService = fhirService;
        this.fhirClient = fhirService.getFhirClient();
        this.clazz = clazz;
    }

    @Override
    public void create(T resource, FhirUpdateListener listener) {
        fhirService.executeMethodOutcomeAsync(
                () -> fhirClient.create().resource(resource).execute(),
                listener::onRequestExecuted,
                listener::onError);
    }

    @Override
    public void update(T resource, FhirUpdateListener listener) {
        fhirService.executeMethodOutcomeAsync(
                () -> fhirClient.update().resource(resource).execute(),
                listener::onRequestExecuted,
                listener::onError);
    }

    @Override
    public void delete(T resource, DeleteCascadeModeEnum deleteCascadeModeEnum, FhirUpdateListener listener) {
        fhirService.executeMethodOutcomeAsync(
                () -> fhirClient.delete().resource(resource).cascade(deleteCascadeModeEnum).execute(),
                listener::onRequestExecuted,
                listener::onError);
    }

    @Override
    public void transaction(Bundle bundle, FhirTransactionListener listener) {
        fhirService.executeTransactionAsync(
                () -> fhirClient.transaction().withBundle(bundle).execute(),
                listener::onRequestExecuted,
                listener::onError);
    }

    @Override
    public void load(List<ICriterion> criterionList, FhirSearchListener<T> searchListener) {
        fhirService.executeBundleAsync(
                () -> {
                    IQuery<IBaseBundle> query = fhirClient.search()
                            .forResource(clazz)
                            .cacheControl(CacheControlDirective.noCache());
                    criterionList.forEach(query::where);
                    return query.returnBundle(Bundle.class).execute();
                },
                bundle -> searchListener.onRequestExecuted(getResourceListForBundle(bundle)),
                searchListener::onError);
    }

    @Override
    public void load(
            List<ICriterion> criterionList,
            List<Include> includeList,
            FhirSearchIncludeListener searchIncludeListener) {
        fhirService.executeBundleAsync(
                () -> {
                    IQuery<IBaseBundle> query = fhirClient.search()
                            .forResource(clazz)
                            .cacheControl(CacheControlDirective.noCache());
                    criterionList.forEach(query::where);
                    includeList.forEach(query::include);
                    return query.returnBundle(Bundle.class).execute();
                },
                bundle -> searchIncludeListener.onRequestExecuted(getResourceMapForBundle(bundle)),
                searchIncludeListener::onError);
    }

    private Map<String, Resource> getResourceMapForBundle(Bundle bundle) {
        return bundle.getEntry().stream()
                .collect(Collectors.toMap(
                        bundleEntryComponent -> bundleEntryComponent.getResource().getId(),
                        Bundle.BundleEntryComponent::getResource));
    }

    private List<T> getResourceListForBundle(Bundle bundle) {
        return bundle.getEntry().stream()
                .map(entry -> (T) entry.getResource())
                .collect(Collectors.toList());
    }

    @Override
    public void read(String resourceId, FhirSearchIdListener<T> searchIdListener) {
        fhirService.executeReadAsync(
                () -> fhirClient.read().resource(clazz).withId(resourceId).execute(),
                searchIdListener::onRequestExecuted,
                searchIdListener::onError);
    }
}