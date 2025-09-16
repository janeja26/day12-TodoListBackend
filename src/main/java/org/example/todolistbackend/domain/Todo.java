package org.example.todolistbackend.domain;


import jakarta.persistence.*;

@Entity
@Table(name = "todos")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=255)
    private String text;

    @Column(nullable=false)
    private boolean done = false;

    protected Todo() {}

    public Todo(String text, boolean done) {
        this.text = text;
        this.done = done;
    }

    public Long getId() { return id; }
    public String getText() { return text; }
    public boolean isDone() { return done; }

    public void setText(String text) { this.text = text; }
    public void setDone(boolean done) { this.done = done; }
}

