#!/bin/bash
set -ex

# Ensure Java is available
if ! command -v java &> /dev/null; then
  echo "Installing OpenJDK 17..."
  sudo apt-get update -qq
  sudo apt-get install -y -qq openjdk-17-jdk-headless
  export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
  export PATH="$JAVA_HOME/bin:$PATH"
fi

echo "Java version:"
java -version 2>&1

# Ensure gradlew is executable
chmod +x ./gradlew

# Run the Kotlin/Wasm build
./gradlew wasmJsBrowserDistribution --no-daemon --stacktrace
