package edu.cnm.deepdive;

import edu.cnm.deepdive.Suit.Color;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Stack;

public class CardTrick {

  public static void main(String[] args) {
    Random rng = new Random();
    Stack<Card> deck = new Stack<>();
    Stack<Card> redPile = new Stack<>();
    Stack<Card> blackPile = new Stack<>();
    for (Suit suit : Suit.values()) {
      for (Rank rank : Rank.values()) {
        deck.push(new Card(rank, suit));
      }
    }
    Collections.shuffle(deck, rng); // Not strictly necessary
    while (!deck.isEmpty()) {
      Card selector  = deck.pop();
      Card card = deck.pop();
      if (selector.getSuit().getColor() == Suit.Color.RED) {
        redPile.push(card);
      } else {
        blackPile.push(card);
      }
    }
    assert 2 * (redPile.size() + blackPile.size()) == Suit.values().length * Rank.values().length;
    Collections.shuffle(redPile, rng); // Not strictly necessary
    Collections.shuffle(blackPile, rng); // Not stricty necessary
    int n = rng.nextInt(Math.min(redPile.size(), blackPile.size()) + 1);
    Stack<Card> redExtract = new Stack<>();
    Stack<Card> blackExtract = new Stack<>();
    for (int i = 0; i < n; i++) {
      redExtract.push(redPile.pop());
      blackExtract.push(blackPile.pop());
    }
    redPile.addAll(blackExtract);
    blackPile.addAll(redExtract);
    Comparator<Card> comparator = new ColorComparator();
    redPile.sort(comparator);
    blackPile.sort(comparator);
    System.out.printf("Red pile: %s%n", redPile);
    System.out.printf("Black pile: %s%n", blackPile);
    int redCount = 0;
    int blackCount = 0;
    for (Card card : redPile) {
      if (card.getSuit().getColor() == Color.RED) {
        redCount++;
      }
    }
    for (Card card : blackPile) {
      if (card.getSuit().getColor() == Color.BLACK) {
        blackCount++;
      }
    }
//    long redCount = redPile.parallelStream()
//        .filter((c) -> c.getSuit().getColor() == Color.RED)
//        .count();
//    long blackCount = blackPile.parallelStream()
//        .filter((c) -> c.getSuit().getColor() == Color.BLACK)
//        .count();
    assert redCount == blackCount;
  }

  private static class ColorComparator implements Comparator<Card> {

    @Override
    public int compare(Card card1, Card card2) {
      return card1.getSuit().getColor().compareTo(card2.getSuit().getColor());
    }

  }

}







