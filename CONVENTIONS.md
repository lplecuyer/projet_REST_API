# Conventions

 - [Conventions git](#gitignore)
 - [Conventions de commits](#conventions-de-commits)
 - [Stratégie de branchage](#stratégie-de-branchage)
 - [Conventions de code](#conventions-de-code)
 - [Documentation](#documentation)
 - [Déclaration d'utilisation de l'IA](#déclaration-dutilisation-de-lia)

## Conventions git
___

### .gitignore

* Fichiers de configuration d’IDE personnels
* Fichiers avec des paths personnels (Ex: C:\\Users\\lewan\\Documents\\programmation)
* Fichiers contenant des secrets (clés, mot de passe, etc.)
* Fichiers auto-générés (Ex: .o)
* Builds
* Fichiers propre aux systèmes d’exploitation (Ex: .DS\_Store sur mac)
* Fichiers de log

### Conventions de commits

#### Quand

* Travail complété
* Faire une sauvegarde pour potentiellement y revenir plus tard
* Session de travail terminée
* Modification mineure (Ex: voir un typo après avoir fini une session de travail) (le moins possible)

#### Quoi

* Fichiers pour un seul travail en cours (Ex: une fonctionnalité, un bug fix, un refactor). Ne pas combiner plusieurs travaux en un commit
* Fichiers de code pour le travail en cours
* Fichiers d’assets ajoutés ou modifiés pour le travail en cours

#### Noms

 * Commits en français
 * Décrire de manière claire et simple ce que le commit change même si ce n’est qu’un typo.

## Stratégie de branchage
___

### Branches:

* Main: Branche principale. Tout ramener sur main une fois une branche terminée.
* Préfixes:

    * Nouvelle fonctionnalité: “newFeat/”
    * Amélioration de fonctionnalité “enhanceFeat/”
    * Fix de bug “bugFix/”
    * Refactoring “refactor/”
    * Documentation "doc/"
    * Tests "tests/"

### Procédure:

* Créer une branche pour chaque nouvelle fonctionnalité, bug fix et refactor avec le nom approprié: le préfixe + le but de la branche
* Respecter la procédure de commit décrit en haut
* Créer une pull request seulement une fois la fonctionnalité finalisée
* Si y a des conflits, effectuez *git pull origin main --rebase*
* Attendre qu’une autre personne regarde la PR et l’approuve
* Merge sur Main
* Fermer la branche terminée


## Conventions de code
___

### Casing

* Variables: camelCase
* Méthodes: camelCase
* Fonctions: camelCase
* Classes: PascalCase
* Constantes: SCREAMING\_SNAKE\_CASE

### Conventions de noms pour méthodes courantes

* Mutateurs: setAttribut()
* Accesseurs: getAttribut()
* Méthode d’équivalence: equals()
* Méthode de clonage: clone()
* Méthode de comparaison compareTo()

Noms de variables, méthodes, fonctions, classes, constantes: anglais

## Documentation
___

en français

* Toute classe doit être documentée de la sorte
* Description de quand et comment utiliser la classe
* Avertissements au besoin (bugs, edge cases, exceptions)
* Toute méthode non triviale (accesseur, mutateur, équivalence, constructeur, comparaison, clonage, etc.) doit être documentée de la sorte:

    * description de la méthode/fonction (peut être plus ou moins simple selon la complexité de la méthode)
    * newline
    * @author auteur(s)
    * @param nom du paramètre brève description du paramètre
    * @return description de la valeur de retour
    * @throws type d’exception cas qui déclenche(nt) l’exception

## Déclaration d'utilisation de l'IA
___

1. Commencer le travail sans utiliser d’IA.
2. Si le problème est en rapport avec le travail d’un coéquipier, lui demander de l’aide en premier.
3. Si un problème est rencontré, rechercher une solution sur des sites fiables.
4. Si le problème persiste, demander de l’aide à un quelqu'un.
5. Si rien n’est réglé, décrire le problème à ChatGPT textuellement et lui demander des pistes de la source de ce problème (sans scanner tout le code).
6. Si le problème est toujours là, utiliser ChatGPT pour le régler en formattant sa solution selon les conventions convenues et en s’assurant de bien comprendre sa réponse et qu’elle fonctionne bien.
