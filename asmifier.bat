@echo off
rem Windows Batch File for running ASMifier
rem usage:
rem   asmifier <package>.<class>
rem Before you can run this...
rem Set the current directory to the project directory
set ARG1=%1
if "%ARG1%" == "" set ARG1=java.lang.String
echo Dumping %ARG1%
java --class-path C:\Users\jimcl\.m2\repository\org\ow2\asm\asm-util\9.7.1\asm-util-9.7.1.jar;C:\Users\jimcl\.m2\repository\org\ow2\asm\asm\9.7.1\asm-9.7.1.jar;target\classes org.objectweb.asm.util.ASMifier %ARG1%
