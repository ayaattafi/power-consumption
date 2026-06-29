# Projet Big Data — Household Power Consumption

Cluster Hadoop (Docker) + traitements MapReduce sur le dataset UCI
**Individual Household Electric Power Consumption**.

## ⚠️ Dataset (non inclus dans le repo)
`household_power_consumption.txt`

## Build
- JDK **1.8** (obligatoire)
- IntelliJ → Maven → `package` → génère `target/powerconsumption-1.jar`

## Lancer sur le cluster
```bash
# copier jar + dataset vers le master
docker cp target/powerconsumption-1.jar hadoop-master:/root/
docker cp household_power_consumption.txt hadoop-master:/root/

docker exec -it hadoop-master bash
hdfs dfs -mkdir -p power_input
hdfs dfs -put household_power_consumption.txt power_input
```

## Partie 1 — Batch MapReduce (package TP)
```bash
hadoop jar powerconsumption-1.jar TP.PowerConsumptionDriver power_input power_output   # stats par mois
hadoop jar powerconsumption-1.jar TP.HourlyDriver power_input out_hourly               # profil horaire
hadoop jar powerconsumption-1.jar TP.WeekdayDriver power_input out_weekday             # semaine vs week-end
hadoop jar powerconsumption-1.jar TP.SubMeteringDriver power_input out_submetering     # sous-compteurs
hadoop jar powerconsumption-1.jar TP.QualityDriver power_input out_quality             # qualité des données
```

## Partie 2 — Spark Structured Streaming (à faire)
Ajouter au pom les dépendances Spark (compatibles Java 8).
