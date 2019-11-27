#!/bin/bash
sbt clean
sbt assembly
export PATH=$PATH:`pwd`/target/scala-2.13