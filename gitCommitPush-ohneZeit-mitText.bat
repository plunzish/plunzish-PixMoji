@echo off
set /p COMMITTEXT="Enter Commit Text: "
SET COMMIT=%USERNAME%-%COMMITTEXT%
git add .
git commit -am "%COMMIT%"
git pull
git push
pause