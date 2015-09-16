#!/bin/bash
# Skript na spusteni DistributedEA Master
# Eclipse - file -> export -> Runnable JAR file
java -cp distributedea.jar jade.Boot -agents Initiator:org.distributedea.agents.systemagents.Agent_Initiator
