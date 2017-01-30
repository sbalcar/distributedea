#!/bin/bash
# Skript na spusteni DistributedEA Master
# Eclipse - file -> export -> Runnable JAR file
#java -cp distributedea.jar jade.Boot -jade_domain_df_autocleanup true -agents Initiator:org.distributedea.agents.systemagents.Agent_Initiator

# spusteni pomoci ANTu
# File -> export -> general -> Ant Buildfiles
# >ant distributedea
ant distributedeanogui

## V Rotunde
# nice -n 19 java -cp .....
## vypocet spustit s prefixem nice (pak)
# ulimit -t unlimited <PID>
## zadat prikaz ulimited s argumentem PIDu nice-procesu
# kinit -R
## spustit prikaz kinit bez argumentu - obnovi AFS tickety
