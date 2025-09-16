package org.example.todolistbackend.service;



import org.example.todolistbackend.domain.Todo;
import org.example.todolistbackend.dto.TodoResponse;
import org.example.todolistbackend.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository repo;

    public TodoServiceImpl(TodoRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<TodoResponse> listAll() {
        List<Todo> todos = repo.findAll();
        return todos.stream()
                .map(t -> new TodoResponse(t.getId(), t.getText(), t.isDone()))
                .toList();
    }
}
