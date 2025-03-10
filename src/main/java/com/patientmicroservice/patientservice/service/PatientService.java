package com.patientmicroservice.patientservice.service;

import com.patientmicroservice.patientservice.model.Patient;
import com.patientmicroservice.patientservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepo;

    public List<Patient> fetchAllPatients() {
        return patientRepo.findAll();
    }

    public Optional<Patient> fetchPatientById(String id) {
        return patientRepo.findById(id);
    }

    public Patient createPatient(Patient patient) {
        return patientRepo.save(patient);
    }

    public Optional<Patient> updatePatient(String id, Patient patient) {
        return patientRepo.findById(id).map(p -> {
            p.setFirstName(patient.getFirstName());
            p.setLastName(patient.getLastName());
            p.setBirthDate(patient.getBirthDate());
            p.setPhoneNumber(patient.getPhoneNumber());
            p.setEmail(patient.getEmail());
            p.setSex(patient.getSex());
            return patientRepo.save(p);
        });
    }

    public void removePatient(String id) {
        patientRepo.deleteById(id);
    }
}

