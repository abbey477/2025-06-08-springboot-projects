# htmx 4.0 hx-partial Demo with Spring Boot & Thymeleaf

This is a demonstration application showing how to use the new `<hx-partial>` element in htmx 4.0 with Spring Boot and Thymeleaf.

## What is hx-partial?

In htmx 4.0, `<hx-partial>` is a new element that provides a cleaner way to handle multiple content updates in a single response. Instead of the complex out-of-band swap syntax from htmx 2.x, partials use regular htmx attributes you already know.

## Features Demonstrated

- **Multiple Partial Updates**: Single request updates multiple page sections
- **Targeted Updates**: Each partial specifies its own target and swap strategy
- **Different Swap Modes**: Examples of `innerHTML`, `beforeend`, and other swap strategies
- **Task Management**: Interactive todo list with real-time updates
- **Notification System**: Badge updates with counter
- **Activity Log**: Message feed with append operations

## Project Structure

```
htmx-partial-demo/
├── src/main/java/com/example/htmxpartial/
│   ├── HtmxPartialDemoApplication.java
│   └── controller/
│       └── DashboardController.java
├── src/main/resources/
│   ├── application.properties
│   └── templates/
│       ├── dashboard.html
│       └── fragments/
│           ├── multiple-updates.html
│           ├── task-updates.html
│           ├── task-toggle.html
│           ├── clear-notifications.html
│           └── notification.html
└── pom.xml
```

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## How to Run

1. **Clone or download the project files**

2. **Navigate to the project directory**:
   ```bash
   cd htmx-partial-demo
   ```

3. **Build the project**:
   ```bash
   mvn clean install
   ```

4. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

5. **Open your browser** and navigate to:
   ```
   http://localhost:8080
   ```

## How hx-partial Works

### Server Response Format

When the server receives an htmx request, it returns multiple `<hx-partial>` elements:

```html
<hx-partial hx-target="#notification-badge" hx-swap="innerHTML">
    <!-- New notification badge content -->
</hx-partial>

<hx-partial hx-target="#messages" hx-swap="beforeend">
    <!-- New message to append -->
</hx-partial>

<hx-partial hx-target="#task-list" hx-swap="innerHTML">
    <!-- Updated task list -->
</hx-partial>
```

### Key Differences from htmx 2.x

**htmx 2.x (out-of-band swaps):**
```html
<div id="notification" hx-swap-oob="beforeend:#notifications">
    Content
</div>
```

**htmx 4.0 (hx-partial):**
```html
<hx-partial hx-target="#notifications" hx-swap="beforeend">
    <div>Content</div>
</hx-partial>
```

## Testing the Features

1. **Add a Task**: Enter a task name and click "Add Task" to see multiple partials update the task list, notifications, and activity log.

2. **Toggle Task Completion**: Check/uncheck tasks to see coordinated updates across components.

3. **Refresh All**: Click the "Refresh All" button to trigger multiple partial updates simultaneously.

4. **Clear Notifications**: Reset the notification counter and see the badge update along with a confirmation message.

## Observing the Partials

Open your browser's Developer Tools and watch the Network tab. When you interact with the application, you'll see responses containing multiple `<hx-partial>` elements, each targeting different parts of the page.

## Important Notes

- htmx 4.0 is currently in alpha (4.0.0-alpha4 as of this demo)
- The `<hx-partial>` element is a template element under the hood
- Partials provide better locality of behavior compared to out-of-band swaps
- Each partial can have its own swap strategy and target

## Customization

You can modify the controller and templates to experiment with different partial update patterns:

- Try different `hx-swap` strategies (`outerHTML`, `afterbegin`, `beforebegin`, etc.)
- Add more complex partial chains
- Implement conditional partials based on server logic
- Combine with htmx 4.0's streaming capabilities

## Troubleshooting

If the application doesn't start:
- Ensure Java 17+ is installed: `java -version`
- Check Maven is installed: `mvn -version`
- Verify port 8080 is not in use
- Check the console for any Spring Boot startup errors

## Resources

- [htmx 4.0 Documentation](https://four.htmx.org/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Thymeleaf Documentation](https://www.thymeleaf.org/)
