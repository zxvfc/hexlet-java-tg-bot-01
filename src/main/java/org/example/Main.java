package org.example;

// Игра 21
// Побеждает тот, кто наберёт 21-очко или наиболее близкое к нему значение
// Играют двое
// Крупье начинает с двух карт
// Затем очередь игрока
// Когда игрок останавливается, крупье добирает необходимые карты
// Проверка суммы карт

// 1) Реализировать колоду
// 2) Вывести колоду
// 3) Предложить игроку взять карту
// 4) Цикл с повторением шага номер 3
// 5) Проверка результатов
// 6) Вывод сообщения о победе/поражение


import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.*;

public class Main {

    static final int MIN_CARD_RANK = 1;
    static final int MAX_CARD_RANK = 9;
    static final int NUMBER_OF_CARD_TYPES = 4;
    static final int MAX_SAFE_SCORE = 17;
    static final int WIN_SCORE = 21;

    static int[] buildDeck() {
        Random random = new Random();
        int[] deck = new int[MAX_CARD_RANK * NUMBER_OF_CARD_TYPES];
        for (int i = MIN_CARD_RANK; i <= MAX_CARD_RANK; i++) {
            for (int j = 0; j < NUMBER_OF_CARD_TYPES; j++) {
                int currentElement = random.nextInt(deck.length);
                while (deck[currentElement] != 0) {
                    currentElement = random.nextInt(deck.length);
                }
                deck[currentElement] = i;
            }
        }
        return deck;
    }

    static String askPlayer(Scanner scanner) {
        System.out.print("Взять карту? (д/н): ");
        String choice = scanner.nextLine();
        while (!choice.equals("д") && !choice.equals("н")) {
            System.out.print("Взять карту? (д/н): ");
            choice = scanner.nextLine();
        }
        return choice;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] deck = buildDeck();
        int currentCard = 0;

        int dealerTotal = deck[currentCard++] + deck[currentCard++];
        int playerTotal = 0;

        while (true) {
            System.out.println("Ваш счёт: " + playerTotal);
            String choice = askPlayer(scanner);
            if (choice.equals("д")) {
                playerTotal += deck[currentCard++];
            }
            if (choice.equals("н")) {
                System.out.println("Ход крупье!");
                while (dealerTotal < MAX_SAFE_SCORE) {
                    dealerTotal += deck[currentCard++];
                }
                break;
            }
            if (playerTotal >= WIN_SCORE) {
                break;
            }
        }

        String scoreMessage = "Ваш счёт: " + playerTotal + ". Счёт крупье: " + dealerTotal;
        if (dealerTotal > WIN_SCORE || (dealerTotal < playerTotal) && playerTotal <= WIN_SCORE) {
            System.out.println("Вы победили! " + scoreMessage);
        } else if (dealerTotal == playerTotal) {
            System.out.println("Ничья! " + scoreMessage);
        } else {
            System.out.println("Крупье победил! " + scoreMessage);
        }
    }

}