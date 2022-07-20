d#!/bin/bash
if [ ! -d /tmp/spark-memory ]; then
  echo "Creating memory track dir"
  mkdir -p /tmp/spark-memory;
fi

sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-39.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-40.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-41.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-42.conf"
