package tn.esprit.spring;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.services.ICourseServices;


import java.util.List;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)


class GestionStationSkiApplicationTests {

	@Autowired
	ICourseServices cs;
	@Test
	@Order(1)
	void getAllCoursess() {
		List<Course> listUsers = cs.retrieveAllCourses();
		Assertions.assertEquals(0, listUsers.size());
		}
	}

