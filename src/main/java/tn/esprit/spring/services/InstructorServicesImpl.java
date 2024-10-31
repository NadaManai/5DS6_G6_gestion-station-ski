package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class InstructorServicesImpl implements IInstructorServices {

    private static final Logger logger = LogManager.getLogger(InstructorServicesImpl.class);

    private IInstructorRepository instructorRepository;
    private ICourseRepository courseRepository;

    @Override
    public Instructor addInstructor(Instructor instructor) {
        logger.info("Adding new instructor: {}", instructor);
        Instructor savedInstructor = instructorRepository.save(instructor);
        logger.info("Instructor added successfully with ID: {}", savedInstructor.getNumInstructor());
        return savedInstructor;
    }

    @Override
    public List<Instructor> retrieveAllInstructors() {
        logger.info("Retrieving all instructors");
        List<Instructor> instructors = instructorRepository.findAll();
        logger.debug("Instructors retrieved: {}", instructors);
        return instructors;
    }

    @Override
    public Instructor updateInstructor(Instructor instructor) {
        logger.info("Updating instructor: {}", instructor);
        Instructor updatedInstructor = instructorRepository.save(instructor);
        logger.info("Instructor updated successfully with ID: {}", updatedInstructor.getNumInstructor());
        return updatedInstructor;
    }

    @Override
    public Instructor retrieveInstructor(Long numInstructor) {
        logger.info("Retrieving instructor with ID: {}", numInstructor);
        Instructor instructor = instructorRepository.findById(numInstructor).orElse(null);
        if (instructor == null) {
            logger.warn("Instructor with ID: {} not found", numInstructor);
        } else {
            logger.info("Instructor retrieved: {}", instructor);
        }
        return instructor;
    }

    @Override
    public Instructor addInstructorAndAssignToCourse(Instructor instructor, Long numCourse) {
        logger.info("Adding instructor: {} and assigning to course ID: {}", instructor, numCourse);
        Course course = courseRepository.findById(numCourse).orElse(null);
        if (course == null) {
            logger.error("Course with ID: {} not found. Instructor not added.", numCourse);
            return null; // ou vous pouvez lancer une exception
        }
        Set<Course> courseSet = new HashSet<>();
        courseSet.add(course);
        instructor.setCourses(courseSet);
        Instructor savedInstructor = instructorRepository.save(instructor);
        logger.info("Instructor added and assigned to course successfully with ID: {}", savedInstructor.getNumInstructor());
        return savedInstructor;
    }
}
