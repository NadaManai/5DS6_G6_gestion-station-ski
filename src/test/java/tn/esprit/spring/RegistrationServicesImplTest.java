package tn.esprit.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.services.RegistrationServicesImpl;

import java.time.LocalDate;
import java.util.Optional;

class RegistrationServicesImplTest {

    @InjectMocks
    private RegistrationServicesImpl registrationServices;

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ICourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addRegistrationAndAssignToSkier_ShouldSaveRegistration() {
        // Arrange
        Skier skier = new Skier();
        skier.setNumSkier(1L);
        Registration registration = new Registration();
        registration.setNumWeek(1);
        when(skierRepository.findById(anyLong())).thenReturn(Optional.of(skier));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        // Act
        Registration result = registrationServices.addRegistrationAndAssignToSkier(registration, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(skier, result.getSkier());
        verify(registrationRepository).save(registration);
    }

    @Test
    void assignRegistrationToCourse_ShouldAssignCourse() {
        // Arrange
        Registration registration = new Registration();
        Course course = new Course();
        course.setNumCourse(1L);
        when(registrationRepository.findById(anyLong())).thenReturn(Optional.of(registration));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        // Act
        Registration result = registrationServices.assignRegistrationToCourse(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(course, result.getCourse());
        verify(registrationRepository).save(registration);
    }

    @Test
    void addRegistrationAndAssignToSkierAndCourse_ShouldReturnNullWhenSkierOrCourseNotFound() {
        // Arrange
        Registration registration = new Registration();

        when(skierRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        // Assert
        assertNull(result);
    }

    @Test
    void addRegistrationAndAssignToSkierAndCourse_ShouldRegisterForIndividualCourse() {
        // Arrange
        Skier skier = new Skier();
        skier.setNumSkier(1L);
        skier.setDateOfBirth(LocalDate.now().minusYears(20)); // Age 20
        Course course = new Course();
        course.setNumCourse(1L);
        course.setTypeCourse(TypeCourse.INDIVIDUAL);
        Registration registration = new Registration(); // Initialize the registration object

        when(skierRepository.findById(anyLong())).thenReturn(Optional.of(skier));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(anyInt(), anyLong(), anyLong())).thenReturn(0L);
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration); // Add this line

        // Act
        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(skier, result.getSkier());
        assertEquals(course, result.getCourse());
    }


    // Additional tests can be added here for other scenarios

}
