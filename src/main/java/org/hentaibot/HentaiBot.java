package org.hentaibot;

import org.hentaibot.dtos.Rule34Dto;
import org.hentaibot.network.Client;
import org.hentaibot.network.HentaiQueries;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Random;

public class HentaiBot extends TelegramLongPollingBot {
    private static final Random random = new Random();

    private static final HentaiQueries hentaiClient = Client.getNsfwClient();

    private final String botName;

    public HentaiBot(String botName, String botToken) {
        super(botToken);
        this.botName = botName;
    }

//    private static final HashSet<String> AllowedMethods = new HashSet<>(
//            Arrays.asList("/start", "/help", "/hentai", "/sfw")
//    );

    private static final String lookingForPic = "Ok. Trying to find a nice pic for you...";

    // received message
    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            // send message indicating error (optional)
            return;
        }

        String  userMsg = update.getMessage().getText(),
                chatId = update.getMessage().getChatId().toString(),
                respText;

        switch (userMsg) {
            case "/start" -> {
                respText = """
                        Hi! // Welcome description
                        List of commands available for now:
                        /start
                        /help
                        /hentai
                        /sfw
                        """;
            }
            case "/help" -> {
                respText = """
                            List of commands available for now:
                            /start
                            /help
                            /hentai
                            /sfw
                            """;
            }
            case "/sfw" -> {
                respText = null;
                respondSfw(chatId);
            }
            case "/hentai", "/nsfw" -> {
                respText = null;
                respondNsfw(chatId);
            }
            default -> {
                respText = "Invalid command. For list of all supported commands, try /help";
            }
        }

        if (respText != null) {
            try {
                SendMessage response = new SendMessage();
                response.setChatId(chatId);
                response.setText(respText);

                execute(response);
            } catch (TelegramApiException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    public void respondNsfw(String chatId) {
        try {
            hentaiClient
                    .getNsfw("hentai", random.nextInt(1, 6001))
                    .enqueue(
                            new Callback<>() {
                                @Override
                                public void onResponse(Call<List<Rule34Dto>> call, Response<List<Rule34Dto>> response) {
                                    if (!response.isSuccessful())
                                        onFailure(call, new Exception("Unsuccessful request"));

                                    var picDto = response.body().get(0);
                                    var path = picDto.getFile_url();

                                    try {
                                        var url = URI.create(path).toURL();
                                        var stream = url.openConnection().getInputStream();
                                        InputFile file = new InputFile(stream, picDto.getImage());
                                        respondPicture(chatId, file);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Rule34Dto>> call, Throwable throwable) {
                                    System.err.println(throwable.getMessage());
                                    throwable.printStackTrace();
                                }
                            }
                    );


        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void respondSfw(String chatId) {
        //throw new Exception();
    }

    public void respondPicture(String chatId, InputFile pic) {
        SendPhoto response = new SendPhoto();
        response.setChatId(chatId);
        response.setPhoto(pic);

        try {
            execute(response);
        } catch (TelegramApiException ex) {
            System.err.println("Picture can't be sent (" + ex.getMessage() + ")");
            ex.printStackTrace();
        }
    }
}
