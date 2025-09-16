package org.example.todolistbackend;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TodoCreateApiIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired JdbcTemplate jdbc;
    @Autowired ObjectMapper om;

    @BeforeEach
    void setup() {
        jdbc.execute("DELETE FROM todos");
    }

    @Test
    void createTodo_success() throws Exception {
        String body = """
          {"text":"Buy milk"}
        """;

        var result = mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.matchesPattern("^/todos/\\d+$")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.text").value("Buy milk"))
                .andExpect(jsonPath("$.done").value(false))
                .andReturn();

        // 校验：已持久化
        String resp = result.getResponse().getContentAsString();
        JsonNode node = om.readTree(resp);
        long id = node.get("id").asLong();

        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM todos WHERE id = ?", Integer.class, id);
        assertNotNull(count);
        assertEquals(1, count);
    }

    @Test
    void rejectEmptyText_returns422() throws Exception {
        String body = """
          {"text":"", "done": false}
        """;
        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void rejectMissingText_returns422() throws Exception {
        String body = """
          {"done": false}
        """;
        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void ignoreClientSentId_serverGeneratesOwnId() throws Exception {
        String body = """
          {"id":"client-sent", "text":"Buy bread", "done": false}
        """;

        var res = mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text").value("Buy bread"))
                .andExpect(jsonPath("$.done").value(false))
                .andExpect(jsonPath("$.id").isNumber()) // 服务器生成的数字 id
                .andReturn();

        String resp = res.getResponse().getContentAsString();
        JsonNode node = om.readTree(resp);
        String returnedId = node.get("id").asText();
        assertNotEquals("client-sent", returnedId);
    }
}

