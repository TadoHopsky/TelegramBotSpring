package org.example.springboottelegrambotjava.service;

import lombok.SneakyThrows;
import org.example.springboottelegrambotjava.conf.TelegramConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final MainMenuListener mainMenuListener;

    public UpdateConsumer(TelegramConfig telegramConfig, MainMenuListener mainMenuListener) {
        this.telegramClient = new OkHttpTelegramClient(telegramConfig.telegramBotToken());
        this.mainMenuListener = mainMenuListener;
    }

    /**
     * Обрабатывает входящие сообщения от пользователя.
     *<p></p>
     * Если сообщение текстовое и равно "/start" — отправляет главное меню.
     * В противном случае — отвечает сообщением "Я вас не понимаю".
     *
     * @param update объект Telegram Update, содержащий входящее сообщение
     */
    @Override
    @SneakyThrows
    public void consume(Update update) {
        if (update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();

            if(text.equals("/start")){
                mainMenuListener.sendMainMenu(chatId);
            }else{
                SendMessage sendMessage = SendMessage.builder()
                        .text("Я вас не понимаю")
                        .chatId(chatId)
                        .build();
                telegramClient.execute(sendMessage);
            }
        }
    }
}


