package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.entity.Message;
import com.example.service.MessageService;

import java.util.List;

import com.example.repository.AccountRepository;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        
        if (message.getMessage_text().trim().isEmpty() || message.getMessage_text().length() > 254) {
            return ResponseEntity.badRequest().build();
        }

        
        if (!accountRepository.existsById(message.getPosted_by())) {
            return ResponseEntity.badRequest().build();
        }

        Message createdMessage = messageService.createMessage(message);
        return ResponseEntity.ok(createdMessage);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        if (messages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Message message = messageService.getMessageById(messageId);
        if (message == null) {
            return ResponseEntity.ok().build(); 
        }
        return ResponseEntity.ok(message);
    }

    // @DeleteMapping("/messages/{messageId}")
    // public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId) {
    //     int rowsDeleted = messageService.deleteMessageById(messageId);
    //     return ResponseEntity.ok(rowsDeleted);
    // }
    // @DeleteMapping("/messages/{message_id}")
    //     public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId) {
    //         if (messageService.getMessageById(messageId) != null) {
    //            int rowsDeleted = messageService.deleteMessageById(messageId);
    //            return ResponseEntity.ok(rowsDeleted);
    //         } else {
    //             return ResponseEntity.ok().build(); 
    //         }
    //     }
    @DeleteMapping("/messages/{message_id}")
        public ResponseEntity<?> deleteMessageByMessageId(@PathVariable int message_id) {
            if (messageService.getMessageById(message_id) != null) {
                int rowsDeleted = messageService.deleteMessageById(message_id);
                return ResponseEntity.ok(rowsDeleted);
            } else {
                return ResponseEntity.ok().build(); 
            }
        }


    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesForUser(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getAllMessagesForUser(accountId);
        if (messages.isEmpty()) {
            return ResponseEntity.ok(messages);
        }

        return ResponseEntity.ok(messages);
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessage(@PathVariable Integer messageId, @RequestBody Message messageUpdate) {
        try {
            
            if (messageUpdate.getMessage_text().trim().isEmpty() || messageUpdate.getMessage_text().length() > 255) {
                return ResponseEntity.badRequest().body("Message text is either empty or exceeds the allowable limit.");
            }

            Message updatedMessage = messageService.updateMessage(messageId, messageUpdate);

            if (updatedMessage == null) {
                return ResponseEntity.badRequest().body("Message with ID " + messageId + " not found.");
            }

            return ResponseEntity.ok(1); 
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
