package com.example.todos.service;

import com.example.todos.model.Todo;
import com.example.todos.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    private Todo completedTodo;
    private Todo pendingTodo;

    @BeforeEach
    void setUp() {
        completedTodo = new Todo("Write tests", true);
        pendingTodo = new Todo("Fix bugs", false);
    }

    @Test
    void findAll_withNoFilter_returnsAllTodos() {
        when(todoRepository.findAll()).thenReturn(List.of(completedTodo, pendingTodo));

        List<Todo> result = todoService.findAll(Optional.empty());

        assertThat(result).containsExactly(completedTodo, pendingTodo);
        verify(todoRepository).findAll();
        verify(todoRepository, never()).findByCompleted(anyBoolean());
    }

    @Test
    void findAll_withCompletedTrue_returnsOnlyCompletedTodos() {
        when(todoRepository.findByCompleted(true)).thenReturn(List.of(completedTodo));

        List<Todo> result = todoService.findAll(Optional.of(true));

        assertThat(result).containsExactly(completedTodo);
        assertThat(result).allMatch(Todo::isCompleted);
        verify(todoRepository).findByCompleted(true);
        verify(todoRepository, never()).findAll();
    }

    @Test
    void findAll_withCompletedFalse_returnsOnlyPendingTodos() {
        when(todoRepository.findByCompleted(false)).thenReturn(List.of(pendingTodo));

        List<Todo> result = todoService.findAll(Optional.of(false));

        assertThat(result).containsExactly(pendingTodo);
        assertThat(result).noneMatch(Todo::isCompleted);
        verify(todoRepository).findByCompleted(false);
        verify(todoRepository, never()).findAll();
    }
}
