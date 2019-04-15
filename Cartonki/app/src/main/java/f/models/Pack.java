package f.models;

import java.util.ArrayList;

public class Pack {
    private int id;
    private ArrayList<Card> cards = new ArrayList<>();    //TODO: Set for random access

    public Pack() {
    }

    public Pack(int id) {
        this.id = id;
    }

    public Pack(int id, ArrayList<Card> cards) {
        this.id = id;
        this.cards = cards;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void add(Card card) {
        cards.add(card);
    }

    public void remove(int i) {
        cards.remove(i);
    }
}
