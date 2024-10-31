package tn.esprit.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;
import tn.esprit.spring.services.InstructorServicesImpl;

import java.time.LocalDate;
import java.util.*;

public class InstructorServicesTest {

    @InjectMocks
    private InstructorServicesImpl instructorServices;

    @Mock
    private IInstructorRepository instructorRepository;

    @Mock
    private ICourseRepository courseRepository;

    private Instructor instructor;
    private Course course1;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        instructor = new Instructor(1L, "John", "Doe", LocalDate.now(), new HashSet<>());
        course1 = new Course(1L, 1, TypeCourse.COLLECTIVE_CHILDREN, Support.SKI, 50.0f, 2, new HashSet<>());
    }

    @Test
    public void testAddInstructor() {
        when(instructorRepository.save(instructor)).thenReturn(instructor);

        Instructor addedInstructor = instructorServices.addInstructor(instructor);

        assertNotNull(addedInstructor);
        assertEquals(instructor.getFirstName(), addedInstructor.getFirstName());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    public void testRetrieveAllInstructors() {
        List<Instructor> instructors = Arrays.asList(instructor);
        when(instructorRepository.findAll()).thenReturn(instructors);

        List<Instructor> retrievedInstructors = instructorServices.retrieveAllInstructors();

        assertEquals(1, retrievedInstructors.size());
        assertEquals(instructor.getFirstName(), retrievedInstructors.get(0).getFirstName());
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateInstructor() {
        instructor.setLastName("Smith");
        when(instructorRepository.save(instructor)).thenReturn(instructor);

        Instructor updatedInstructor = instructorServices.updateInstructor(instructor);

        assertNotNull(updatedInstructor);
        assertEquals("Smith", updatedInstructor.getLastName());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    public void testRetrieveInstructor() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));

        Instructor retrievedInstructor = instructorServices.retrieveInstructor(1L);

        assertNotNull(retrievedInstructor);
        assertEquals(instructor.getFirstName(), retrievedInstructor.getFirstName());
        verify(instructorRepository, times(1)).findById(1L);
    }

    @Test
    public void testAddInstructorAndAssignToCourse() {
        when(courseRepository.findById(course1.getNumCourse())).thenReturn(Optional.of(course1));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor addedInstructor = instructorServices.addInstructorAndAssignToCourse(instructor, course1.getNumCourse());

        assertNotNull(addedInstructor);
        assertTrue(addedInstructor.getCourses().contains(course1));
        verify(instructorRepository, times(1)).save(instructor);
        verify(courseRepository, times(1)).findById(course1.getNumCourse());
    }

    @Test
    public void testAddInstructorAndAssignToCourseCourseNotFound() {
        when(courseRepository.findById(course1.getNumCourse())).thenReturn(Optional.empty());
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor addedInstructor = instructorServices.addInstructorAndAssignToCourse(instructor, course1.getNumCourse());

        assertNotNull(addedInstructor); // Vérifie que l'instructeur est sauvegardé
        assertNotNull(addedInstructor.getCourses()); // Vérifie que le set de cours n'est pas null
        assertEquals(1, addedInstructor.getCourses().size()); // Vérifie que le set de cours contient un élément
        assertTrue(addedInstructor.getCourses().contains(null)); // Vérifie que cet élément est null
        verify(instructorRepository, times(1)).save(instructor);
        verify(courseRepository, times(1)).findById(course1.getNumCourse());
    }
}