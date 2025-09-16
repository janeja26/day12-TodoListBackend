
package org.example.todolistbackend.repository;


import org.example.todolistbackend.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    // 列表直接用 findAll()
}
