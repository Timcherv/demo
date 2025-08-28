package com.example.demo.service;

import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;
    
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    
    public List<Task> getTasksByCompletion(boolean completed) {
        return taskRepository.findByCompleted(completed);
    }
    
    public List<Task> searchTasksByTitle(String title) {
        return taskRepository.findByTitleContainingIgnoreCase(title);
    }
    
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }
    
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }
    
    public Optional<Task> updateTask(Long id, Task taskDetails) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(taskDetails.getTitle());
                    task.setDescription(taskDetails.getDescription());
                    task.setCompleted(taskDetails.isCompleted());
                    return taskRepository.save(task);
                });
    }
    
    public Optional<Task> toggleTaskCompletion(Long id) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setCompleted(!task.isCompleted());
                    return taskRepository.save(task);
                });
    }
    
    public boolean deleteTask(Long id) {
        return taskRepository.findById(id)
                .map(task -> {
                    taskRepository.delete(task);
                    return true;
                }).orElse(false);
    }
}