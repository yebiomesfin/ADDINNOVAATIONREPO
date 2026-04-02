package com.example.todos.controller;

import com.example.todos.model.Todo;
import com.example.todos.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Test
    void getAll_withNoFilter_returnsAllTodos() throws Exception {
        Todo t1 = new Todo("Write tests", true);
        Todo t2 = new Todo("Fix bugs", false);
        when(todoService.findAll(Optional.empty())).thenReturn(List.of(t1, t2));

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(todoService).findAll(Optional.empty());
    }

    @Test
    void getAll_withCompletedTrue_returnsOnlyCompletedTodos() throws Exception {
        Todo completed = new Todo("Write tests", true);
        when(todoService.findAll(Optional.of(true))).thenReturn(List.of(completed));

        mockMvc.perform(get("/api/todos").param("completed", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].completed").value(true));

        verify(todoService).findAll(Optional.of(true));
    }

    @Test
    void getAll_withCompletedFalse_returnsOnlyPendingTodos() throws Exception {
        Todo pending = new Todo("Fix bugs", false);
        when(todoService.findAll(Optional.of(false))).thenReturn(List.of(pending));

        mockMvc.perform(get("/api/todos").param("completed", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].completed").value(false));

        verify(todoService).findAll(Optional.of(false));
    }

    @Test
    void getAll_withInvalidCompletedParam_returns400() throws Exception {
        mockMvc.perform(get("/api/todos").param("completed", "abc"))
                .andExpect(status().isBadRequest());
    }
}
