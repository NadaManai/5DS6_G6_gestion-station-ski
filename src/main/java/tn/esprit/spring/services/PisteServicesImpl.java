package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.repositories.IPisteRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import java.util.List;
@AllArgsConstructor
@Service
public class PisteServicesImpl implements  IPisteServices{

    private IPisteRepository pisteRepository;
    private static final Logger logger = LogManager.getLogger(PisteServicesImpl.class);
    @PostConstruct
    public void init() {
        testerLogs();  // Appel de la méthode de test de journalisation au démarrage
    }

    public void testerLogs() {
        logger.info("Début du test de journalisation dans testerLogs");
        logger.debug("Ceci est un message DEBUG dans testerLogs");
        logger.warn("Ceci est un message WARN dans testerLogs");
        logger.error("Ceci est un message ERROR dans testerLogs");
        logger.info("Fin du test de journalisation dans testerLogs");
    }

    @Override
    public List<Piste> retrieveAllPistes() {
        logger.info("Appel de retrieveAllPistes");
        List<Piste> pistes = pisteRepository.findAll();
        logger.info("Nombre de pistes récupérées : {}", pistes.size());
        return pistes;
    }

    @Override
    public Piste addPiste(Piste piste) {
        logger.info("Début de addPiste avec les données : {}", piste);
        try {
            Piste savedPiste = pisteRepository.save(piste);
            logger.info("Piste ajoutée avec succès : {}", savedPiste);
            return savedPiste;
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout de la piste : {}", piste, e);
            throw e;
        }
    }

    @Override
    public void removePiste(Long numPiste) {
        logger.info("Tentative de suppression de la piste avec ID : {}", numPiste);
        if (pisteRepository.existsById(numPiste)) {
            pisteRepository.deleteById(numPiste);
            logger.info("Piste avec ID {} supprimée avec succès", numPiste);
        } else {
            logger.warn("Piste avec ID {} non trouvée pour suppression", numPiste);
        }
    }

    @Override
    public Piste retrievePiste(Long numPiste) {
        logger.info("Récupération de la piste avec ID : {}", numPiste);
        Piste piste = pisteRepository.findById(numPiste).orElse(null);
        if (piste != null) {
            logger.info("Piste récupérée : {}", piste);
        } else {
            logger.warn("Aucune piste trouvée avec ID : {}", numPiste);
        }
        return piste;
    }
}
