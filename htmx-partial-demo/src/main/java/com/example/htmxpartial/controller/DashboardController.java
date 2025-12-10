package com.example.htmxpartial.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class DashboardController {

    private final AtomicInteger notificationCount = new AtomicInteger(5);
    private final List<String> messages = new ArrayList<>(Arrays.asList(
        "Welcome to the dashboard!",
        "System check completed",
        "All services running normally"
    ));
    private final List<Task> tasks = new ArrayList<>(Arrays.asList(
        new Task(1, "Complete project documentation", false),
        new Task(2, "Review pull requests", false),
        new Task(3, "Update dependencies", true)
    ));
    private int taskIdCounter = 4;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("notificationCount", notificationCount.get());
        model.addAttribute("messages", messages);
        model.addAttribute("tasks", tasks);
        model.addAttribute("currentTime", getCurrentTime());
        return "dashboard";
    }

    @PostMapping("/refresh-all")
    public String refreshAll(Model model,
                           @RequestHeader(value = "HX-Request", required = false) String hxRequest) {
        
        // Simulate some updates
        notificationCount.incrementAndGet();
        messages.add("Dashboard refreshed at " + getCurrentTime());
        
        model.addAttribute("notificationCount", notificationCount.get());
        model.addAttribute("messages", messages);
        model.addAttribute("tasks", tasks);
        model.addAttribute("currentTime", getCurrentTime());
        
        // If this is an htmx request, return the partial updates
        if ("true".equals(hxRequest)) {
            return "fragments/multiple-updates :: partials";
        }
        
        // Otherwise, return the full page
        return "dashboard";
    }

    @PostMapping("/add-task")
    public String addTask(@RequestParam String taskName, Model model,
                         @RequestHeader(value = "HX-Request", required = false) String hxRequest) {
        
        Task newTask = new Task(taskIdCounter++, taskName, false);
        tasks.add(newTask);
        notificationCount.incrementAndGet();
        messages.add("New task added: " + taskName);
        
        model.addAttribute("tasks", tasks);
        model.addAttribute("notificationCount", notificationCount.get());
        model.addAttribute("messages", messages);
        
        if ("true".equals(hxRequest)) {
            return "fragments/task-updates :: task-partials";
        }
        
        return "redirect:/";
    }

    @PostMapping("/toggle-task/{id}")
    public String toggleTask(@PathVariable int id, Model model,
                           @RequestHeader(value = "HX-Request", required = false) String hxRequest) {
        
        tasks.stream()
            .filter(t -> t.getId() == id)
            .findFirst()
            .ifPresent(task -> {
                task.setCompleted(!task.isCompleted());
                String status = task.isCompleted() ? "completed" : "uncompleted";
                messages.add("Task '" + task.getName() + "' marked as " + status);
            });
        
        notificationCount.incrementAndGet();
        
        model.addAttribute("tasks", tasks);
        model.addAttribute("notificationCount", notificationCount.get());
        model.addAttribute("messages", messages);
        
        if ("true".equals(hxRequest)) {
            return "fragments/task-toggle :: toggle-partials";
        }
        
        return "redirect:/";
    }

    @GetMapping("/notifications")
    public String getNotifications(Model model) {
        model.addAttribute("notificationCount", notificationCount.get());
        return "fragments/notification :: notification-badge";
    }

    @PostMapping("/clear-notifications")
    public String clearNotifications(Model model,
                                   @RequestHeader(value = "HX-Request", required = false) String hxRequest) {
        notificationCount.set(0);
        messages.add("Notifications cleared");
        
        model.addAttribute("notificationCount", 0);
        model.addAttribute("messages", messages);
        
        if ("true".equals(hxRequest)) {
            return "fragments/clear-notifications :: clear-partials";
        }
        
        return "redirect:/";
    }

    private String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    // Inner class for Task
    public static class Task {
        private int id;
        private String name;
        private boolean completed;

        public Task(int id, String name, boolean completed) {
            this.id = id;
            this.name = name;
            this.completed = completed;
        }

        // Getters and setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public boolean isCompleted() { return completed; }
        public void setCompleted(boolean completed) { this.completed = completed; }
    }
}
