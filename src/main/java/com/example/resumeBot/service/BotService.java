package com.example.resumeBot.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public interface BotService {
    default Long getUserChatId(Update update) {
        if (update.hasMessage()) return update.getMessage().getChatId();
        return update.getCallbackQuery().getMessage().getChatId();
    }

    default String getUserResponse(Update update) {
        if (update.hasMessage()) if (update.getMessage().hasText()) return update.getMessage().getText();
        else return "Nothing not found!";
        return update.getCallbackQuery().getData();
    }


    default ReplyKeyboardMarkup chooseStudy() {
        return createMarkupButtons(
                "Законченное высшее", "Студент заочного отделения", "Студент 4го курса очного отделения", "Средне-специальное"
        );
    }

    default ReplyKeyboardMarkup chooseRusLang() {
        return createMarkupButtons(
                "В совершенстве", "Продвинутый", "Средний", "Плохо"
        );
    }

    default ReplyKeyboardMarkup chooseUzbLang() {
        return createMarkupButtons(
                "В совершенстве", "Продвинутый", "Средний", "Плохо"
        );
    }

    default ReplyKeyboardMarkup chooseWorkLvl() {
        return createMarkupButtons(
                "На аналогичной должности", "В банковской сфере", "Другое"
        );
    }
    default ReplyKeyboardMarkup mainMenu() {
        return createMarkupButtons(
                "Главное меню"
        );
    }

    default ReplyKeyboardMarkup makeReplyMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        return replyKeyboardMarkup;
    }

    default ReplyKeyboardMarkup createMarkupButtons(String firstButton, String secondButton, String thirdButton) {
        ReplyKeyboardMarkup replyKeyboardMarkup = makeReplyMarkup();
        KeyboardRow keyboardRow = new KeyboardRow();
        List<KeyboardRow> rowList = new ArrayList<>();

        keyboardRow.add(firstButton);
        keyboardRow.add(secondButton);
        rowList.add(keyboardRow);
        keyboardRow = new KeyboardRow();
        keyboardRow.add(thirdButton);
        rowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(rowList);
        return replyKeyboardMarkup;
    }
    default ReplyKeyboardMarkup createMarkupButtons(String firstButton) {
        ReplyKeyboardMarkup replyKeyboardMarkup = makeReplyMarkup();
        KeyboardRow keyboardRow = new KeyboardRow();
        List<KeyboardRow> rowList = new ArrayList<>();
        keyboardRow.add(firstButton);
        rowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(rowList);
        return replyKeyboardMarkup;
    }

    default ReplyKeyboardMarkup createMarkupButtons(String firstButton, String secondButton, String thirdButton, String fourthCard) {
        ReplyKeyboardMarkup replyKeyboardMarkup = makeReplyMarkup();
        KeyboardRow keyboardRow = new KeyboardRow();
        List<KeyboardRow> rowList = new ArrayList<>();

        keyboardRow.add(firstButton);
        keyboardRow.add(secondButton);
        rowList.add(keyboardRow);
        keyboardRow = new KeyboardRow();
        keyboardRow.add(thirdButton);
        keyboardRow.add(fourthCard);
        rowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(rowList);
        return replyKeyboardMarkup;
    }

}
