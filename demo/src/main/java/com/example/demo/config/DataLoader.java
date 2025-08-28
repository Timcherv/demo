package com.example.demo.config;

import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public void run(String... args) throws Exception {
        // Очищаем существующие данные (опционально)
        taskRepository.deleteAll();
        
        // Добавляем тестовые задачи
        taskRepository.save(new Task("Задача 1", "Описание к задаче"));
        taskRepository.save(new Task("Задача 2", "Описание к задаче"));
        taskRepository.save(new Task("Задача 3", "Соотствественно, описание к задаче"));
        
        System.out.println("Тестовые данные добавлены в базу!");
    }
}