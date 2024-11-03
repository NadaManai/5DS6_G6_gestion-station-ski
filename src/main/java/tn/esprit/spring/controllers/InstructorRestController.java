package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.services.IInstructorServices;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;

import java.util.List;

@Tag(name = "\uD83D\uDC69\u200D\uD83C\uDFEB Instructor Management")
@RestController
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorRestController {

    private static final Logger logger = LogManager.getLogger(InstructorRestController.class);

    private final IInstructorServices instructorServices;
    private final MeterRegistry meterRegistry;

    // Déclaration des compteurs pour les métriques
    private final Counter instructorAddedCounter;
    private final Counter instructorUpdatedCounter;
    private final Counter instructorRetrievedCounter;

    // Lombok gère déjà l'initialisation des dépendances ici
    public InstructorRestController(IInstructorServices instructorServices, MeterRegistry meterRegistry) {
        this.instructorServices = instructorServices;
        this.meterRegistry = meterRegistry;

        // Initialisation des compteurs
        this.instructorAddedCounter = meterRegistry.counter("instructor.added");
        this.instructorUpdatedCounter = meterRegistry.counter("instructor.updated");
        this.instructorRetrievedCounter = meterRegistry.counter("instructor.retrieved");
    }

    @Operation(description = "Add Instructor")
    @PostMapping("/add")
    public Instructor addInstructor(@RequestBody Instructor instructor) {
        logger.info("Received request to add instructor: {}", instructor);
        Instructor addedInstructor = instructorServices.addInstructor(instructor);
        logger.info("Instructor added successfully with ID: {}", addedInstructor.getNumInstructor());

        // Incrémentation du compteur
        instructorAddedCounter.increment();
        return addedInstructor;
    }

    @Operation(description = "Add Instructor and Assign To Course")
    @PutMapping("/addAndAssignToCourse/{numCourse}")
    public Instructor addAndAssignToInstructor(@RequestBody Instructor instructor, @PathVariable("numCourse") Long numCourse) {
        logger.info("Received request to add instructor: {} and assign to course ID: {}", instructor, numCourse);
        Instructor addedInstructor = instructorServices.addInstructorAndAssignToCourse(instructor, numCourse);
        if (addedInstructor != null) {
            logger.info("Instructor added and assigned to course successfully with ID: {}", addedInstructor.getNumInstructor());
            instructorAddedCounter.increment(); // Incrémentation si l'instructeur a été ajouté
        } else {
            logger.error("Failed to add instructor and assign to course ID: {}", numCourse);
        }
        return addedInstructor;
    }

    @Operation(description = "Retrieve all Instructors")
    @GetMapping("/all")
    public List<Instructor> getAllInstructors() {
        logger.info("Received request to retrieve all instructors");
        List<Instructor> instructors = instructorServices.retrieveAllInstructors();
        logger.debug("Retrieved instructors: {}", instructors);
        
        // Incrémentation du compteur
        instructorRetrievedCounter.increment(instructors.size());
        return instructors;
    }

    @Operation(description = "Update Instructor")
    @PutMapping("/update")
    public Instructor updateInstructor(@RequestBody Instructor instructor) {
        logger.info("Received request to update instructor: {}", instructor);
        Instructor updatedInstructor = instructorServices.updateInstructor(instructor);
        logger.info("Instructor updated successfully with ID: {}", updatedInstructor.getNumInstructor());

        // Incrémentation du compteur
        instructorUpdatedCounter.increment();
        return updatedInstructor;
    }

    @Operation(description = "Retrieve Instructor by Id")
    @GetMapping("/get/{id-instructor}")
    public Instructor getById(@PathVariable("id-instructor") Long numInstructor) {
        logger.info("Received request to retrieve instructor with ID: {}", numInstructor);
        Instructor instructor = instructorServices.retrieveInstructor(numInstructor);
        if (instructor == null) {
            logger.warn("Instructor with ID: {} not found", numInstructor);
        } else {
            logger.info("Retrieved instructor: {}", instructor);
            instructorRetrievedCounter.increment(); // Incrémentation si l'instructeur a été trouvé
        }
        return instructor;
    }
}
