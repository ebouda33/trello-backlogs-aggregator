package io.tools.trellobacklogsaggregator.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.julienvey.trello.domain.TList;

@Service
public class StoryBoardService {
    private static final String LIVRE_EN_RECETTE = "Livré en recette";

    private static final String PRET_POUR_MISE_EN_QUALIFICATION = "Prêt pour mise en qualification";

    private static final String PRET_A_DEMONTRER = "Prêt à démontrer";

    private static final String PRET_POUR_DEV = "Prêt pour dev";

    private static final String A_LIVRER = "A livrer";

    private static final String EN_COURS = "En cours";

    private static final String EN_ATTENTE = "En attente";

    private static final String A_FAIRE = "A faire";

    private static final String A_CHIFFRER = "A chiffrer";

    private static final String A_VALIDER = "A valider";

    private static final String EN_COURS_DE_REDACTION = "En cours de rédaction";

    private static final String IDEES = "Idées";

    private static final Logger LOGGER = LoggerFactory.getLogger(StoryBoardService.class);

    private final String listsNamesAllowed[] = { IDEES, EN_COURS_DE_REDACTION, A_VALIDER, A_CHIFFRER, PRET_POUR_DEV, A_FAIRE,
            EN_ATTENTE, EN_COURS, A_LIVRER, PRET_A_DEMONTRER, PRET_POUR_MISE_EN_QUALIFICATION, LIVRE_EN_RECETTE };

    boolean ideesExist = false;
    boolean enCoursDeRedactionExist = false;
    boolean aValiderExist = false;
    boolean aChiffrerExist = false;
    boolean pretPourDevExist = false;
    boolean aFaireExist = false;
    boolean enAttenteExist = false;
    boolean enCoursExist = false;
    boolean aLivrerExist = false;
    boolean pretADemontrerExist = false;
    boolean pretPourMiseEnQualificationExist = false;
    boolean livreEnRecetteExist = false;

    public boolean checkListConsistency(List<TList> tlists) {
        tlists.forEach(tList -> {
            boolean isAllowed = false;
            for (String listNameAllowed : listsNamesAllowed) {
                if (listNameAllowed.toLowerCase().equals(tList.getName().trim().toLowerCase())) {
                    isAllowed = true;
                    break;
                }
            }
            if (!isAllowed) {
                LOGGER.error("La liste " + tList.getName() + " n'est pas conforme au modèle de backlog");
            }
            if (!ideesExist) {
                ideesExist = checkListExist(tList, IDEES);
            }
            if (!enCoursDeRedactionExist) {
                enCoursDeRedactionExist = checkListExist(tList, EN_COURS_DE_REDACTION);
            }
            if (!aValiderExist) {
                aValiderExist = checkListExist(tList, A_VALIDER);
            }
            if (!aChiffrerExist) {
                aChiffrerExist = checkListExist(tList, A_CHIFFRER);
            }
            if (!pretPourDevExist) {
                pretPourDevExist = checkListExist(tList, PRET_POUR_DEV);
            }
            if (!aFaireExist) {
                aFaireExist = checkListExist(tList, A_FAIRE);
            }
            if (!enAttenteExist) {
                enAttenteExist = checkListExist(tList, EN_ATTENTE);
            }
            if (!enCoursExist) {
                enCoursExist = checkListExist(tList, EN_COURS);
            }
            if (!aLivrerExist) {
                aLivrerExist = checkListExist(tList, A_LIVRER);
            }
            if (!pretADemontrerExist) {
                pretADemontrerExist = checkListExist(tList, PRET_A_DEMONTRER);
            }
            if (!pretPourMiseEnQualificationExist) {
                pretPourMiseEnQualificationExist = checkListExist(tList, PRET_POUR_MISE_EN_QUALIFICATION);
            }
            if (!livreEnRecetteExist) {
                livreEnRecetteExist = checkListExist(tList, LIVRE_EN_RECETTE);
            }

        });
        boolean consistency = ideesExist && enCoursDeRedactionExist && aValiderExist && aChiffrerExist && pretPourDevExist && aFaireExist
                && enAttenteExist && enCoursDeRedactionExist && aLivrerExist && pretADemontrerExist
                && pretPourMiseEnQualificationExist && livreEnRecetteExist;

        return consistency;
    }

    private boolean checkListExist(TList tlist, String nameTocheck) {
        return nameTocheck.toLowerCase().equals(tlist.getName().trim().toLowerCase());
    }

    public boolean checkListInSprint(TList tlist) {
        boolean inSprint = false;
        if (A_FAIRE.toLowerCase().equals(tlist.getName().toLowerCase())
                || EN_ATTENTE.toLowerCase().equals(tlist.getName().toLowerCase())
                || EN_COURS.toLowerCase().equals(tlist.getName().toLowerCase())
                || A_LIVRER.toLowerCase().equals(tlist.getName().toLowerCase())
                || PRET_A_DEMONTRER.toLowerCase().equals(tlist.getName().toLowerCase())
                || PRET_POUR_MISE_EN_QUALIFICATION.toLowerCase().equals(tlist.getName().toLowerCase())) {
            inSprint = true;
        }

        return inSprint;
    }
}
