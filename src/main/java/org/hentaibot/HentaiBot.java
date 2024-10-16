package org.hentaibot;

import org.apache.log4j.Logger;
import org.hentaibot.constants.Constants;
import org.hentaibot.dtos.Rule34Dto;
import org.hentaibot.dtos.WaifuDto;
import org.hentaibot.network.waifu.AnimeQueries;
import org.hentaibot.network.rule34.Rule34ApiClient;
import org.hentaibot.network.rule34.HentaiQueries;
import org.hentaibot.network.waifu.WaifuApiClient;
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
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class HentaiBot extends TelegramLongPollingBot {
    private static final Random random = new Random();
    private static final Logger logger = Logger.getLogger(HentaiBot.class.getName());

    private static final HentaiQueries hentaiClient = Rule34ApiClient.getNsfwClient();
    private static final AnimeQueries animeClient = WaifuApiClient.getSfwClient();

    private final String botName;

    public HentaiBot(String botName, String botToken) {
        super(botToken);
        this.botName = botName;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            respondWithText(chatId, Constants.MESSAGES.get("UNRECOGNIZED_INPUT"));
            return;
        }

        String userMsg = update.getMessage().getText();

        if (userMsg.startsWith("/tags_"))
            respondOnTags(chatId, userMsg);
        else
            respondOnCommands(chatId, userMsg);
    }

    public void respondOnTags(String chatId, String userMsg) {
        if (!userMsg.startsWith("/tags_")) return;

        String tag = userMsg.substring(6);
        Optional<Integer> randomUpperBound =
                Constants.TAGS.contains(tag)
                        ? Optional.of(50000)
                        : Constants.ARTISTS.contains(tag)
                        ? Optional.of(500)
                        : Optional.empty();

        if (randomUpperBound.isPresent())
            respondNsfw(chatId, tag, randomUpperBound.get());
        else
            respondWithText(chatId, Constants.MESSAGES.get("UNRECOGNIZED_INPUT"));
    }

    public void respondOnCommands(String chatId, String userMsg) {
        switch (userMsg) {
            case "/start" -> respondWithText(chatId, Constants.MESSAGES.get("START"));
            case "/help" -> respondWithText(chatId, Constants.MESSAGES.get("HELP"));
            case "/help_tags" -> respondWithText(chatId, Constants.MESSAGES.get("HELP_TAGS"));
            case "/anime", "/sfw" -> respondSfw(chatId);
            case "/hentai", "/nsfw" -> respondNsfw(chatId, "female", 200000);
            default -> respondWithText(chatId, Constants.MESSAGES.get("UNRECOGNIZED_INPUT"));
        }
    }

    public void respondNsfw(String chatId, String tag, int randomUpperBound) {
        int pageId = random.nextInt(1, randomUpperBound);
        try {
            hentaiClient
                    .getNsfw(tag, pageId)
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
        int categoryIndex = random.nextInt(Constants.SFW_CATEGORIES.size());
        String category = Constants.SFW_CATEGORIES.get(categoryIndex);

        animeClient
                .getSFW(category)
                .enqueue(
                        new Callback<>() {
                            @Override
                            public void onResponse(Call<WaifuDto> call, Response<WaifuDto> response) {
                                var picDto = response.body();
                                if (picDto == null) {
                                    onFailure(call, new IOException("Response body is null"));
                                    return;
                                }

                                var path = picDto.getUrl();
                                logger.info("\nResponse:\n" + path + "\n");
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
            else if (Constants.PICTURE_EXTENSIONS.stream().anyMatch(pathToSource::endsWith))
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
