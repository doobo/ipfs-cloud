#!/bin/bash
#git init .
#git remote add origin git@github.com:doobo/ipfs-cloud.git
#git add .
#git commit -m "版本更新"
#git push origin master --force
#git branch --set-upstream-to=origin/master master

#git remote add gitee git@gitee.com:doobo/ipfs-cloud.git
#git push gitee master --force

mvn clean
mvn install
#mvn release:prepare
#mvn release:perform
