
package org.example.todolistbackend.service;



import org.example.todolistbackend.dto.TodoResponse;

import java.util.List;

public interface TodoService {
    List<TodoResponse> listAll();
}
