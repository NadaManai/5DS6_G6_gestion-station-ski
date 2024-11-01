package tn.esprit.spring;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.MethodArgumentNotValidException;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISubscriptionRepository;
import tn.esprit.spring.services.SubscriptionServicesImpl;


import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class SubscriptionServicesImplTest {

    @Autowired
    private SubscriptionServicesImpl subscriptionService;

    @MockBean
    private ISubscriptionRepository subscriptionRepository;

    /*

    @Test
    public void shouldSaveSubscriptionWithSuccess(){
        Subscription subscription1 = new Subscription();
        subscription1.setStartDate(LocalDate.now());
        subscription1.setEndDate(LocalDate.now().plusMonths(9));
        subscription1.setPrice(100f);
        subscription1.setTypeSub(TypeSubscription.MONTHLY);

        Subscription savedSubscription = subscriptionServices.addSubscription(subscription1);

        Assertions.assertNotNull(savedSubscription);
        Assertions.assertNotNull(savedSubscription.getNumSub());

        Assertions.assertEquals(subscription1.getPrice(),savedSubscription.getPrice());
        Assertions.assertEquals(subscription1.getStartDate(),savedSubscription.getStartDate());
    }


    @Test
    public void shouldUpdateSubscriptionWithSuccess(){

        Subscription subscription1 = new Subscription();
        subscription1.setStartDate(LocalDate.now());
        subscription1.setEndDate(LocalDate.now().plusMonths(10));
        subscription1.setPrice(900f);
        subscription1.setTypeSub(TypeSubscription.ANNUAL);

        Subscription savedSubscription = subscriptionServices.addSubscription(subscription1);

        Subscription subscriptionToUpdate = savedSubscription;
        subscriptionToUpdate.setPrice(333f);
        subscriptionToUpdate.setTypeSub(TypeSubscription.SEMESTRIEL);

        savedSubscription = subscriptionServices.updateSubscription(subscriptionToUpdate);

        Assertions.assertNotNull(subscriptionToUpdate);
        Assertions.assertNotNull(subscriptionToUpdate.getNumSub());

        Assertions.assertEquals(subscriptionToUpdate.getPrice(),savedSubscription.getPrice());
        Assertions.assertEquals(subscriptionToUpdate.getStartDate(),savedSubscription.getStartDate());

    }
*/
    /*
    @Test
    public void shouldThrowInvalidEntityExceptionTest(){
        Subscription emptySubscription = new Subscription();

        NullPointerException expectedException = assertThrows(NullPointerException.class, ()-> subscriptionService.addSubscription(emptySubscription));

        //assertEquals(1,expectedException.);
        assertEquals("Veuillez remplir les champs vides !!", expectedException.getMessage());

    }
    */

    @Test
    public void testSomeFunctionality() {
        // Your test code here
        assertEquals(1, 1); // Example assertion
    }


    @Test
    public void getSubTest(){
        int size =0;
        when(subscriptionRepository.findAll()).thenReturn(Stream
                .of(new Subscription(10L,LocalDate.now(),LocalDate.now().plusMonths(4),2.5f, TypeSubscription.ANNUAL),
                        new Subscription(11L,LocalDate.now(),LocalDate.now().plusMonths(10),9.5f, TypeSubscription.MONTHLY))
                .collect(Collectors.toList()));

        if (subscriptionService.getAllSubscriptions() instanceof Collection) {
            size =((Collection<?>) subscriptionService.getAllSubscriptions()).size();
        }
        assertEquals(2,size); // mocked the repository
    }

    @Test
    public void getSubByIdTest() {
        Long id_test = 11L;
        Subscription subscription = new Subscription(10L, LocalDate.now(), LocalDate.now().plusMonths(4), 2.5f, TypeSubscription.ANNUAL);

        when(subscriptionRepository.findById(id_test)).thenReturn(Optional.of(subscription));
        Subscription retrievedSubscription = subscriptionService.retrieveSubscriptionById(id_test);

        assertEquals(subscription.getPrice(),retrievedSubscription.getPrice());
    }

    @Test
    public void saveSubTest(){
        Subscription subscription = new Subscription(20L, LocalDate.now(), LocalDate.now().plusMonths(12), 2.5f, TypeSubscription.SEMESTRIEL);
        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        assertEquals(subscription,subscriptionService.addSubscription(subscription));
    }

    @Test
    public void deleteSubTest(){
        Subscription subscription = new Subscription(10L, LocalDate.now(), LocalDate.now().plusMonths(4), 2.5f, TypeSubscription.ANNUAL);
        subscriptionService.deleteSubscription(subscription);

        verify(subscriptionRepository,times(1)).delete(subscription);
    }




}

