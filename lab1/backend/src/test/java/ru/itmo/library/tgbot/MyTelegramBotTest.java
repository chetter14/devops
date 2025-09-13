package ru.itmo.library.tgbot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MyTelegramBotTest {

    private MyTelegramBot bot;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        bot = Mockito.spy(new MyTelegramBot());

        // Use reflection to set private fields
        Field tokenField = MyTelegramBot.class.getDeclaredField("botToken");
        tokenField.setAccessible(true);
        tokenField.set(bot, "dummy-token");

        Field usernameField = MyTelegramBot.class.getDeclaredField("botUsername");
        usernameField.setAccessible(true);
        usernameField.set(bot, "dummy-username");

        Field chatIdField = MyTelegramBot.class.getDeclaredField("chatId");
        chatIdField.setAccessible(true);
        chatIdField.set(bot, "123456");
    }

    @Test
    public void testGetBotUsernameAndToken() {
        assert(bot.getBotUsername().equals("dummy-username"));
        assert(bot.getBotToken().equals("dummy-token"));
    }

    @Test
    public void testSendMessage_callsExecute() throws TelegramApiException {
        doReturn(mock(org.telegram.telegrambots.meta.api.objects.Message.class))
                .when(bot).execute(any(SendMessage.class));

        bot.sendMessage("Hello world");

        verify(bot, times(1)).execute(any(SendMessage.class));
    }

    @Test
    public void testSendMessage_handlesException() throws TelegramApiException {
        doThrow(new TelegramApiException("API failure")).when(bot).execute(any(SendMessage.class));

        bot.sendMessage("Hello world");

        verify(bot, times(1)).execute(any(SendMessage.class));
    }
}

