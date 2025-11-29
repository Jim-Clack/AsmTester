#!/bin/bash
# Linux/Apple Shell File for running ASMifier
# usage:
#   asmifier <package>.<class>
# Before you can run this...
# Set the current directory to the project directory
# Make the shell script executable, like this:
#  chmod +r asmifier.sh
ARG1=$1
if [ "$ARG1" = "" ]; then
  ARG1="java.lang.String"
fi
echo Dumping $ARG1
java --class-path ~/.m2/repository/org/ow2/asm/asm-util/9.7.1/asm-util-9.7.1.jar;~/.m2/repository/org/ow2/asm/asm/9.7.1/asm-9.7.1.jar:./target/classes org.objectweb.asm.util.ASMifier $ARG1