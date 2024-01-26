package org.hentaibot;

import org.apache.log4j.Logger;
import org.hentaibot.dtos.Rule34Dto;
import org.hentaibot.dtos.WaifuDto;
import org.hentaibot.network.AnimeWaifuQueries;
import org.hentaibot.network.Client;
import org.hentaibot.network.ClientSFW;
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

public class HentaiBot extends TelegramLongPollingBot {
    private static final Random random = new Random();
    private static final List<String> picExtensions =
            Arrays.asList(".jpg", ".jpeg", ".png", ".webp", ".avif", ".jfif");
    private static final Logger logger = Logger.getLogger(HentaiBot.class.getName());

    private static final HentaiQueries hentaiClient = Client.getNsfwClient();
    private static final AnimeWaifuQueries animeClient = ClientSFW.getSfwClient();
    private final String botName;

    public HentaiBot(String botName, String botToken) {
        super(botToken);
        this.botName = botName;
    }

    // received message
    @Override
    public void onUpdateReceived(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            respondWithText(chatId, "I don't understand you. " +
                    "For list of all supported commands, try /help");
            return;
        }

        String userMsg = update.getMessage().getText(), respText;
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

        respondWithText(chatId, respText);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    public void respondNsfw(String chatId) {
        int pageId = random.nextInt(1, 3970);

        try {
            hentaiClient
                    .getNsfw("female", pageId)
                    .enqueue(
                            new Callback<>() {
                                @Override
                                public void onResponse(Call<List<Rule34Dto>> call, Response<List<Rule34Dto>> response) {
                                    var picDto = response.body() != null
                                            ? response.body().getFirst()
                                            : null;

                                    logger.info("\nResponse:\n" + picDto + "\n");

                                    if (picDto == null) {
                                        onFailure(call, new IOException("Response body is null"));
                                        return;
                                    }

                                    var path = picDto.getFile_url();
                                    respondWithContent(chatId, path);
                                }

                                @Override
                                public void onFailure(Call<List<Rule34Dto>> call, Throwable throwable) {
                                    logger.error(throwable.getMessage());
                                    respondWithText(chatId, "Failed to get hentai. Try again later");
                                }
                            }
                    );


        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    public void respondSfw(String chatId) {
         final String[] categories = new String[]{"waifu", "neko", "shinobu", "megumin", "hug", "awoo", "kiss", "lick", "smug", "blush", "smile", "wave", "nom", "glomp", "slap", "kill", "happy", "wink", "poke", "dance", "cringe"};
        String urlCategory = categories[random.nextInt(0, categories.length)];
        animeClient
                .getSFW(urlCategory)
                .enqueue(
                        new Callback<>() {
                            @Override
                            public void onResponse(Call<WaifuDto> call, Response<WaifuDto> response) {
                                var picSDto = response.body();
                                logger.info("\nResponse:\n" + picSDto.getUrl() + "\n");

                                if (picSDto == null) {
                                    onFailure(call, new IOException("Response body is null"));
                                    return;
                                }

                                var path = picSDto.getUrl();
                                respondWithContent(chatId, path);
                            }

                            @Override
                            public void onFailure(Call<WaifuDto> call, Throwable throwable) {
                                logger.error(throwable.getMessage());
                                respondWithText(chatId, "Failed to get hentai. Try again later");
                            }
                        }
                );

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
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }

    public void respondWithPicture(String chatId, InputFile pic) {
        if (pic == null || chatId == null || chatId.isBlank()) return;

        SendPhoto response = new SendPhoto();
        response.setChatId(chatId);
        response.setPhoto(pic);

        try {
            execute(response);
        } catch (TelegramApiException ex) {
            logger.error("Picture can't be sent (" + ex.getMessage() + ")");
        }
    }

    public void respondWithGif(String chatId, InputFile file) {
        if (file == null || chatId == null || chatId.isBlank()) return;

        SendAnimation response = new SendAnimation();
        response.setChatId(chatId);
        response.setAnimation(file);

        try {
            execute(response);
        } catch (TelegramApiException ex) {
            logger.error("GIF can't be sent (" + ex.getMessage() + ")");
        }
    }

    public void respondWithVideo(String chatId, InputFile file) {
        if (file == null || chatId == null || chatId.isBlank()) return;

        SendVideo response = new SendVideo();
        response.setChatId(chatId);
        response.setVideo(file);


        try {
            execute(response);
        } catch (TelegramApiException ex) {
            logger.error("Video can't be sent (" + ex.getMessage() + ")");
        }
    }

    public void respondWithText(String chatId, String responseText) {
        if (responseText == null || responseText.isBlank() ||
                chatId == null || chatId.isBlank()) return;

        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText(responseText);

        try {
            execute(response);
        } catch (TelegramApiException ex) {
            logger.error("Text can't be sent (" + ex.getMessage() + ")");
        }
    }
}
