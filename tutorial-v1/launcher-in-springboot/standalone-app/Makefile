## Makefile
```makefile
# Spring Boot Standalone Application Makefile

.PHONY: help clean compile package run run-dev run-prod run-batch run-debug stop status test

# Default goal
.DEFAULT_GOAL := help

# Variables
APP_NAME = standalone-app
VERSION = 1.0.0
JAR_FILE = target/$(APP_NAME)-$(VERSION).jar
SCRIPTS_DIR = scripts

help: ## Show this help message
	@echo "Spring Boot Standalone Application"
	@echo "=================================="
	@echo ""
	@echo "Available targets:"
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "  %-15s %s\n", $1, $2}' $(MAKEFILE_LIST)

clean: ## Clean build artifacts
	@echo "Cleaning build artifacts..."
	mvn clean
	rm -rf logs/
	rm -f $(APP_NAME).pid

compile: ## Compile the application
	@echo "Compiling application..."
	mvn compile

test: ## Run tests
	@echo "Running tests..."
	mvn test

package: clean ## Package the application
	@echo "Packaging application..."
	mvn clean package -DskipTests
	@echo "JAR file created: $(JAR_FILE)"

run: package ## Build and run application (default profile)
	@echo "Starting application..."
	cd $(SCRIPTS_DIR) && ./start-app.sh

run-dev: package ## Build and run application (dev profile)
	@echo "Starting application in development mode..."
	cd $(SCRIPTS_DIR) && ./start-app.sh --profile=dev

run-prod: package ## Build and run application (prod profile)
	@echo "Starting application in production mode..."
	cd $(SCRIPTS_DIR) && ./start-app.sh --profile=prod

run-batch: package ## Run application in batch mode (once and exit)
	@echo "Running application in batch mode..."
	cd $(SCRIPTS_DIR) && ./start-batch.sh

run-debug: package ## Run application in debug mode
	@echo "Starting application in debug mode..."
	cd $(SCRIPTS_DIR) && ./start-debug.sh

stop: ## Stop the running application
	@echo "Stopping application..."
	cd $(SCRIPTS_DIR) && ./stop-app.sh

status: ## Check application status
	@if [ -f "$(APP_NAME).pid" ]; then \
		PID=$(cat $(APP_NAME).pid); \
		if ps -p $PID > /dev/null 2>&1; then \
			echo "Application is running with PID: $PID"; \
		else \
			echo "Application is not running (stale PID file)"; \
		fi \
	else \
		echo "Application is not running"; \
	fi

logs: ## Show application logs
	@if [ -f "logs/standalone-app.log" ]; then \
		tail -f logs/standalone-app.log; \
	else \
		echo "Log file not found"; \
	fi

install: package ## Install the application (copy to /opt)
	@echo "Installing application to /opt/$(APP_NAME)..."
	sudo mkdir -p /opt/$(APP_NAME)
	sudo cp $(JAR_FILE) /opt/$(APP_NAME)/
	sudo cp -r $(SCRIPTS_DIR) /opt/$(APP_NAME)/
	sudo cp src/main/resources/application.yml /opt/$(APP_NAME)/
	@echo "Installation completed"