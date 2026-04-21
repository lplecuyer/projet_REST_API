# TP4

## Rapport de vulnérabilités

### Sommaire

![sommaire1](images/rapportVulnerabilitesSommaire1.png)
![sommaire2](images/rapportVulnerabilitesSommaire2.png)
![sommaire3](images/rapportVulnerabilitesSommaire3.png)

### Détaillé

1. aopalliance-repackaged-3.0.6.jar
    ![detaille1](images/rapportVulnerabilitesDetaille1.png)
    ![detaille1.1](images/rapportVulnerabilitesDetaille1.1.png)

2. asm-9.7.1.jar
   ![detaille2](images/rapportVulnerabilitesDetaille2.png)
   ![detaille2.1](images/rapportVulnerabilitesDetaille2.1.png)
   ![detaille2.2](images/rapportVulnerabilitesDetaille2.2.png)

3. bson-5.6.1.jar
   ![detaille3](images/rapportVulnerabilitesDetaille3.png)
   ![detaille3.1](images/rapportVulnerabilitesDetaille3.1.png)

### Pratiques de sécurité

1. Intégrer un processus automatisé d'analyse de sécurité

    La conception d'un système d'analyse de sécurité automatisé permet de faire des vérifications en continu des
    failles de sécurité potentielles dans la base de code. Celui-ci s'intègre bien dans un pipeline comme celui
    du CI/CD.


2. Analyser la sécurité de façon itérative

    Une grande partie de l'industrie du logiciel aujourd'hui utilise un processus itératif pour développer leurs
    logiciels. L'ajout d'une étape de sécurité avec les vérifications de code permet d'obtenir les bénéfices
    de l'agilité (l'amélioration continue, la séparation en livrables plus simples) pour cette étape de
    développement.


3. Utiliser des environnements de developpement qui prennent en compte la sécurité

    Il est complexe pour les développeurs de faire des analyses de sécurité pendant l'étape de création de code.
    Cependant, certains IDE vont détecter des failles de sécurité automatiquement pendant son implémentation.
    Prioriser et corriger ces erreurs pendant le développement est moins couteux que dans une autre étape par
    la suite.