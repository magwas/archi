#!/bin/bash
# usage: $0 <uploadtarget>
# e.g:
# ./drone.sh "magwas,archistyledhtml@frs.sourceforge.net:/home/pfs/project/archici"
set -x
date
mvnversion=$(grep version pom.xml |head -1|sed 's/.*<version>//;s/<.*//')
branchname=$(echo $DRONE_BRANCH-$DRONE_BUILD_NUMBER | sed 'sA/A-A')
./updatever.sh $branchname
version=$(echo $mvnversion |sed "s/qualifier/$branchname/")
sudo apt-get update
sudo apt-get install libwebkitgtk-1.0-0 devscripts maven xvfb wine wkhtmltopdf
sudo Xvfb -nolock :99 -screen 0 1024x768x24+32&
export DISPLAY=:99
sleep 3s; xterm&#keep the display
mvn install
date
mkdir -p target/upload
mv archi/com.archimatetool.build/target/products/Archi-*.zip target/upload
tar czf target/upload/branding.tar.gz archi/com.archimatetool.build/branding/ archi/com.archimatetool.build/examples/ archi/com.archimatetool.htmlmerger/target/
scp -r target/upload $1/intermediate/$version
git clone git@github.com:magwas/archi-packager.git target/archi-packager
echo $version >target/archi-packager/version
cd target/archi-packager
git add version archimate-makeinstallers.xml windows-installer.iss
git commit -m "archi build $version"
git push
kill `ps ax |grep xterm|grep -v grep|awk '{print $1}'`
ps ax |grep Xvfb|egrep -v "grep|sudo"|awk '{print $1}' |xargs sudo kill 
ps afx
date
exit 0
