package com.example.application.service.fhir;

import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl
        extends AbstractFhirResourceAsyncService<Patient>
        implements FhirResourceAsyncService<Patient> {

    @Autowired
    public PatientServiceImpl(FhirService fhirService) {
        super(fhirService, Patient.class);
    }

}