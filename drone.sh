#!/bin/bash
# usage: $0 <debemail> <uploadtarget>
# e.g:
# ./drone.sh "Travis Zorp <mag+travis@magwas.rulez.org>" "magwas,archistyledhtml@frs.sourceforge.net:/home/pfs/project/archici"
set -x
mkdir ~/Downloads
sudo apt-get update
sudo apt-get install libwebkitgtk-1.0-0 devscripts maven xvfb wine
sudo Xvfb -nolock :99 -screen 0 1024x768x24+32&
export DISPLAY=:99
wget -O ~/Downloads/archi-extra.tar.gz http://magwas.rulez.org/archi-extra.tar.gz
xterm&#keep the display
mvn integration-test
mvnversion=$(grep version pom.xml |head -1|sed 's/.*<version>//;s/<.*//')
branchname=$(echo $DRONE_BRANCH-$DRONE_BUILD_NUMBER | sed 'sA/A-A')
./updatever.sh $branchname
version=$(echo $mvnversion |sed "s/qualifier/$branchname/")
DEBEMAIL="$1" dch -v $version -b -D zenta --force-distribution "drone.io $DRONE_BRANCH build $DRONE_BUILD_NUMBER"
debuild -us -uc
if [ $DRONE_BRANCH = "master" ]
then
     DEPLOYMENT=stable
else
     DEPLOYMENT=unstable
fi
dest=target/upload
mv ../*.deb $dest
scp -r target/upload/* $2
