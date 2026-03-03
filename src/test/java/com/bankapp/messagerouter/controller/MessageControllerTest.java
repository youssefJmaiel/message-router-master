package com.bankapp.messagerouter.controller;

import com.bankapp.messagerouter.entity.Message;
import com.bankapp.messagerouter.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ---------------------- GET ALL ----------------------
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllMessages() throws Exception {
        Message message = new Message();
        message.setContent("Hello, World!");
        message.setSender("Alice");
        message.setReceiver("Bob");

        when(messageService.getAllMessages()).thenReturn(List.of(message));

        mockMvc.perform(get("/api/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Hello, World!"))
                .andExpect(jsonPath("$[0].sender").value("Alice"))
                .andExpect(jsonPath("$[0].receiver").value("Bob"));
    }

    // ---------------------- POST ----------------------
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testSaveMessage() throws Exception {
        Message message = new Message();
        message.setContent("New Message");
        message.setSender("Alice");
        message.setReceiver("Bob");

        String messageJson = objectMapper.writeValueAsString(message);
        when(messageService.saveMessage(any(Message.class))).thenReturn(message);

        mockMvc.perform(post("/api/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(messageJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("New Message"))
                .andExpect(jsonPath("$.sender").value("Alice"))
                .andExpect(jsonPath("$.receiver").value("Bob"));
    }

    // ---------------------- DELETE ----------------------
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteMessage_Success() throws Exception {
        Long messageId = 1L;
        when(messageService.findById(messageId)).thenReturn(Optional.of(new Message()));
        doNothing().when(messageService).deleteMessage(messageId);

        mockMvc.perform(delete("/api/message/{id}", messageId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testDeleteMessage_NotFound() throws Exception {
        Long messageId = 1L;
        when(messageService.findById(messageId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/message/{id}", messageId))
                .andExpect(status().isNotFound());
    }

    // ---------------------- PAGINATION ----------------------
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetMessagesPaginated() throws Exception {
        Message message = new Message();
        message.setId(1L);
        message.setContent("Paged Message");
        message.setSender("Alice");
        message.setReceiver("Bob");

        Page<Message> page = new PageImpl<>(List.of(message));
        when(messageService.getMessagesPaginated(0, 10, "timestamp", "desc")).thenReturn(page);

        mockMvc.perform(get("/api/messages/paged")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "timestamp")
                        .param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].content").value("Paged Message"))
                .andExpect(jsonPath("$.content[0].sender").value("Alice"))
                .andExpect(jsonPath("$.content[0].receiver").value("Bob"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }
}