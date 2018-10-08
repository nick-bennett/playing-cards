package edu.cnm.deepdive;

import edu.cnm.deepdive.Suit.Color;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * This class implements the card trick described in <a
 * href="http://rosettacode.org/wiki/Mind_boggling_card_trick">Mind boggling card
 * trick</a>. The trick itself is implemented in the {@link #main} method, while
 * the various steps (shuffling, dealing, swapping cards between the 2 piles of
 * cards) are implemented in instance methods&mdash;partly to illustrate method
 * decomposition, and partly to facilitate unit testing.
 *
 * @author Nicholas Bennett &amp; Deep Dive Coding Java Cohort 5
 */
public class CardTrick {

  private Random rng;
  private Stack<Card> deck;
  private List<Card> discardPile;
  private Stack<Card> redPile;
  private Stack<Card> blackPile;

  /**
   * Shuffles, deals, and swaps cards as described in <a
   * href="http://rosettacode.org/wiki/Mind_boggling_card_trick">Mind boggling
   * card trick</a>. After each relevant step, the color count invariant
   * condition (i.e. the number of red cards in the red pile is equal to the
   * number of black cards in the black pile) is tested with the
   * <code>assert</code> statement (which can be enabled using the
   * <code>-enableassertions</code> or <code>-ea</code> option). After all the
   * steps are completed, the cards in the red and black piles are sorted by
   * color, making it easier to verify the results visually.
   *
   * @param args  command line arguments (currently ignored).
   */
  public static void main(String[] args) {
    CardTrick trick = new CardTrick();
    trick.shuffle();
    trick.deal();
    assert trick.count(Color.RED, Color.RED) == trick.count(Color.BLACK, Color.BLACK);
    trick.swap();
    assert trick.count(Color.RED, Color.RED) == trick.count(Color.BLACK, Color.BLACK);
    List<Card> redPile = trick.getPile(Color.RED);
    List<Card> blackPile = trick.getPile(Color.BLACK);
    Comparator<Card> comparator = new ColorComparator();
    Collections.sort(redPile, comparator);
    Collections.sort(blackPile, comparator);
    System.out.printf("%s pile: %s%n", "Red", redPile);
    System.out.printf("%s pile: %s%n", "Black", blackPile);
  }

  /**
   * Initializes an instance of <code>CardTrick</code> with an instance of
   * {@link Random} as a source of randomness. Invoking this constructor is
   * equivalent to invoking {@link #CardTrick(Random) CardTrick(new
   * java.util.Random()} (see that constructor for initialization details).
   */
  public CardTrick() {
    this(new Random());
  }

  /**
   * Initializes an instance of <code>CardTrick</code> with the specified source
   * of randomness. On initialization, the {@link List List&lt;Card&gt;}
   * returned by {@link #getDeck()} contains all the cards in a single standard
   * deck of playing cards (unshuffled), while those returned by {@link
   * #getPile(Color)} are empty.
   *
   * @param rng   source of randomness (used for shuffling).
   */
  public CardTrick(Random rng) {
    this.rng = rng;
    deck = new Stack<>();
    redPile = new Stack<>();
    blackPile = new Stack<>();
    discardPile = new LinkedList<>();
    for (Suit suit : Suit.values()) {
      for (Rank rank : Rank.values()) {
        deck.push(new Card(rank, suit));
      }
    }
  }

  /**
   * Shuffles (separately) the contents of the original deck, the red pile, and
   * the black pile. Note that it will be the case that the original deck
   * contains either all of the cards, or (after invocation of {@link #deal()})
   * <em>none</em> of the cards; in the latter case, the red and black piles
   * contain a combined total of 26 cards (half of the deck), while the discard
   * pile contains the other 26. (Note that the discard pile is not shuffled by
   * this method.)
   */
  public void shuffle() {
    Collections.shuffle(deck, rng);
    Collections.shuffle(redPile, rng);
    Collections.shuffle(blackPile, rng);
  }

  /**
   * Deals the cards from the main deck into a discard pile, red pile, and black
   * pile, as described in <a
   * href="http://rosettacode.org/wiki/Mind_boggling_card_trick">Mind boggling
   * card trick</a>.
   */
  public void deal() {
    while (!deck.isEmpty()) {
      Card selector = deck.pop();
      Card card = deck.pop();
      if (selector.getSuit().getColor() == Suit.Color.RED) {
        redPile.push(card);
      } else {
        blackPile.push(card);
      }
      discardPile.add(selector);
    }
  }

  /**
   * Selects and removes a random number of cards from the red pile, and an
   * equal number from the black pile, and adds each selection to the opposite
   * pile. That is, the cards selected from the red pile are added to the black
   * pile, and vice versa.
   */
  public void swap() {
    swap(rng.nextInt(Math.min(redPile.size(), blackPile.size()) + 1));
  }

  /**
   * Selects and removes the specified number of cards from the red pile, and an
   * equal number from the black pile, and adds each selection to the opposite
   * pile. That is, the cards selected from the red pile are added to the black
   * pile, and vice versa.
   *
   * @param n   number of cards to swap between piles.
   */
  public void swap(int n) {
    Stack<Card> redExtract = new Stack<>();
    Stack<Card> blackExtract = new Stack<>();
    for (int i = 0; i < n; i++) {
      redExtract.push(redPile.pop());
      blackExtract.push(blackPile.pop());
    }
    redPile.addAll(blackExtract);
    blackPile.addAll(redExtract);
  }

  /**
   * Returns a copy of the deck of cards. This is a "safe" copy: any changes to
   * the {@link List List<Card>} returned have no effect on the deck maintained
   * by this instance of <code>CardTrick</code>. (Note that after invocation of
   * {@link #deal()}, the deck is empty.)
   *
   * @return  safe copy of the deck of cards used for the card trick.
   */
  public List<Card> getDeck() {
    return new LinkedList<>(deck);
  }

  /**
   * Returns a copy of the specified pile (all of which are populated during
   * execution of {@link #deal()}). This is a "safe" copy: any changes to the
   * {@link List List<Card>} returned have no effect on the corresponding pile
   * maintained internally.
   *
   * @param color specified pile to return (a value of <code>null</code> returns
   *              the discard pile).
   * @return      safe copy of the specified pile.
   */
  public List<Card> getPile(Color color) {
    switch (color) {
      case RED:
        return new LinkedList<>(redPile);
      case BLACK:
        return new LinkedList<>(blackPile);
      default:
        return new LinkedList<>(discardPile);
    }
  }

  /**
   * Computes and returns the number of cards of the specified color in the
   * specified pile.
   *
   * @param cardColor color of cards to count.
   * @param pileColor pile to count from (<code>null</code> counts from the
   *                  discard pile).
   * @return          count of cards of specified color in specified pile.
   */
  public int count(Color cardColor, Color pileColor) {
    List<Card> pile;
    switch (pileColor) {
      case RED:
        pile = redPile;
        break;
      case BLACK:
        pile = blackPile;
        break;
      default:
        pile = discardPile;
        break;
    }
    int count = 0;
    for (Card card : pile) {
      if (card.getSuit().getColor() == cardColor) {
        count++;
      }
    }
    return count;
  }

  /**
   * A simple {@link Comparator Comparator&lt;Card&gt;} that can be used to sort
   * (group) cards by color only.
   */
  private static class ColorComparator implements Comparator<Card> {

    @Override
    public int compare(Card card1, Card card2) {
      return card1.getSuit().getColor().compareTo(card2.getSuit().getColor());
    }

  }

}
