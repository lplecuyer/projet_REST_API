# TP4

## Rétrospective


## Open sourcing

1. 1. N'importe qui peut contribuer au projet; Des contributeurs de n'importe où peuvent aider à améliorer le code, proposer des changements ou des améliorations, ce qui peut grandement augmenter la valeur du projet sans devoir dépenser davantage. Cela permet aussi aux contributeurs de gagner de l'expérience dans la technologie utilisée, devenant potentiellement un bon candidat d'embauche pour la compagnie menant le projet.
   2. Le projet peut être utilisé par d'autres compagnies ou personnes, ce qui pourrait indirectement lui créer de la publicité et inciter plus de personnes à contribuer. Son accessibilité est également très appréciée pour le développement de d'autres petits projets.
   3. Il est plus facile de faire confiance à une application open source, car on peut savoir exactement ce que le code exécute, ce qui rend difficile de cacher du code malveillant. Davantage d'experts - indépendants - peuvent alors scruter le code et attester de sa fiabilité, devenant en quelque sorte un argument de vente, qui augmente la confiance des utilisateurs envers celui-ci.

2. 1. Comme tout le monde peut y participer, des personnes malintentionnées peuvent tenter d'implémenter des chevals de troie. Les reviewers doivent alors analyser le code avec vigilance et de façon extensive. Implémenter des outils automatisés d'analyse de sécurité et restreindre les permissions aux nouveaux venus est alors nécessaire.
   2. Coordonner la communauté qui participe au développement peut être difficile. Contrairement au personnel d'entreprise, les contributeurs ne sont pas nécessairement au courant des dernières décisions prises et ceux-ci ont potentiellement des façons de travailler ou de structurer leur code très différentes. Il faut alors implémenter des canaux/voies de communications efficaces et des standards précis pour contrôler la qualité.
   3. Si le projet s'appuie sur d'autres projets ou librairies, il peut être alors pénible et exigeant de vérifier quelles sont les permissions de distribution de chacun et d'en conclure quelle sera la license appropriée que l'on pourra choisir pour notre projet.
 
3. 

### Code of conduct
Le modèle de code de conduite est le [Contributor Covenant](https://www.contributor-covenant.org/).
Il est déjà utilisé par une vaste majorité de projets. Celui-ci est simple et efficace, proposant une liste complète de comportements à privilégier, à proscrire, la façon de rapporter une infraction et enfin les conséquences qui suivront au mauvais comportement.

### Licence

La license choisie est l'APACHE 2.0. Tout d'abord, identifions quelques aspects de notre projet. Celui-ci consiste en une API pour une boutique en ligne. Les principaux utilisateurs risquent davantage d'être des entreprises que d'autres projets open-source, petits ou pas. Il n'est pas destiné à être une libraire utilitaire.

Pour cela, il ne faut pas une license comme GPL ou toute version qui dérive du projet soit obligatoirement open-source et puisse être redistribuée gratuitement. Avec Apache 2.0, la nouvelle version peut donc être privée et être vendue.
Ensuite, cette license offre une protection contre les poursuites judiciaires, tant pour les contributeurs que les entreprises. Si le code engendre des dégâts de toute sorte à l'entreprise, les contributeurs ne peuvent pas légalement être tenus pour responsables par ceux l'utilisant.
De même, elle protège les entreprises, qui possèdent alors les droits des brevets du projet permettant les utiliser partout et à des fins lucratives.
Les créateurs du projet original gardent toutefois la marque déposée du projet, (ou Trademark, comme le nom ou le logo), ne permettant pas aux entreprises de les utiliser.
Ces clauses, qui ne sont pas dans les license du MIT ni du BSD sont intéressantes non seulement pour les compagnies mais aussi pour les contributeurs.

### Contributing
Le guide de contribution s'est en partie inspiré du template suivant : [contributing-template](https://github.com/nayafia/contributing-template). 
Il suggère plusieurs sections qu'il ne faut pas ommettre comme la clause sur les vulnérabilités. Ce modèle donne une bonne idée de la démarche à suivre tout en étant clair et concis.

## ScreenShots

### L'architecture de classes
![](./images/)

### GithubProject
![GithubProject](./images/Projet.png)

### Milestone
![Milestone](./images/Milestone.png)

### Issue 1
![Issue1](./images/Issue1-1.png)
![Issue1](./images/Issue1-2.png)


### Issue 2
![Issue2](./images/Issue2-1.png)
![Issue2](./images/Issue2-2.png)

### Issue 3
![Issue3](./images/Issue3.png)

### Pull request 1
![PR1-1](./images/PR1-1.png)
![PR1-2](./images/PR1-2.png)
### Pull request 2
![PR2-1](./images/PR2-1.png)
![PR2-2](./images/PR2-2.png)

### Pull request 3
![PR3-1](./images/PR3-1.png)
![PR3-2](./images/PR3-2.png)

### Arbre de commits
![](./images/)


## Déclaration d'utilisation de l'IA
1. Commencer le travail sans utiliser d’IA.
2. Si le problème est en rapport avec le travail d’un coéquipier, lui demander de l’aide en premier.
3. Si un problème est rencontré, rechercher une solution sur google avec des sites fiables (StackOverflow, W3Schools, GeeksForGeeks) ou les notes de cours.
4. Si le problème persiste, demander de l’aide à un coéquipier.
5. Si rien n’est réglé, décrire le problème à ChatGPT textuellement et lui demander des pistes de la source de ce problème (sans scanner tout le code).
6. Si le problème est toujours là, utiliser ChatGPT pour le régler en formattant sa solution selon les conventions convenues et en s’assurant de bien comprendre sa réponse et qu’elle fonctionne bien.

## Formattage
`mvn editorconfig:check`


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