package org.example.todolistbackend.dto;



public class TodoResponse {
    private Long id;
    private String text;
    private boolean done;

    public TodoResponse(Long id, String text, boolean done) {
        this.id = id;
        this.text = text;
        this.done = done;
    }

    public Long getId() { return id; }
    public String getText() { return text; }
    public boolean isDone() { return done; }
}
