@echo off

set JPDA=-agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=y

set CMD_LINE_ARGS=
:setArgs
if ""%1""=="""" goto doneSetArgs
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto setArgs
:doneSetArgs

startjetty %CMD_LINE_ARGS%
