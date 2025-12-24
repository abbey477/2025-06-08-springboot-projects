#!/bin/bash

# Spring Boot Standalone Application Launcher Script
# Usage: ./start-app.sh [options]

set -e

# Configuration
APP_NAME="standalone-app"
JAR_FILE="../target/${APP_NAME}-1.0.0.jar"
PID_FILE="../${APP_NAME}.pid"
LOG_DIR="../logs"
JAVA_OPTS="-Xms256m -Xmx1024m -XX:+UseG1GC"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Functions
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

stop_application() {
    if [ ! -f "$PID_FILE" ]; then
        log_warn "PID file not found. Application may not be running."
        return 1
    fi

    PID=$(cat "$PID_FILE")

    if ! ps -p "$PID" > /dev/null 2>&1; then
        log_warn "Process with PID $PID is not running."
        rm -f "$PID_FILE"
        return 1
    fi

    log_info "Stopping $APP_NAME with PID: $PID"

    # Try graceful shutdown first
    kill -TERM "$PID"

    # Wait for graceful shutdown
    local count=0
    while ps -p "$PID" > /dev/null 2>&1 && [ $count -lt 30 ]; do
        sleep 1
        count=$((count + 1))
        echo -n "."
    done
    echo ""

    # Check if process is still running
    if ps -p "$PID" > /dev/null 2>&1; then
        log_warn "Graceful shutdown failed. Forcing shutdown..."
        kill -KILL "$PID"
        sleep 2
    fi

    # Verify shutdown
    if ps -p "$PID" > /dev/null 2>&1; then
        log_error "Failed to stop application with PID: $PID"
        return 1
    else
        log_info "Application stopped successfully"
        rm -f "$PID_FILE"
        return 0
    fi
}

# Main execution
main() {
    log_info "=== Spring Boot Standalone Application Stop Script ==="
    stop_application
}

main "$@"
    echo -e "${RED}[ERROR]${NC} $1"
}

check_java() {
    if ! command -v java &> /dev/null; then
        log_error "Java is not installed or not in PATH"
        exit 1
    fi

    JAVA_VERSION=$(java -version 2>&1 | head -n1 | cut -d'"' -f2)
    log_info "Using Java version: $JAVA_VERSION"
}

check_jar() {
    if [ ! -f "$JAR_FILE" ]; then
        log_error "JAR file not found: $JAR_FILE"
        log_info "Please run 'mvn clean package' first"
        exit 1
    fi
}

create_directories() {
    mkdir -p "$LOG_DIR"
    log_info "Created log directory: $LOG_DIR"
}

check_if_running() {
    if [ -f "$PID_FILE" ]; then
        PID=$(cat "$PID_FILE")
        if ps -p "$PID" > /dev/null 2>&1; then
            log_warn "Application is already running with PID: $PID"
            return 0
        else
            log_warn "Stale PID file found. Removing..."
            rm -f "$PID_FILE"
        fi
    fi
    return 1
}

start_application() {
    local profile=""
    local extra_args=""

    # Parse arguments
    while [[ $# -gt 0 ]]; do
        case $1 in
            --profile=*)
                profile="${1#*=}"
                shift
                ;;
            --run-once)
                extra_args="$extra_args --run-once"
                shift
                ;;
            --show-threads)
                extra_args="$extra_args --show-threads"
                shift
                ;;
            --debug)
                extra_args="$extra_args --debug"
                JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
                shift
                ;;
            *)
                extra_args="$extra_args $1"
                shift
                ;;
        esac
    done

    # Set profile
    if [ -n "$profile" ]; then
        JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=$profile"
        log_info "Using profile: $profile"
    fi

    log_info "Starting $APP_NAME..."
    log_info "JAR file: $JAR_FILE"
    log_info "Java options: $JAVA_OPTS"
    log_info "Extra arguments: $extra_args"

    # Start application
    nohup java $JAVA_OPTS -jar "$JAR_FILE" $extra_args > "$LOG_DIR/console.log" 2>&1 &

    # Save PID
    echo $! > "$PID_FILE"
    PID=$(cat "$PID_FILE")

    log_info "Application started with PID: $PID"
    log_info "Console output: $LOG_DIR/console.log"
    log_info "Application logs: $LOG_DIR/standalone-app.log"

    # Wait a moment and check if it's still running
    sleep 3
    if ps -p "$PID" > /dev/null 2>&1; then
        log_info "Application is running successfully!"
    else
        log_error "Application failed to start. Check logs for details."
        rm -f "$PID_FILE"
        exit 1
    fi
}

show_usage() {
    echo "Usage: $0 [options]"
    echo ""
    echo "Options:"
    echo "  --profile=PROFILE    Set Spring profile (dev, prod)"
    echo "  --run-once          Run once and exit"
    echo "  --show-threads      Show thread information"
    echo "  --debug             Enable debug mode with remote debugging"
    echo ""
    echo "Examples:"
    echo "  $0                          # Start with default profile"
    echo "  $0 --profile=dev            # Start with dev profile"
    echo "  $0 --run-once               # Run once and exit"
    echo "  $0 --debug --profile=dev    # Start in debug mode with dev profile"
}

# Main execution
main() {
    log_info "=== Spring Boot Standalone Application Launcher ==="

    if [[ $# -eq 1 && ($1 == "-h" || $1 == "--help") ]]; then
        show_usage
        exit 0
    fi

    check_java
    check_jar
    create_directories

    if check_if_running; then
        exit 1
    fi

    start_application "$@"
}

# Run main function with all arguments
main "$@"