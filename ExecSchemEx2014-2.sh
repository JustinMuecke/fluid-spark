d#!/bin/bash
if [ ! -d /tmp/spark-memory ]; then
  echo "Creating memory track dir"
  mkdir -p /tmp/spark-memory;
fi

sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-31.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-32.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-33.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2014/SchemEx-2014-34.conf"
