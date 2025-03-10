package com.patientmicroservice.patientservice.controller;

import com.patientmicroservice.patientservice.model.Patient;
import com.patientmicroservice.patientservice.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Controller
@RequestMapping("/patients")
public class PatientController {

    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);


    @Autowired
    private PatientService patientService;

    @GetMapping
    public String listPatients(Model model) {
        model.addAttribute("patients", patientService.fetchAllPatients());
        // patients.html is in templates folder
        return "patients";
    }

    @GetMapping("/add")
    public String showAddPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        // patientAdd.html is in templates folder
        return "patientAdd";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable String id) {
        Optional<Patient> patient = patientService.fetchPatientById(id);
        return patient.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public String addPatient(@ModelAttribute Patient patient, Model model) {
        patientService.createPatient(patient);
        // after saving, redirects to the patients list
        return "redirect:/patients";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable String id, @RequestBody Patient patient) {
        Optional<Patient> updatedPatient = patientService.updatePatient(id, patient);
        return updatedPatient.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // displays the form to edit a patient's details
    @GetMapping("/edit/{id}")
    public String showEditPatientForm(@PathVariable String id, Model model) {
        Optional<Patient> patient = patientService.fetchPatientById(id);
        if (patient.isPresent()) {
            model.addAttribute("patient", patient.get());
            return "updatePatient"; // returning the edit-patient.html template
        } else {
            return "redirect:/patients";
        }
    }
    // handles the form submission for editing a patient
    @PostMapping("/edit/{id}")
    public String editPatient(@PathVariable String id, @ModelAttribute Patient patient) {
        patientService.updatePatient(id, patient);
        return "redirect:/patients";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable String id) {
        logger.info("Deleting patient with ID: " + id);
        patientService.removePatient(id);
        return ResponseEntity.noContent().build();
    }
}
