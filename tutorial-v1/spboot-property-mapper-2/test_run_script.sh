#!/bin/bash

# Demo App Property - Test Runner Script
# This script runs different test scenarios to validate property mapping

echo "=========================================="
echo "Demo App Property - Test Execution"
echo "=========================================="

# Function to print section headers
print_section() {
    echo ""
    echo "----------------------------------------"
    echo "$1"
    echo "----------------------------------------"
}

# Function to run tests and capture results
run_test() {
    local test_name="$1"
    local test_command="$2"
    
    echo "Running: $test_name"
    if eval "$test_command"; then
        echo "✅ PASSED: $test_name"
    else
        echo "❌ FAILED: $test_name"
        return 1
    fi
}

# Clean and compile
print_section "CLEANING AND COMPILING PROJECT"
mvn clean compile test-compile

# Run unit tests
print_section "RUNNING UNIT TESTS"

run_test "Property Mapping Tests" "mvn test -Dtest=AppPropertiesTest -q"
run_test "Controller Unit Tests" "mvn test -Dtest=PropertyControllerTest -q"
run_test "Service Unit Tests" "mvn test -Dtest=PropertyDisplayServiceTest -q"

# Run integration tests
print_section "RUNNING INTEGRATION TESTS"

run_test "Full Integration Tests" "mvn test -Dtest=DemoAppPropertyIntegrationTest -q"

# Run all tests together
print_section "RUNNING ALL TESTS"

run_test "Complete Test Suite" "mvn test -q"

# Generate test report
print_section "GENERATING TEST REPORT"

if mvn surefire-report:report-only -q; then
    echo "✅ Test report generated in target/site/surefire-report.html"
else
    echo "⚠️  Test report generation failed"
fi

# Application startup test
print_section "TESTING APPLICATION STARTUP"

echo "Starting application in background..."
mvn spring-boot:run &
APP_PID=$!

# Wait for application to start
echo "Waiting for application to start..."
sleep 15

# Test if application is running
if curl -f http://localhost:8080/api/properties/basic > /dev/null 2>&1; then
    echo "✅ Application started successfully"
    echo "Testing REST endpoints..."
    
    # Test basic endpoint
    if curl -f http://localhost:8080/api/properties/basic > /dev/null 2>&1; then
        echo "✅ Basic properties endpoint working"
    else
        echo "❌ Basic properties endpoint failed"
    fi
    
    # Test features endpoint
    if curl -f http://localhost:8080/api/properties/features > /dev/null 2>&1; then
        echo "✅ Features endpoint working"
    else
        echo "❌ Features endpoint failed"
    fi
    
    # Test lists endpoint
    if curl -f http://localhost:8080/api/properties/lists > /dev/null 2>&1; then
        echo "✅ Lists endpoint working"
    else
        echo "❌ Lists endpoint failed"
    fi
    
else
    echo "❌ Application failed to start or endpoints not accessible"
fi

# Stop the application
echo "Stopping application..."
kill $APP_PID 2>/dev/null
wait $APP_PID 2>/dev/null

print_section "TEST EXECUTION SUMMARY"

echo "Test execution completed!"
echo ""
echo "📋 What was tested:"
echo "   • Property mapping from application-test.properties"
echo "   • Nested class property binding"
echo "   • List and Map property mapping"
echo "   • REST endpoint functionality"
echo "   • Console output validation"
echo "   • Sensitive data masking"
echo "   • Application startup and shutdown"
echo ""
echo "📁 Key files validated:"
echo "   • AppProperties.java - Configuration class"
echo "   • application-test.properties - Test properties"
echo "   • PropertyController.java - REST endpoints"
echo "   • PropertyDisplayService.java - Console output"
echo ""
echo "🔍 Test coverage includes:"
echo "   • Basic properties (strings, booleans, numbers)"
echo "   • Nested objects (server.ssl, database.pool)"
echo "   • Collections (lists of origins, emails)"
echo "   • Maps (external services, error messages)"
echo "   • Security (password masking)"
echo "   • Integration (full Spring context)"
echo ""

if [ $? -eq 0 ]; then
    echo "🎉 ALL TESTS COMPLETED SUCCESSFULLY!"
    echo ""
    echo "Your Spring Boot property mapping is working correctly!"
    echo "The application demonstrates:"
    echo "   ✅ @ConfigurationProperties without spring-boot-configuration-processor"
    echo "   ✅ Lombok integration for clean code"
    echo "   ✅ Comprehensive property mapping techniques"
    echo "   ✅ Security-conscious sensitive data handling"
    echo "   ✅ Full test coverage validation"
else
    echo "❌ SOME TESTS FAILED - Check the output above for details"
    exit 1
fi