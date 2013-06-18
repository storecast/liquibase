#!/bin/bash
mvn -B -DskipTests release:prepare release:perform -DtagNameFormat="liquibase-@{project.version}" -DautoVersionSubmodules=true -DpreparationGoals="clean install" -Darguments="-DskipTests" -DuseReleaseProfile=false -Dgoals="clean deploy"
