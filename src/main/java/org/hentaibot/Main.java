package org.hentaibot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main /*extends Object*/ {
    public static void main(String[] args)  {
        // class Object -> virtual: toString, equals, getClass
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new HentaiBot(

                    ));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
}
