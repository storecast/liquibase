#!/bin/bash
mvn -B -DaltDeploymentRepository='internal.repo::default::file://${project.build.directory}/mvn-repo' -DskipTests release:prepare release:perform -DtagNameFormat="liquibase-@{project.version}" -DautoVersionSubmodules=true -DpreparationGoals="clean install" -Darguments="-DskipTests" -DuseReleaseProfile=false -Dgoals="clean deploy"
