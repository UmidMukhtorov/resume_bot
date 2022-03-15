package com.example.resumeBot.service;

import com.example.resumeBot.domain.User;
import com.example.resumeBot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(Update update) {
        Long chatId = update.getMessage().getChatId();
        Optional<User> users = userRepository.findByChatId(chatId);
        User user = new User();
        if (users.isEmpty()) {
            user.setChatId(chatId);
            user.setUsername(update.getMessage().getFrom().getUserName());
            user.setName(update.getMessage().getFrom().getFirstName());
            userRepository.save(user);
        }

    }

    public void saveUserLanguage(Update update, String lang) {
        Long chatId = update.getMessage().getChatId();
        Optional<User> users = userRepository.findByChatId(chatId);

        if (users.isPresent()) {
            User user = users.get();
            user.setLanguage(lang);
            userRepository.save(user);
        }
    }

    public String editUserPhone(Update update, String phone){
        Long chatId = update.getMessage().getChatId();

        Optional<User> user = userRepository.findByChatId(chatId);

        if (user.isPresent()){
            user.get().setPhoneNumber(phone);
            userRepository.save(user.get());
            return "success";
        } else return "error";
    }

    public Optional<User> findUserByChatId(Update update){
        return userRepository.findByChatId(update.getMessage().getChatId());
    }

}
