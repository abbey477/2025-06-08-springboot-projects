#!/bin/bash

# Batch Processing Mode Launcher
# Runs the application once and exits

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "Starting application in batch mode..."
"$SCRIPT_DIR/start-app.sh" --run-once --profile=prod "$@"