package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Message getMessageById(Integer id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        return optionalMessage.orElse(null);
    }

    @Override
    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Message updateMessage(Integer id, Message message) {
        
        if (messageRepository.existsById(id)) {

            
            if (message.getMessage_text().isEmpty()) {
                throw new IllegalArgumentException("Message text cannot be empty.");
            }

            
            if (message.getMessage_text().length() > 255) {
                throw new IllegalArgumentException("Message text exceeds the allowable limit.");
            }

            
            return messageRepository.save(message);
        }
        return null; 
    }

    

    @Override
    public Message createMessage(Message message) {
        
        return messageRepository.save(message);
    }

    @Override
    public int deleteMessageById(Integer messageId) {
        return messageRepository.deleteMessageById(messageId);
        
    }

    @Override
    public List<Message> getAllMessagesForUser(Integer userId) {
        return messageRepository.findAllByPosted_by(userId);
    }

    @Override
    public void deleteMessage(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteMessage'");
    }

}

