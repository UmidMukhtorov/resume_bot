package com.example.resumeBot.config;

import com.example.resumeBot.service.MainBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class BotConfig {

    @Autowired
    private MainBotService bot;

    @PostConstruct
    protected void init() {
        //инициализируйте конфигурацию здесь
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
            System.out.println("Bot ishladi");
        } catch (TelegramApiException e) {
            /// it's your exception
        }
    }

    @PreDestroy
    protected void closeSession() {
        //де-инициализируйте конфигурацию здесь если есть такая необходимость
        //например закройте connection если таковой имеется
    }




}
