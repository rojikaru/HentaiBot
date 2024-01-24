package org.hentaibot;

import org.hentaibot.configuration.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args)  {
        String  botName = Configuration.getBotName(),
                botToken = Configuration.getBotToken();

        try {
            TelegramBotsApi botsApi
                    = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new HentaiBot(botName, botToken));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
}
