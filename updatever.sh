#!/bin/bash
set -x
version=$(grep version pom.xml |head -1|sed 's/.*<version>//;s/<\/version.*//')
newversion=$(echo $version |sed "s/qualifier/$1/")
find -name pom.xml |xargs sed -i "s%<!--version--><version>.*</version>%<!--version--><version>$newversion</version>%" 
find -name MANIFEST.MF |xargs sed -i "s/Bundle-Version:.*/Bundle-Version: $newversion/"

