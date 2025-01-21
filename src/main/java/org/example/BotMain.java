package org.example;

import java.util.Random;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class BotMain extends TelegramLongPollingBot {

    int playerTotal;
    int dealerTotal;
    boolean isPlaying;
    int[] deck;
    int currentCard;

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(
                DefaultBotSession.class
        );
        telegramBotsApi.registerBot(new BotMain());
        System.out.println("Hello!");
    }

    static int[] buildDeck() {
        Random random = new Random();
        int[] deck = new int[36];
        for (int i = 1; i <= 9; i++) {
            for (int j = 0; j < 4; j++) {
                int currentElement = random.nextInt(deck.length);
                while (deck[currentElement] != 0) {
                    currentElement = random.nextInt(deck.length);
                }
                deck[currentElement] = i;
            }
        }
        return deck;
    }

    void initGame() {
        isPlaying = false;
        deck = buildDeck();
        currentCard = 0;

        playerTotal = 0;
        dealerTotal = 0;
    }

    @Override
    public void onUpdateReceived(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String userInput = update.getMessage().getText();
        System.out.println(userInput);

        if (userInput.equals("/start")) {
            sendMessage(chatId, "Добро пожаловать в игру 21");
            sendMessage(chatId, "Взять карту? (д/н)");
            initGame();
            dealerTotal += deck[currentCard++] + deck[currentCard++];
            isPlaying = true;
        }
        if (!isPlaying) {
            sendMessage(chatId, "Что бы начать игру введите команду /start");
            return;
        }
        if (userInput.equals("д")) {
            playerTotal += deck[currentCard++];
            if (playerTotal > 21) {
                isPlaying = false;
            }
            sendMessage(chatId, "Ваш счёт: " + playerTotal);
            sendMessage(chatId, "Взять карту? (д/н)");
        }
        if (userInput.equals("н")) {
            sendMessage(chatId, "Ход крупье...");
            while (dealerTotal < 17) {
                dealerTotal += deck[currentCard++];
            }
            isPlaying = false;
        }

        if (!isPlaying) {
            String scoreMessage = "Ваш счёт: " + playerTotal + ". Счёт крупье: " + dealerTotal;
            if (dealerTotal > 21 || playerTotal <= 21 && (playerTotal > dealerTotal)) {
                sendMessage(chatId, "Вы победили! " + scoreMessage);
            } else if (dealerTotal == playerTotal) {
                sendMessage(chatId, "Ничья! " + scoreMessage);
            } else {
                sendMessage(chatId, "Победил крупье! " + scoreMessage);
            }
            sendMessage(chatId, "Игра окончена. Что бы сыграть ещё раз введите команду /start");
        }
    }

    void sendMessage(String chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "Hexlet-Java-21-Bot";
    }

    @Override
    public String getBotToken() {
        return ""; // Судя подставлять токен
    }
}
