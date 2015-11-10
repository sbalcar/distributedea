#!/bin/bash
# Skript na spusteni DistributedEA Master
# Eclipse - file -> export -> Runnable JAR file
java -cp distributedea.jar jade.Boot -jade_domain_df_autocleanup true -agents Initiator:org.distributedea.agents.systemagents.Agent_Initiator
