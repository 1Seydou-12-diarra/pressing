package com.gestionPressing.demo.domain.enums

enum StatutCommande {


        DEPOSE,
        EN_TRAITEMENT,
        PRET,
        RETIRE

        List<StatutCommande> transitionsAutorisees() {
            switch (this) {
                case DEPOSE:
                    return [EN_TRAITEMENT]
                case EN_TRAITEMENT:
                    return [PRET]
                case PRET:
                    return [RETIRE]
                case RETIRE:
                    return []
            }
        }

        boolean peutTransitionnerVers(StatutCommande cible) {
            transitionsAutorisees().contains(cible)
        }
    }

