package org.example.todolistbackend.service;

import org.example.todolistbackend.domain.Todo;
import org.example.todolistbackend.dto.TodoCreateRequest;
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
        return repo.findAll().stream()
                .map(t -> new TodoResponse(t.getId(), t.getText(), t.isDone()))
                .toList();
    }

    @Override
    public TodoResponse create(TodoCreateRequest req) {
        // Bean Validation 已保证 req.getText() 非空非空白
        String text = req.getText().trim();
        // AC 要求：创建的 done 固定为 false（即使客户端传了 done 也忽略）
        Todo saved = repo.save(new Todo(text, false));
        return new TodoResponse(saved.getId(), saved.getText(), saved.isDone());
    }
}
