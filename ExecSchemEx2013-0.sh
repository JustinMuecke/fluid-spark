d#!/bin/bash
if [ ! -d /tmp/spark-memory ]; then
  echo "Creating memory track dir"
  mkdir -p /tmp/spark-memory;
fi

sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2013/SchemEx-2013-24.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2013/SchemEx-2013-25.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2013/SchemEx-2013-26.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2013/SchemEx-2013-27.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2013/SchemEx-2013-28.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2013/SchemEx-2013-29.conf"
