d#!/bin/bash
if [ ! -d /tmp/spark-memory ]; then
  echo "Creating memory track dir"
  mkdir -p /tmp/spark-memory;
fi

sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2020/SchemEx-2020-0.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2020/SchemEx-2020-1.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2020/SchemEx-2020-2.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2020/SchemEx-2020-3.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2020/SchemEx-2020-4.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2020/SchemEx-2020-5.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2020/SchemEx-2020-6.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2020/SchemEx-2020-7.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2020/SchemEx-2020-8.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2020/SchemEx-2020-9.conf"
sbt "runMain Main resources/configs/DOGS_exp/SchemEx/2020/SchemEx-2020-10.conf"

