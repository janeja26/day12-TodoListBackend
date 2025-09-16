package org.example.todolistbackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TodoCreateRequest {

    @NotBlank(message = "text must not be blank")
    private String text;

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}

