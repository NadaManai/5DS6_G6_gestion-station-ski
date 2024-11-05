package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.services.IRegistrationServices;

import java.util.List;

@Tag(name = "\uD83D\uDDD3️Registration Management")
@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationRestController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationRestController.class);
    private final IRegistrationServices registrationServices;

    @Operation(description = "Add Registration and Assign to Skier")
    @PutMapping("/addAndAssignToSkier/{numSkieur}")
    public Registration addAndAssignToSkier(@RequestBody Registration registration,
                                            @PathVariable("numSkieur") Long numSkieur) {
        logger.info("Request to add and assign registration to skier with ID: {}", numSkieur);
        Registration result = registrationServices.addRegistrationAndAssignToSkier(registration, numSkieur);
        logger.info("Registration added and assigned to skier with ID: {}", numSkieur);
        return result;
    }

    @Operation(description = "Assign Registration to Course")
    @PutMapping("/assignToCourse/{numRegis}/{numSkieur}")
    public Registration assignToCourse(@PathVariable("numRegis") Long numRegistration,
                                       @PathVariable("numSkieur") Long numCourse) {
        logger.info("Request to assign registration with ID: {} to course with ID: {}", numRegistration, numCourse);
        Registration result = registrationServices.assignRegistrationToCourse(numRegistration, numCourse);
        logger.info("Registration with ID: {} assigned to course with ID: {}", numRegistration, numCourse);
        return result;
    }

    @Operation(description = "Add Registration and Assign to Skier and Course")
    @PutMapping("/addAndAssignToSkierAndCourse/{numSkieur}/{numCourse}")
    public Registration addAndAssignToSkierAndCourse(@RequestBody Registration registration,
                                                     @PathVariable("numSkieur") Long numSkieur,
                                                     @PathVariable("numCourse") Long numCourse) {
        logger.info("Request to add registration and assign to skier with ID: {} and course with ID: {}", numSkieur, numCourse);
        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, numSkieur, numCourse);
        logger.info("Registration added and assigned to skier with ID: {} and course with ID: {}", numSkieur, numCourse);
        return result;
    }

    @Operation(description = "Numbers of the weeks when an instructor has given lessons in a given support")
    @GetMapping("/numWeeks/{numInstructor}/{support}")
    public List<Integer> numWeeksCourseOfInstructorBySupport(@PathVariable("numInstructor") Long numInstructor,
                                                             @PathVariable("support") Support support) {
        logger.info("Request to retrieve number of weeks for instructor with ID: {} and support: {}", numInstructor, support);
        List<Integer> result = registrationServices.numWeeksCourseOfInstructorBySupport(numInstructor, support);
        logger.info("Retrieved number of weeks for instructor with ID: {} and support: {}", numInstructor, support);
        return result;
    }
}
