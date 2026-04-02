package com.example.todos.controller;

import com.example.todos.model.Todo;
import com.example.todos.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String completed) {

        if (completed != null && !completed.equalsIgnoreCase("true")
                && !completed.equalsIgnoreCase("false")) {
            return ResponseEntity.badRequest()
                    .body("Invalid value for 'completed' parameter. Use 'true' or 'false'.");
        }

        Optional<Boolean> filter = completed != null
                ? Optional.of(Boolean.parseBoolean(completed))
                : Optional.empty();

        List<Todo> todos = todoService.findAll(filter);
        return ResponseEntity.ok(todos);
    }
}
