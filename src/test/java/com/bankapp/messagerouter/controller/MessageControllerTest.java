package com.bankapp.messagerouter.controller;

import com.bankapp.messagerouter.dto.MessageRequest;
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
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------------------- GET ALL MESSAGES ----------------------
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetAllMessages() throws Exception {
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

    // ---------------------- GET MESSAGE BY ID ----------------------
    @Test
    @WithMockUser
    public void testGetMessageById_found() throws Exception {
        Message message = new Message();
        message.setId(1L);
        message.setContent("Message 1");
        message.setSender("Alice");
        message.setReceiver("Bob");

        when(messageService.getMessageById(1L)).thenReturn(message);

        mockMvc.perform(get("/api/message/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Message 1"))
                .andExpect(jsonPath("$.sender").value("Alice"))
                .andExpect(jsonPath("$.receiver").value("Bob"));
    }

    // ---------------------- GET MESSAGES PAGINATED ----------------------
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetMessagesPaginated() throws Exception {
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

    // ---------------------- SAVE MESSAGE ----------------------
    @Test
    @WithMockUser
    public void testSaveMessage() throws Exception {
        Message message = new Message();
        message.setContent("New Message");
        message.setSender("Alice");
        message.setReceiver("Bob");

        when(messageService.saveMessage(any(Message.class))).thenReturn(message);

        mockMvc.perform(post("/api/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(message)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("New Message"))
                .andExpect(jsonPath("$.sender").value("Alice"))
                .andExpect(jsonPath("$.receiver").value("Bob"));
    }

    // ---------------------- DELETE MESSAGE ----------------------
    @Test
    @WithMockUser
    public void testDeleteMessage_found() throws Exception {
        Message message = new Message();
        message.setId(1L);

        when(messageService.findById(1L)).thenReturn(Optional.of(message));
        doNothing().when(messageService).deleteMessage(1L);

        mockMvc.perform(delete("/api/message/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    public void testDeleteMessage_notFound() throws Exception {
        when(messageService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/message/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    // ---------------------- SEND MESSAGE ----------------------
    @Test
    @WithMockUser
    public void testSendMessage() throws Exception {
        MessageRequest request = new MessageRequest();
        request.setContent("Sent message");
        request.setSender("Alice");
        request.setReceiver("Bob");

        Message message = new Message();
        message.setContent("Sent message");
        message.setSender("Alice");
        message.setReceiver("Bob");

        when(messageService.sendMessage(any(), any(), any())).thenReturn(message);

        mockMvc.perform(post("/api/message/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Sent message"))
                .andExpect(jsonPath("$.sender").value("Alice"))
                .andExpect(jsonPath("$.receiver").value("Bob"));
    }
}