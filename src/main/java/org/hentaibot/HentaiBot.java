package org.hentaibot;

import org.hentaibot.dtos.Rule34Dto;
import org.hentaibot.network.Client;
import org.hentaibot.network.HentaiQueries;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SuppressWarnings("CallToPrintStackTrace")
public class HentaiBot extends TelegramLongPollingBot {
    private static final Random random = new Random();
    private static final List<String> picExtensions =
            Arrays.asList(".jpg", ".jpeg", ".png", ".webp", ".avif", ".jfif");

    private static final HentaiQueries hentaiClient = Client.getNsfwClient();

    private final String botName;

    public HentaiBot(String botName, String botToken) {
        super(botToken);
        this.botName = botName;
    }

    // received message
    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            // send message indicating error (optional)
            return;
        }

        String userMsg = update.getMessage().getText(),
                chatId = update.getMessage().getChatId().toString(),
                respText;

        switch (userMsg) {
            case "/start" -> respText = """
                    Hi! I'm HentaiBot. I can send you some spicy anime pics.
                    Use me if you're 18+.
                    For list of all supported commands, try /help
                    """;
            case "/help" -> respText = """
                    List of commands available for now:
                    /help
                    /hentai
                    /anime
                    """;
            case "/anime", "/sfw" -> {
                respText = null;
                respondSfw(chatId);
            }
            case "/hentai", "/nsfw" -> {
                respText = null;
                respondNsfw(chatId);
            }
            default -> respText = "Invalid command. For list of all supported commands, try /help";
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
        int pageId = random.nextInt(1, 3970);

        try {
            hentaiClient
                    .getNsfw("anime", pageId)
                    .enqueue(
                            new Callback<>() {
                                @Override
                                public void onResponse(Call<List<Rule34Dto>> call, Response<List<Rule34Dto>> response) {
                                    var picDto = response.body() != null
                                            ? response.body().getFirst()
                                            : null;
                                    if (picDto == null) {
                                        respondNsfw(chatId);
                                        return;
                                    }

                                    var path = picDto.getFile_url();
                                    respondWithContent(chatId, path);
                                }

                                @Override
                                public void onFailure(Call<List<Rule34Dto>> call, Throwable throwable) {
                                    System.err.println(throwable.getMessage());
                                    throwable.printStackTrace();

                                    respondNsfw(chatId);
                                }
                            }
                    );


        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void respondSfw(String chatId) {

    }

    public void respondWithContent(String chatId, @NotNull String pathToSource) {
        try {
            var url = URI.create(pathToSource).toURL();
            var stream = url.openConnection().getInputStream();
            InputFile file = new InputFile(stream, pathToSource);

            if (pathToSource.endsWith(".gif"))
                respondWithGif(chatId, file);
            else if (picExtensions.stream().anyMatch(pathToSource::endsWith))
                respondWithPicture(chatId, file);
            else respondWithVideo(chatId, file);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void respondWithPicture(String chatId, InputFile pic) {
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

    public void respondWithGif(String chatId, InputFile file) {
        SendAnimation response = new SendAnimation();
        response.setChatId(chatId);
        response.setAnimation(file);

        try {
            execute(response);
        } catch (TelegramApiException ex) {
            System.err.println("GIF can't be sent (" + ex.getMessage() + ")");
            ex.printStackTrace();
        }
    }

    public void respondWithVideo(String chatId, InputFile file) {
        SendVideo response = new SendVideo();
        response.setChatId(chatId);
        response.setVideo(file);

        try {
            execute(response);
        } catch (TelegramApiException ex) {
            System.err.println("Video can't be sent (" + ex.getMessage() + ")");
            ex.printStackTrace();
        }
    }
}
