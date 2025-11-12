#!/bin/bash
cd /home/kavia/workspace/code-generation/live-sports-scores-android-app-41532-41544/android_tv_frontend
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

