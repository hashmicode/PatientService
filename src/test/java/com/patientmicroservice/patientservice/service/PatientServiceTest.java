package com.patientmicroservice.patientservice.service;

import com.patientmicroservice.patientservice.model.Patient;
import com.patientmicroservice.patientservice.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatientServiceTest {

    @InjectMocks
    private PatientService patientService; // Service being tested.

    @Mock
    private PatientRepository patientRepo; // Mock repository.

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFetchAllPatients() {
        List<Patient> patients = Arrays.asList(
                new Patient("Khurram", "Hashmi", "1995-02-04", "1234567890", "khurramhashmi2010@gmail.com", "Male"),
                new Patient("Jane", "Jenny", "1995-02-02", "0987654321", "jane.doe@io.com", "Female")
        );
        when(patientRepo.findAll()).thenReturn(patients);

        List<Patient> result = patientService.fetchAllPatients();
        assertEquals(2, result.size());
        assertEquals("Khurram", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
    }

    @Test
    public void testFetchPatientById() {
        Patient patient = new Patient("Khurram", "Hashmi", "1995-02-04", "1234567890", "khurramhashmi2010@gmail.com", "Male");
        when(patientRepo.findById("1")).thenReturn(Optional.of(patient));

        Optional<Patient> result = patientService.fetchPatientById("1");
        assertTrue(result.isPresent());
        assertEquals("Khurram", result.get().getFirstName());
    }

    @Test
    public void testCreatePatient() {
        Patient patient = new Patient("Khurram", "Hashmi", "1995-02-04", "1234567890", "khurramhashmi2010@gmail.com", "Male");
        when(patientRepo.save(any(Patient.class))).thenReturn(patient);

        Patient result = patientService.createPatient(patient);
        assertNotNull(result);
        assertEquals("Khurram", result.getFirstName());
    }

    @Test
    public void testUpdatePatient() {
        Patient existingPatient = new Patient("John", "Doe", "1990-01-01", "1234567890", "john.doe@example.com", "Male");
        existingPatient.setPatientId("1");

        Patient updatedPatient = new Patient("Johnny", "Doe", "1990-01-01", "1234567890", "johnny.doe@example.com", "Male");

        when(patientRepo.findById("1")).thenReturn(Optional.of(existingPatient));
        when(patientRepo.save(any(Patient.class))).thenReturn(updatedPatient);

        Optional<Patient> result = patientService.updatePatient("1", updatedPatient);
        assertTrue(result.isPresent());
        assertEquals("Johnny", result.get().getFirstName());
        assertEquals("johnny.doe@example.com", result.get().getEmail());
    }

    @Test
    public void testRemovePatient() {
        doNothing().when(patientRepo).deleteById("1");

        patientService.removePatient("1");
        verify(patientRepo, times(1)).deleteById("1");
    }
}
