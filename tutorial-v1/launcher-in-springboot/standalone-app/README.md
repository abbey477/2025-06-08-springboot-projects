# Spring Boot Standalone Application

A comprehensive Spring Boot 3.5.0 application template for standalone, non-web applications with Java 21.

## Features

- ✅ **Non-web application** - Lightweight, no web server
- ✅ **Scheduled tasks** - Background processing with configurable intervals
- ✅ **Multiple execution modes** - Batch, continuous, debug modes
- ✅ **Process monitoring** - PID tracking, thread information, system metrics
- ✅ **Professional logging** - SLF4J with Logback, file rotation, async logging
- ✅ **Configuration management** - YAML configuration, profiles, validation
- ✅ **Shell scripts** - Easy deployment and management scripts
- ✅ **Multiple launchers** - PropertiesLauncher support for custom classpath

## Quick Start

### Prerequisites
- Java 21 or higher
- Maven 3.6 or higher

### Build and Run
```bash
# Clone and build
git clone <repository-url>
cd standalone-app

# Build and run (using Makefile)
make run

# Or build and run manually
mvn clean package
cd scripts
./start-app.sh