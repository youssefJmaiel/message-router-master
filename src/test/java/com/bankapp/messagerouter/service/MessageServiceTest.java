package com.bankapp.messagerouter.service;

import com.bankapp.messagerouter.entity.Message;
import com.bankapp.messagerouter.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    private Message sampleMessage;

    @BeforeEach
    void setUp() {
        // Message d'exemple
        sampleMessage = new Message();
        sampleMessage.setId(1L);
        sampleMessage.setContent("Hello, World!");
        sampleMessage.setSender("Alice");
        sampleMessage.setReceiver("Bob");
        sampleMessage.setTimestamp(LocalDateTime.now());
        sampleMessage.setProcessed(false);

        // Mock findAll
        when(messageRepository.findAll()).thenReturn(List.of(sampleMessage));

        // Mock save
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> {
            Message saved = invocation.getArgument(0);
            saved.setId(1L); // simule l'assignation de l'ID
            return saved;
        });
    }

    @Test
    void testSaveMessage() {
        Message message = new Message();
        message.setContent("Test Content");
        message.setSender("Alice");
        message.setReceiver("Bob");

        Message savedMessage = messageService.saveMessage(message);

        assertNotNull(savedMessage);
        assertEquals("Test Content", savedMessage.getContent());
        assertEquals("Alice", savedMessage.getSender());
        assertEquals("Bob", savedMessage.getReceiver());

        verify(messageRepository, times(1)).save(message);
    }

    @Test
    void testSaveMessageThrowsException() {
        Message message = new Message();
        when(messageRepository.save(any(Message.class))).thenThrow(new RuntimeException("Error saving message"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> messageService.saveMessage(message));
        assertEquals("Error saving message", ex.getMessage());
    }

    @Test
    void testGetAllMessages() {
        List<Message> messages = messageService.getAllMessages();

        assertNotNull(messages);
        assertFalse(messages.isEmpty());
        assertEquals(1, messages.size());
        assertEquals("Hello, World!", messages.get(0).getContent());
    }

    @Test
    void testGetAllMessagesEmpty() {
        when(messageRepository.findAll()).thenReturn(Collections.emptyList());

        List<Message> messages = messageService.getAllMessages();

        assertNotNull(messages);
        assertTrue(messages.isEmpty());
    }

    @Test
    void testGetAllMessagesWithException() {
        when(messageRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> messageService.getAllMessages());
        assertEquals("Database error", ex.getMessage());
    }

    @Test
    void deleteMessage_shouldCallRepository() {
        Long messageId = 1L;

        // Simule que le message existe
        when(messageRepository.existsById(messageId)).thenReturn(true);

        // Appel de la méthode
        messageService.deleteMessage(messageId);

        // Vérifie que existsById et deleteById ont été appelés
        verify(messageRepository, times(1)).existsById(messageId);
        verify(messageRepository, times(1)).deleteById(messageId);
    }
    @Test
    void deleteMessage_shouldThrowException_whenNotFound() {
        Long messageId = 2L;
        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> messageService.deleteMessage(messageId));
        assertEquals("Message not found", ex.getMessage());
    }

    @Test
    void getMessageById_shouldReturnMessage_whenFound() {
        Long messageId = 1L;
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(sampleMessage));

        Message foundMessage = messageService.getMessageById(messageId);

        assertNotNull(foundMessage);
        assertEquals("Hello, World!", foundMessage.getContent());
        verify(messageRepository, times(1)).findById(messageId);
    }

    @Test
    void getMessageById_shouldThrowException_whenNotFound() {
        Long messageId = 2L;
        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> messageService.getMessageById(messageId));
        assertEquals("Message not found", ex.getMessage());
        verify(messageRepository, times(1)).findById(messageId);
    }

    @Test
    void testGetMessagesPaginated() {
        Message message = new Message();
        message.setId(1L);
        message.setContent("Hello Paginated");
        Page<Message> page = new PageImpl<>(List.of(message));

        when(messageRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Message> result = messageService.getMessagesPaginated(0, 10, "timestamp", "asc");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Hello Paginated", result.getContent().get(0).getContent());
    }
}