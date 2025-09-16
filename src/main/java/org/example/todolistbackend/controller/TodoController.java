
package org.example.todolistbackend.controller;



import org.example.todolistbackend.dto.TodoResponse;
import org.example.todolistbackend.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TodoResponse>> listTodos() {
        return ResponseEntity.ok(service.listAll());
    }
}
