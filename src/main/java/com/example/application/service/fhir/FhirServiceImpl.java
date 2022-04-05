package com.example.application.service.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.example.application.Util.Constants;
import com.vaadin.flow.component.UI;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Resource;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class FhirServiceImpl implements FhirService {

    private final IGenericClient fhirClient;

    public FhirServiceImpl() {
        FhirContext ctx = FhirContext.forR4();
        fhirClient = ctx.newRestfulGenericClient(Constants.FHIR_SERVER_ADDRESS);
    }

    @Override
    public IGenericClient getFhirClient() {
        return fhirClient;
    }

    @Override
    public void executeMethodOutcomeAsync(
            Supplier<MethodOutcome> fhirOperation,
            Consumer<MethodOutcome> uiCallbackOperation,
            Consumer<Exception> onErrorOperation) {
        UI currentUi = UI.getCurrent();
        if (currentUi != null) {
            Runnable runnable = () -> {
                try {
                    MethodOutcome methodOutcome = fhirOperation.get();
                    currentUi.access(() -> {
                        uiCallbackOperation.accept(methodOutcome);
                        currentUi.push();
                    });
                } catch (Exception e) {
                    currentUi.access(() -> {
                        onErrorOperation.accept(e);
                        currentUi.push();
                    });
                }
            };
            SecurityContext context = SecurityContextHolder.getContext();
            DelegatingSecurityContextRunnable wrappedRunnable = new DelegatingSecurityContextRunnable(
                    runnable, context);
            new Thread(wrappedRunnable).start();
        } else {
            throw new RuntimeException("Warning: FhirService has been called from a background thread");
        }
    }

    @Override
    public void executeBundleAsync(
            Supplier<Bundle> fhirOperation,
            Consumer<Bundle> uiCallbackOperation,
            Consumer<Exception> onErrorOperation) {
        UI currentUi = UI.getCurrent();
        if (currentUi != null) {
            Runnable runnable = () -> {
                try {
                    Bundle bundle = fhirOperation.get();
                    currentUi.access(() -> {
                        uiCallbackOperation.accept(bundle);
                        currentUi.push();
                    });
                } catch (Exception e) {
                    currentUi.access(() -> {
                        onErrorOperation.accept(e);
                        currentUi.push();
                    });
                }
            };
            SecurityContext context = SecurityContextHolder.getContext();
            DelegatingSecurityContextRunnable wrappedRunnable = new DelegatingSecurityContextRunnable(
                    runnable, context);
            new Thread(wrappedRunnable).start();
        } else {
            throw new RuntimeException("Warning: FhirService has been called from a background thread");
        }
    }

    @Override
    public <T extends Resource> void executeReadAsync(
            Supplier<T> fhirOperation,
            Consumer<T> uiCallbackOperation,
            Consumer<Exception> onErrorOperation) {
        UI currentUi = UI.getCurrent();
        if (currentUi != null) {
            Runnable runnable = () -> {
                try {
                    T resource = fhirOperation.get();
                    currentUi.access(() -> {
                        uiCallbackOperation.accept(resource);
                        currentUi.push();
                    });
                } catch (Exception e) {
                    currentUi.access(() -> {
                        onErrorOperation.accept(e);
                        currentUi.push();
                    });
                }
            };
            SecurityContext context = SecurityContextHolder.getContext();
            DelegatingSecurityContextRunnable wrappedRunnable = new DelegatingSecurityContextRunnable(
                    runnable, context);
            new Thread(wrappedRunnable).start();
        } else {
            throw new RuntimeException("Warning: FhirService has been called from a background thread");
        }
    }

    @Override
    public <T extends Resource> void executeTransactionAsync(
            Supplier<Bundle> fhirOperation,
            Consumer<Bundle> uiCallbackOperation,
            Consumer<Exception> onErrorOperation) {
        UI currentUi = UI.getCurrent();
        if (currentUi != null) {
            Runnable runnable = () -> {
                try {
                    Bundle bundle = fhirOperation.get();
                    currentUi.access(() -> {
                        uiCallbackOperation.accept(bundle);
                        currentUi.push();
                    });
                } catch (Exception e) {
                    currentUi.access(() -> {
                        onErrorOperation.accept(e);
                        currentUi.push();
                    });
                }
            };
            SecurityContext context = SecurityContextHolder.getContext();
            DelegatingSecurityContextRunnable wrappedRunnable = new DelegatingSecurityContextRunnable(
                    runnable, context);
            new Thread(wrappedRunnable).start();
        } else {
            throw new RuntimeException("Warning: FhirService has been called from a background thread");
        }
    }
}