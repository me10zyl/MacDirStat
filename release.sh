#!/bin/bash
git add .
git commit -am "1"
git push origin master
git tag $1
git push $1
