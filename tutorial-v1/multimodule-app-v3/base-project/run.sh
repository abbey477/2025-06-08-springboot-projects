#!/bin/bash

echo "🚀 Multi-Module Maven Project Demo"
echo "=================================="

# Function to print colored output
print_success() {
    echo -e "\033[1;32m✅ $1\033[0m"
}

print_info() {
    echo -e "\033[1;34mℹ️  $1\033[0m"
}

print_command() {
    echo -e "\033[1;33m$ $1\033[0m"
}

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven first."
    exit 1
fi

print_info "Cleaning and compiling project..."
print_command "mvn clean compile"
mvn clean compile

if [ $? -eq 0 ]; then
    print_success "Build successful!"
else
    echo "❌ Build failed!"
    exit 1
fi

print_info "Running tests..."
print_command "mvn test"
mvn test

if [ $? -eq 0 ]; then
    print_success "All tests passed!"
else
    echo "❌ Tests failed!"
    exit 1
fi

print_info "Running the application..."
print_command "mvn exec:java -pl web-app"
mvn exec:java -pl web-app

print_success "Demo completed!"
echo
echo "📚 Try these commands:"
echo "  mvn dependency:tree              # View dependency tree"
echo "  mvn test -pl common-lib          # Test specific module"
echo "  mvn help:effective-pom           # View effective POM"
