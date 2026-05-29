package com.example.taskmanagement.controller;

import com.example.taskmanagement.domain.TaskStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullCrudFlow() throws Exception {

        var createReq = new TaskController.CreateTaskRequest("Title", "Desc", null, LocalDate.now().plusDays(5));
        var createResult = mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        var created = objectMapper.readTree(createResult.getResponse().getContentAsString());
        String id = created.get("id").asText();

        mockMvc.perform(get("/tasks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"));

        var updateReq = new TaskController.UpdateTaskRequest("NewTitle", null, TaskStatus.DONE, null);
        mockMvc.perform(put("/tasks/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("NewTitle"))
                .andExpect(jsonPath("$.status").value("DONE"));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id));

        mockMvc.perform(delete("/tasks/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/tasks/{id}", id))
                .andExpect(status().isNotFound());
    }
}
