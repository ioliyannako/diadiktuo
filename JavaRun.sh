#!/bin/bash


for dir in AllPublicXML/*; do
    echo "$dir"
    java -cp mysql-connector-java-8.0.11.jar:. ClinicalStudiesXml "$dir"
done

