# Floppa

## Requis

- Java 21
- Maven 3.x

## Commandes

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