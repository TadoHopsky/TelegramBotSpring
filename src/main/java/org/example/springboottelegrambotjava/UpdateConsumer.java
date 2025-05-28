package org.example.springboottelegrambotjava;

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

    public UpdateConsumer(TelegramConfig telegramConfig) {
        this.telegramClient = new OkHttpTelegramClient(telegramConfig.telegramBotToken());
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
                sendMainMenu(chatId);
            }else{
                SendMessage sendMessage = SendMessage.builder()
                        .text("Я вас не понимаю")
                        .chatId(chatId)
                        .build();
                telegramClient.execute(sendMessage);
            }
        }
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


