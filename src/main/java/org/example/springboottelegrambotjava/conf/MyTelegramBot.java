package org.example.springboottelegrambotjava.conf;

import lombok.RequiredArgsConstructor;
import org.example.springboottelegrambotjava.service.UpdateConsumer;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;

@Component
@RequiredArgsConstructor
public class MyTelegramBot implements SpringLongPollingBot {
    private final UpdateConsumer updateConsumer;
    private final TelegramConfig telegramConfig;

    @Override
    public String getBotToken() {
        return telegramConfig.telegramBotToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return updateConsumer;
    }
}
