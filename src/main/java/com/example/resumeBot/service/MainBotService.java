package com.example.resumeBot.service;


import com.example.resumeBot.bot.Response;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MainBotService extends TelegramLongPollingBot implements BotService, Response {
    @Value("${telegram.bot.username}")
    private String username;
    @Value("${telegram.bot.token}")
    private String token;
    @Value("${telegram.group.chatId}")
    private String groupChatId;
    private Long userChatId;
    private String userMessage;
    private String userMessageAfter;
    private final UserService userService;
    private final Map<Long, String> start = new HashMap<>();
    private final Map<Long, String> inputAge = new HashMap<>();
    private final Map<Long, String> inputStudy = new HashMap<>();
    private final Map<Long, String> russianLang = new HashMap<>();
    private final Map<Long, String> uzbekLang = new HashMap<>();
    private final Map<Long, String> workLvl = new HashMap<>();
    private final Map<Long, String> getWorkLvl = new HashMap<>();
    private final Map<Long, String> getRussianLang = new HashMap<>();
    private final Map<Long, String> getUzbekLang = new HashMap<>();
    private final Map<Long, String> getInputStudy = new HashMap<>();
    private final Map<Long, String> getInputAge = new HashMap<>();
    private final Map<Long, String> inputName = new HashMap<>();
    private final Map<Long, String> sendFile = new HashMap<>();

    public MainBotService(UserService userService) {
        this.userService = userService;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        userChatId = getUserChatId(update);
        String inputText = getUserResponse(update);
        if (update.hasMessage()) {


            if ("Start".equals(start.get(userChatId))) {
                if (update.getMessage().hasText()) {
                    inputName.put(userChatId, inputText);
                    start.clear();
                    inputAge.put(userChatId, "Age");
                    userMessage = age;
                    execute1();
                }
            } else {
                if ("Age".equals(inputAge.get(userChatId))) {
                    if (update.getMessage().hasText()) {
                        getInputAge.put(userChatId, inputText);
                        inputAge.clear();
                        inputStudy.put(userChatId, "Study");
                        userMessageAfter = study;
                        execute(chooseStudy(), null);
                    }
                } else {
                    if ("Study".equals(inputStudy.get(userChatId))) {
                        if (update.getMessage().hasText()) {
                            getInputStudy.put(userChatId, inputText);
                            inputStudy.clear();
                            russianLang.put(userChatId, "RussianLang");
                            userMessageAfter = levelRussianLang;
                            execute(chooseRusLang(), null);
                        }
                    } else {
                        if ("RussianLang".equals(russianLang.get(userChatId))) {
                            if (update.getMessage().hasText()) {
                                getRussianLang.put(userChatId, inputText);
                                russianLang.clear();
                                uzbekLang.put(userChatId, "Uzbek Lang");
                                userMessageAfter = levelUzbekLang;
                                execute(chooseUzbLang(), null);
                            }

                        } else {
                            if ("Uzbek Lang".equals(uzbekLang.get(userChatId))) {
                                if (update.getMessage().hasText()) {
                                    getUzbekLang.put(userChatId, inputText);
                                    uzbekLang.clear();
                                    workLvl.put(userChatId, "Work Level");
                                    userMessageAfter = workLevel;
                                    execute(chooseWorkLvl(), null);
                                }
                            } else {
                                if ("Work Level".equals(workLvl.get(userChatId))) {
                                    if (update.getMessage().hasText()) {
                                        getWorkLvl.put(userChatId, inputText);
                                        workLvl.clear();
                                        userMessage="Пожалуйста, отправьте свой резюме";
                                        execute1();
                                    }
                                } else {
                                    if (update.getMessage().hasDocument()) {
                                        sendFile.put(userChatId, update.getMessage().getDocument().getFileId());
                                        SendMessage sendMessage = new SendMessage();
                                        sendMessage.setChatId(String.valueOf(userChatId));
                                        sendMessage.setText("Пожалуйста, отправьте свой контакт");
                                        shareContact(sendMessage);

                                    }else if (update.getMessage().getContact()!=null){
                                        String texts =
                                                "\uD83D\uDC68\u200D\uD83D\uDCBCФИО: " + inputName.get(userChatId) + "\n" +
                                                        "\n" + "\uD83E\uDD35\u200D♂️Возраст: " + getInputAge.get(userChatId) + "\n" +
                                                        "\n" + "\uD83D\uDC68\u200D\uD83C\uDF93Образование: " + getInputStudy.get(userChatId) + "\n" +
                                                        "\n" + "\uD83C\uDDF7\uD83C\uDDFAУровен русского языка: " + getRussianLang.get(userChatId) + "\n" +
                                                        "\n" + "\uD83C\uDDFA\uD83C\uDDFFУровен узбекского языка: " + getUzbekLang.get(userChatId) + "\n" +
                                                        "\n" + "\uD83D\uDC68\u200D\uD83D\uDCBBОпыт работы: " + getWorkLvl.get(userChatId) + "\n" +
                                                        "\n" + "☎️Телефон: " + update.getMessage().getContact().getPhoneNumber();
                                        executeGroup(texts);
                                        executeFile(sendFile.get(userChatId));
                                        userMessageAfter=latest;
                                        execute(mainMenu(),null);
                                        inputName.clear();
                                        getInputAge.clear();
                                        getInputStudy.clear();
                                        getRussianLang.clear();
                                        getUzbekLang.clear();
                                        getWorkLvl.clear();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (inputText.equals("/start")) {
                inputName.clear();
                getInputAge.clear();
                getInputStudy.clear();
                getRussianLang.clear();
                getUzbekLang.clear();
                getWorkLvl.clear();
                userService.saveUser(update);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText(startText);
                sendMessage.setParseMode(ParseMode.MARKDOWN);
                sendMessage.setChatId(String.valueOf(userChatId));

                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                List<InlineKeyboardButton> buttonList1 = new ArrayList<>();
                List<InlineKeyboardButton> buttonList2 = new ArrayList<>();
                List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                InlineKeyboardButton button1 = new InlineKeyboardButton();
                InlineKeyboardButton button2 = new InlineKeyboardButton();
                button1.setText("A'zo bo`lish");
                button1.setUrl("https://t.me/anorbankcareers");
                button2.setText("A'zo bo'lganman");
                button2.setCallbackData("start");
                buttonList1.add(button1);
                buttonList2.add(button2);
                buttons.add(buttonList1);
                buttons.add(buttonList2);
                inlineKeyboardMarkup.setKeyboard(buttons);
                sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else {
                if ("Главное меню".equals(inputText)){
                    update.getMessage().setText("/start");
                    onUpdateReceived(update);
                }
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            if (data.equals("start")) {
                start.put(userChatId, "Start");
                userMessage = name;
                execute1();
            }
        }

    }

    private void executeGroup(String text) {
        SendMessage sendMessage;
        sendMessage = new SendMessage(groupChatId, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void execute1() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(this.userChatId));
        sendMessage.setText(this.userMessage);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void executeFile(String fileId) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setDocument(new InputFile(fileId));
        sendDocument.setChatId(String.valueOf(groupChatId));
        try {
            execute(sendDocument);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shareContact(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("Пожалуйста, отправьте свой контакт");
        keyboardButton.setRequestContact(true);
        keyboardFirstRow.add(keyboardButton);
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        try {
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void execute(ReplyKeyboardMarkup replyKeyboardMarkup, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(this.userChatId));
        sendMessage.setText(this.userMessageAfter);
        sendMessage.enableHtml(true);
        if (replyKeyboardMarkup != null)
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        else if (inlineKeyboardMarkup != null)
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return this.username;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

}
