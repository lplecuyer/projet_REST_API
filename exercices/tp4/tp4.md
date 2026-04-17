# TP4

## Rétrospective


## Open sourcing

1. 1. N'importe qui peut contribuer au projet; Des contributeurs de n'importe où peuvent aider à améliorer le code, proposer des changements ou des améliorations, ce qui peut grandement augmenter la valeur du projet sans devoir dépenser davantage. Cela permet aussi aux contributeurs de gagner de l'expérience dans la technologie utilisée, devenant potentiellement un bon candidat d'embauche pour la compagnie menant le projet.
   2. Le projet peut être utilisé par d'autres compagnies ou personnes, ce qui pourrait indirectement lui créer de la publicité et inciter plus de personnes à contribuer. Son accessibilité est également très appréciée pour le développement de d'autres petits projets.
   3. Il est plus facile de faire confiance à une application open source, car on peut savoir exactement ce que le code éxécute, ce qui rend difficle de cacher du code malveillant. Davantage d'experts - indépendants - peuvent alors scruter le code et attester de sa fiabilité, devenant en quelque sorte un argument de vente, qui augmente la confiance des utilisateurs envers celui-ci.

2. 1. Comme tout le monde peut y participer, des personnes malintentionnées peuvent tenter d'implémenter des chevals de troie. Les reviewers doivent alors analyser le code avec vigilance et de façon extensive. Implémenter des outils automatisés d'analyse de sécurité et restreindre les permissions aux nouveaux venus est alors nécessaire.
   2. Coordonner la communauté qui participe au développement peut être difficile. Contrairement au personnel d'entreprise, les contributeurs ne sont pas nécessairement au courant des dernières décisions prises et ceux-ci ont potentiellement des façons de travailler ou de structurer leur code très différentes. Il faut alors implémenter des canaux/voies de communications efficaces et des standards précis pour contrôler la qualité.
   3. Si le projet projet s'appuie sur d'autres projets ou librairies, il peut être alors pénible et exigeant de vérifier quelles sont les permissions de distribution de chacun et d'en conclure quelle sera la license appropriée que l'on pourra choisir pour notre projet.
 
3. 

### Code of conduct
Le modèle de code de conduite est le [Contributor Covenant](https://www.contributor-covenant.org/). Il est déjà utilisé par une vaste majorité de projets. Celui-ci est simple et efficace, proposant une liste complète de comportements à privilégier, à proscrire, la façon de rapporter une infraction et enfin les conséquences qui suivront au mauvais comportement.

### Licence

La license choisie est l'APACHE 2.0. Tout d'abord, identifions quelques aspects de notre projet. Notre projet consiste en une API pour une boutique en ligne. L'utilisation principale risque davantage d'être par des d'être des entreprises que par d'autres projets open-source.

Pour cela, il ne faut pas une license comme GPL ou toute version qui dérive du projet soit obligatoirement open-source et puisse être redistribuée gratuitement. La nouvelle version peut donc être privée et être vendue.
Ensuite, cette license offre une protection contre les poursuites judiciaires, tant pour les contributeurs que les entreprises. Les contributeurs ne peuvent pas légalement être tenus pour responsables à ceux l'utilisant.
De même, elle protège les entreprises, qui possèdent alors les droits des brevets du projet permettant de les utiliser partout et à des fins lucratives.
Les créateurs du projet original gardent toutefois la marque déposée du projet, (ou Trademark, comme le nom ou le logo), ne permettant pasaux entreprises de les utiliser.
Ces clauses, qui ne sont pas dans les license du MIT ni du BSD sont intéressantes non seulement pour les compagnies mais aussi pour les contributeurs.

## ScreenShots

### L'architecture de classes

### GithubProject


### Milestone


### Issue 1


### Issue 2


### Issue 3

### Pull request 1

### Pull request 2


### Pull request 3


### Arbre de commits



## Déclaration d'utilisation de l'IA
1. Commencer le travail sans utiliser d’IA.
2. Si le problème est en rapport avec le travail d’un coéquipier, lui demander de l’aide en premier.
3. Si un problème est rencontré, rechercher une solution sur google avec des sites fiables (StackOverflow, W3Schools, GeeksForGeeks) ou les notes de cours.
4. Si le problème persiste, demander de l’aide à un coéquipier.
5. Si rien n’est réglé, décrire le problème à ChatGPT textuellement et lui demander des pistes de la source de ce problème (sans scanner tout le code).
6. Si le problème est toujours là, utiliser ChatGPT pour le régler en formattant sa solution selon les conventions convenues et en s’assurant de bien comprendre sa réponse et qu’elle fonctionne bien.

## Formattage
`mvn editorconfig:check`

