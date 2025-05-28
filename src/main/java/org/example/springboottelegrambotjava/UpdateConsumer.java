package org.example.springboottelegrambotjava;

import org.example.springboottelegrambotjava.conf.TelegramConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;

    public UpdateConsumer(TelegramConfig telegramConfig) {
        this.telegramClient = new OkHttpTelegramClient(telegramConfig.telegramBotToken());
    }


    @Override
    public void consume(Update update) {
        System.out.printf("Пришло сообщение: %s от id:%s",
                update.getMessage().getText(),
                update.getMessage().getChatId());

        var chatId = update.getMessage().getChatId();
        var text = update.getMessage().getText();

        SendMessage sendMessage = SendMessage.builder()
                .text("Привет , твоё сообщение: " + text)
                .chatId(chatId)
                .build();

        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
