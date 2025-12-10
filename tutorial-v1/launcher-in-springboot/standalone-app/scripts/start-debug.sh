#!/bin/bash

# Debug Mode Launcher
# Starts application with remote debugging enabled

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "Starting application in debug mode..."
echo "Remote debugging will be available on port 5005"
"$SCRIPT_DIR/start-app.sh" --debug --profile=dev --show-threads "$@"