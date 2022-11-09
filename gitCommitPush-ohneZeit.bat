@echo off
SET COMMIT=%USERNAME%
git add .
git commit -am "%COMMIT%"
git pull
git push
pause