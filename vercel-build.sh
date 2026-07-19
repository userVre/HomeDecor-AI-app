#!/bin/bash
set -e

# Install Java 17 if not present (Vercel build env)
if ! command -v java &> /dev/null; then
  echo "Java not found. Installing OpenJDK 17..."
  apt-get update -qq
  apt-get install -y -qq openjdk-17-jdk-headless > /dev/null 2>&1
  export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
  export PATH=$JAVA_HOME/bin:$PATH
fi

echo "Java version:"
java -version 2>&1

# Make gradlew executable
chmod +x ./gradlew

# Run the build
echo "Starting Gradle build..."
./gradlew wasmJsBrowserDistribution --no-daemon --stacktrace
