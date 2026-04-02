package com.example.todos.service;

import com.example.todos.model.Todo;
import com.example.todos.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> findAll(Optional<Boolean> completed) {
        return completed
                .map(todoRepository::findByCompleted)
                .orElseGet(todoRepository::findAll);
    }
}
