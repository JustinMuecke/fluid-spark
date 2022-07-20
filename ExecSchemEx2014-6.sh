d#!/bin/bash
if [ ! -d /tmp/spark-memory ]; then
  echo "Creating memory track dir"
  mkdir -p /tmp/spark-memory;
fi

sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-47.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-48.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-49.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-50.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-51.conf"
