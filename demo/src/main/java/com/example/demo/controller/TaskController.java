package com.example.demo.controller;

import com.example.demo.model.Task;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    
    @Autowired
    private TaskService taskService;
    
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }
    
    @GetMapping("/completed")
    public List<Task> getCompletedTasks() {
        return taskService.getTasksByCompletion(true);
    }
    
    @GetMapping("/pending")
    public List<Task> getPendingTasks() {
        return taskService.getTasksByCompletion(false);
    }
    
    @GetMapping("/search")
    public List<Task> searchTasks(@RequestParam String title) {
        return taskService.searchTasksByTitle(title);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        Optional<Task> updatedTask = taskService.updateTask(id, taskDetails);
        return updatedTask.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }
    
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Task> toggleTaskCompletion(@PathVariable Long id) {
        Optional<Task> toggledTask = taskService.toggleTaskCompletion(id);
        return toggledTask.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        boolean deleted = taskService.deleteTask(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}