# Floppa
___
- [Qu'est-ce que Floppa ?](#quest-ce-que-floppa-)
- [Installation et éxécution](#installation-et-éxécution)
- [Autheurs](#autheurs)
- [Sources](#sources)
- [Badges](#badges)
## Qu'est-ce que Floppa ?
___
Floppa est un serveur open-source pouvant faire office d'API pour toute boutique en ligne.
Ce projet comprend les routes, la logique et la sauvegarde des informations à traiter à travers trois couches : 
API (Classes Resource), Application (la logique, classes Service), le domaine (définissant les objets modèles traités et le stockage in-memory) et enfin Persistence (stockage avec MongoDB).

Floppa est sous license Apache 2.0, offrant une base de code facile à reprendre pour les entreprises et les commerces indépendants à construire leur boutique en ligne.
Consultez LICENSE.txt pour en savoir plus.

Pour contribuer au projet, consultez les fichiers CODE_OF_CONDUCT.md ainsi que CONTRIBUTING.md.

## Installation et éxécution
___
### Requis

- [Java 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
- [Maven 3.x](https://maven.apache.org/download.cgi?.)
- [MongoDB](https://www.mongodbtutorial.org/getting-started/install-mongodb/)

### Compilation

```
mvn compile
```

### Exécution in-memory

```
mvn exec:java
```

### Exécution MongoDB

#### terminal 1
```
docker compose up -d
```

#### terminal 2 (bash)
```
export MONGO_CLUSTER_URL="mongodb://root:example@localhost:27017"
export MONGO_DATABASE="floppa"
mvn exec:java -Dpersistence=mongo
```

#### terminal 2 (powershell)
```
$env:MONGO_CLUSTER_URL="mongodb://root:example@localhost:27017/?authSource=admin"
$env:MONGO_DATABASE="glo2003"
mvn exec:java -Dpersistence=mongo
```

## Autheurs
___
- Colin Lewandowski (ColinLewandowski)
- Jean-Gabriel Bujold (Jean-Gabriel325)
- Andrei Iurii (Andrei-Iurii)
- Louis-Philippe L'Écuyer (lplecuyer)
- Samuel Langevin (SamuelLangevin)

## Sources
___
- [Contributor Covenant](https://www.contributor-covenant.org/)
- [contributing-template](https://github.com/nayafia/contributing-template)
- [Apache 2.0](https://choosealicense.com/licenses/apache-2.0/)

## Badges
___
