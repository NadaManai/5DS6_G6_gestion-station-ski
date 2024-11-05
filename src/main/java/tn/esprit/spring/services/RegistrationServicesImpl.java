package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class RegistrationServicesImpl implements IRegistrationServices {

    private final IRegistrationRepository registrationRepository;
    private final ISkierRepository skierRepository;
    private final ICourseRepository courseRepository;

    @Override
    public Registration addRegistrationAndAssignToSkier(Registration registration, Long numSkier) {
        log.info("Adding registration and assigning to skier with ID: {}", numSkier);
        Skier skier = skierRepository.findById(numSkier).orElse(null);
        if (skier == null) {
            log.warn("Skier with ID: {} not found.", numSkier);
            return null;
        }
        registration.setSkier(skier);
        Registration savedRegistration = registrationRepository.save(registration);
        log.info("Registration added and assigned to skier with ID: {}", numSkier);
        return savedRegistration;
    }

    @Override
    public Registration assignRegistrationToCourse(Long numRegistration, Long numCourse) {
        log.info("Assigning registration with ID: {} to course with ID: {}", numRegistration, numCourse);
        Registration registration = registrationRepository.findById(numRegistration).orElse(null);
        Course course = courseRepository.findById(numCourse).orElse(null);
        if (registration == null || course == null) {
            log.warn("Either registration ID: {} or course ID: {} was not found.", numRegistration, numCourse);
            return null;
        }
        registration.setCourse(course);
        Registration savedRegistration = registrationRepository.save(registration);
        log.info("Registration with ID: {} assigned to course with ID: {}", numRegistration, numCourse);
        return savedRegistration;
    }

    @Transactional
    @Override
    public Registration addRegistrationAndAssignToSkierAndCourse(Registration registration, Long numSkieur, Long numCours) {
        log.info("Adding registration and assigning to skier with ID: {} and course with ID: {}", numSkieur, numCours);
        Skier skier = skierRepository.findById(numSkieur).orElse(null);
        Course course = courseRepository.findById(numCours).orElse(null);

        if (skier == null || course == null) {
            log.warn("Either skier ID: {} or course ID: {} was not found.", numSkieur, numCours);
            return null;
        }

        if (registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(registration.getNumWeek(), skier.getNumSkier(), course.getNumCourse()) >= 1) {
            log.warn("Skier with ID: {} is already registered for the course in week: {}", skier.getNumSkier(), registration.getNumWeek());
            return null;
        }

        int ageSkieur = Period.between(skier.getDateOfBirth(), LocalDate.now()).getYears();
        log.info("Skier's age: {}", ageSkieur);

        switch (course.getTypeCourse()) {
            case INDIVIDUAL:
                log.info("Assigning individual course without further checks.");
                return assignRegistration(registration, skier, course);

            case COLLECTIVE_CHILDREN:
                if (ageSkieur < 16) {
                    log.info("Assigning to Collective Children Course.");
                    if (registrationRepository.countByCourseAndNumWeek(course, registration.getNumWeek()) < 6) {
                        log.info("Course slot available. Registration successful.");
                        return assignRegistration(registration, skier, course);
                    } else {
                        log.warn("Course is full. Registration unsuccessful.");
                        return null;
                    }
                } else {
                    log.warn("Skier's age does not qualify for Collective Children Course.");
                }
                break;

            default:
                if (ageSkieur >= 16) {
                    log.info("Assigning to Collective Adult Course.");
                    if (registrationRepository.countByCourseAndNumWeek(course, registration.getNumWeek()) < 6) {
                        log.info("Course slot available. Registration successful.");
                        return assignRegistration(registration, skier, course);
                    } else {
                        log.warn("Course is full. Registration unsuccessful.");
                        return null;
                    }
                } else {
                    log.warn("Skier's age does not qualify for Collective Adult Course.");
                }
        }
        return registration;
    }

    private Registration assignRegistration(Registration registration, Skier skier, Course course) {
        log.info("Assigning skier ID: {} to course ID: {}", skier.getNumSkier(), course.getNumCourse());
        registration.setSkier(skier);
        registration.setCourse(course);
        Registration savedRegistration = registrationRepository.save(registration);
        log.info("Registration completed for skier ID: {} to course ID: {}", skier.getNumSkier(), course.getNumCourse());
        return savedRegistration;
    }

    @Override
    public List<Integer> numWeeksCourseOfInstructorBySupport(Long numInstructor, Support support) {
        log.info("Retrieving number of weeks for instructor ID: {} with support: {}", numInstructor, support);
        List<Integer> weeks = registrationRepository.numWeeksCourseOfInstructorBySupport(numInstructor, support);
        log.info("Number of weeks retrieved: {}", weeks);
        return weeks;
    }
}
