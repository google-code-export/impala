@echo off

if not "%JPDA%" == "" goto jpdaSet
set JPDA=-agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=y
:jpdaSet

if not "%PORT%" == "" goto portSet
set PORT=8080
:portSet

if "%CONTEXTPATH%" == goto contextPathSet
set CONTEXTPATH=/wineorder
:contextPathSet

@echo on
java %JPDA% -jar launcher.jar StartJetty --addclasspath config --addjardir jetty %PORT% war %CONTEXTPATH%