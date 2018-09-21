echo off

set myDirName = ".\build\libs"

if exist myDirName (
    cd build\libs
    del *.*
    cd ..\..
)

call gradlew clean build
call run
cd ..\..
