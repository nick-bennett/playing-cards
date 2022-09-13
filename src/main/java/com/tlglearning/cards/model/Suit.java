package com.tlglearning.cards.model;

public enum Suit {

  CLUBS('\u2663'), // Becomes public static final Suit CLUBS = new Suit('\u2663');
  DIAMONDS('\u2662'),
  HEARTS('\u2661'),
  SPADES('\u2660');

  private final char symbol;

  Suit(char symbol) {
    this.symbol = symbol;
  }

  public char symbol() {
    return symbol;
  }

  @Override
  public String toString() {
    String name = name();
    return name.charAt(0) + name.substring(1).toLowerCase();
  }

}
