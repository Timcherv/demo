package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

// Стандартная модель ответа API
class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String date;
    private String dayOfWeek;

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        LocalDateTime now = LocalDateTime.now();
        this.date = now.toString();
        this.dayOfWeek = now.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public String getDate() {
        return date;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }
}

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Task>>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        ApiResponse<List<Task>> response = new ApiResponse<>(true, "All tasks fetched successfully", tasks);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/completed")
    public ResponseEntity<ApiResponse<List<Task>>> getCompletedTasks() {
        List<Task> tasks = taskService.getTasksByCompletion(true);
        ApiResponse<List<Task>> response = new ApiResponse<>(true, "Completed tasks fetched successfully", tasks);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<Task>>> getPendingTasks() {
        List<Task> tasks = taskService.getTasksByCompletion(false);
        ApiResponse<List<Task>> response = new ApiResponse<>(true, "Pending tasks fetched successfully", tasks);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Task>>> searchTasks(@RequestParam String title) {
        List<Task> tasks = taskService.searchTasksByTitle(title);
        ApiResponse<List<Task>> response = new ApiResponse<>(true, "Tasks found by title", tasks);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Task>> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        if (task.isPresent()) {
            ApiResponse<Task> response = new ApiResponse<>(true, "Task found", task.get());
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<Task> response = new ApiResponse<>(false, "Task not found", null);
            return ResponseEntity.status(404).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Task>> createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        ApiResponse<Task> response = new ApiResponse<>(true, "Task created successfully", createdTask);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Task>> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        Optional<Task> updatedTask = taskService.updateTask(id, taskDetails);
        if (updatedTask.isPresent()) {
            ApiResponse<Task> response = new ApiResponse<>(true, "Task updated successfully", updatedTask.get());
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<Task> response = new ApiResponse<>(false, "Task not found with id " + id, null);
            return ResponseEntity.status(404).body(response);
        }
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<Task>> toggleTaskCompletion(@PathVariable Long id) {
        Optional<Task> toggledTask = taskService.toggleTaskCompletion(id);
        if (toggledTask.isPresent()) {
            ApiResponse<Task> response = new ApiResponse<>(true, "Task completion toggled", toggledTask.get());
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<Task> response = new ApiResponse<>(false, "Task not found", null);
            return ResponseEntity.status(404).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long id) {
        boolean deleted = taskService.deleteTask(id);
        if (deleted) {
            ApiResponse<Void> response = new ApiResponse<>(true, "Task deleted successfully", null);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<Void> response = new ApiResponse<>(false, "Task not found", null);
            return ResponseEntity.status(404).body(response);
        }
    }
}