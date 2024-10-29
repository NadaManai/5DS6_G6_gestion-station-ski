package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.repositories.IPisteRepository;
import tn.esprit.spring.services.PisteServicesImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PisteServicesImplTest {

    @Mock
    private IPisteRepository pisteRepository;

    @InjectMocks
    private PisteServicesImpl pisteServices;

    private Piste piste;

    @BeforeEach
    public void setup() {
        piste = new Piste();
        piste.setNamePiste("Piste Test");
    }

    @Test
    public void testAddPiste() {
        when(pisteRepository.save(any(Piste.class))).thenReturn(piste);
        Piste addedPiste = pisteServices.addPiste(piste);
        assertNotNull(addedPiste);
        assertEquals("Piste Test", addedPiste.getNamePiste());
        verify(pisteRepository, times(1)).save(piste);
    }

    @Test
    public void testRetrieveAllPistes() {
        when(pisteRepository.findAll()).thenReturn(Arrays.asList(piste)); // Utilise Arrays.asList au lieu de List.of
        List<Piste> pistes = pisteServices.retrieveAllPistes();
        assertNotNull(pistes);
        assertFalse(pistes.isEmpty());
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    public void testRemovePiste() {
        when(pisteRepository.existsById(anyLong())).thenReturn(true);
        pisteServices.removePiste(1L);
        verify(pisteRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testRetrievePiste() {
        when(pisteRepository.findById(anyLong())).thenReturn(Optional.of(piste));
        Piste retrievedPiste = pisteServices.retrievePiste(1L);
        assertNotNull(retrievedPiste);
        assertEquals("Piste Test", retrievedPiste.getNamePiste());
        verify(pisteRepository, times(1)).findById(1L);
    }
}
