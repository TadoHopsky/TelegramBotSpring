package org.example.springboottelegrambotjava.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.springboottelegrambotjava.conf.TelegramConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Component
public class MainMenuListener {
    private final TelegramClient telegramClient;

    public MainMenuListener(TelegramConfig telegramConfig) {
        this.telegramClient = new OkHttpTelegramClient(telegramConfig.telegramBotToken());
    }
    /**
     * Отправляет главное меню пользователю в виде сообщения с inline-кнопками.
     * <p>
     * Кнопки:
     * - "Как меня зовут?" → callbackData = "my_name"
     * - "Случайное число" → callbackData = "random_number"
     * - "Генерация изображения" → callbackData = "image_generation"
     *
     * @param chatId ID чата, куда отправить меню
     */
    @SneakyThrows
    public void sendMainMenu(Long chatId){
        SendMessage sendMessage = SendMessage.builder()
                .text("Добро пожаловать, выберите действие!")
                .chatId(chatId)
                .build();

        var button1 = InlineKeyboardButton.builder()
                .text("Как меня зовут?")
                .callbackData("my_name")
                .build();
        var button2 = InlineKeyboardButton.builder()
                .text("Случайное число")
                .callbackData("random_number")
                .build();
        var button3 = InlineKeyboardButton.builder()
                .text("генерация изображения")
                .callbackData("image_generation")
                .build();


        List<InlineKeyboardRow> buttons = List.of(
                new InlineKeyboardRow(button1),
                new InlineKeyboardRow(button2),
                new InlineKeyboardRow(button3));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);

        sendMessage.setReplyMarkup(markup);
        telegramClient.execute(sendMessage);
    }
}
