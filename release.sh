#!/bin/bash
if [ "$#" -ne 1  ];then
	echo "usage: ./release.sh v1.0.0"
	git tag
	exit 1
fi
git add .
git commit -am "1"
git push origin master
git tag $1
git push origin $1
