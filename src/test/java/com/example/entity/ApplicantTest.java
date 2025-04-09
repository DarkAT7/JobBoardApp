package com.example.entity;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.exception.InvalidEmailException;

public class ApplicantTest {
    private Applicant applicant;
    private final String FIRST_NAME = "John";
    private final String LAST_NAME = "Doe";
    private final String EMAIL = "john.doe@example.com";
    private final String PHONE = "1234567890";

    @Before
    public void setUp() {
        applicant = new Applicant(FIRST_NAME, LAST_NAME, EMAIL, PHONE);
    }

    @Test
    public void testApplicantCreation() {
        assertNotNull("Applicant should not be null", applicant);
        assertEquals("First name should match", FIRST_NAME, applicant.getFirstName());
        assertEquals("Last name should match", LAST_NAME, applicant.getLastName());
        assertEquals("Email should match", EMAIL, applicant.getEmail());
        assertEquals("Phone should match", PHONE, applicant.getPhone());
    }

    @Test
    public void testCreateProfile() throws InvalidEmailException {
        Applicant newApplicant = new Applicant();
        newApplicant.createProfile(EMAIL, FIRST_NAME, LAST_NAME, PHONE);

        assertEquals("First name should match", FIRST_NAME, newApplicant.getFirstName());
        assertEquals("Last name should match", LAST_NAME, newApplicant.getLastName());
        assertEquals("Email should match", EMAIL, newApplicant.getEmail());
        assertEquals("Phone should match", PHONE, newApplicant.getPhone());
    }

    @Test(expected = InvalidEmailException.class)
    public void testCreateProfileWithInvalidEmail() throws InvalidEmailException {
        Applicant newApplicant = new Applicant();
        newApplicant.createProfile("invalid-email", FIRST_NAME, LAST_NAME, PHONE);
    }

    @Test
    public void testToString() {
        String applicantString = applicant.toString();
        assertTrue("ToString should contain first name", applicantString.contains(FIRST_NAME));
        assertTrue("ToString should contain last name", applicantString.contains(LAST_NAME));
        assertTrue("ToString should contain email", applicantString.contains(EMAIL));
        assertTrue("ToString should contain phone", applicantString.contains(PHONE));
    }
}
