package com.patientmicroservice.patientservice.controller;

import com.patientmicroservice.patientservice.model.Patient;
import com.patientmicroservice.patientservice.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PatientControllerTest {

    private MockMvc mockMvc; // MockMvc for testing endpoints

    @Mock
    private PatientService patientService; // Mocked PatientService

    @InjectMocks
    private PatientController patientController; // Injected PatientController

    @BeforeEach
    public void setUp() {
        // Initialize mocks and build MockMvc with the controller
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(patientController).build();
    }

    @Test
    public void testShowAddPatientForm() throws Exception {
        // Perform GET request to show add patient form
        mockMvc.perform(get("/patients/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("patient"))
                .andExpect(view().name("patientAdd")); // Ensure the view name matches your template
    }

    @Test
    public void testGetPatientById() throws Exception {
        // Arrange: Mock service response
        Patient patient = new Patient("Khurram", "Hashmi", "1990-11-04", "1000007890", "khurramhashmi@gmail.com", "Male");
        patient.setPatientId("1");
        when(patientService.fetchPatientById("1")).thenReturn(Optional.of(patient));

        // Act & Assert: Perform GET request and verify the response
        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Khurram"))
                .andExpect(jsonPath("$.lastName").value("Hashmi"))
                .andExpect(jsonPath("$.birthDate").value("1990-11-04"))
                .andExpect(jsonPath("$.phoneNumber").value("1000007890"))
                .andExpect(jsonPath("$.email").value("khurramhashmi@gmail.com"))
                .andExpect(jsonPath("$.sex").value("Male"));
    }

    @Test
    public void testAddPatient() throws Exception {
        // Arrange: Mock service response
        Patient patient = new Patient("Jenny", "Sun", "1989-05-15", "123456789", "jenny.sun@example.com", "Female");
        when(patientService.createPatient(any(Patient.class))).thenReturn(patient);

        // Act & Assert: Perform POST request and verify redirection
        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "Jenny")
                        .param("lastName", "Sun")
                        .param("birthDate", "1989-05-15")
                        .param("phoneNumber", "123456789")
                        .param("email", "jenny.sun@example.com")
                        .param("sex", "Female"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients"));
    }
}
