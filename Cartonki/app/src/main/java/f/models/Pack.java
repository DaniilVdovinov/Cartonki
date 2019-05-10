package f.models;

import java.util.ArrayList;

public class Pack {
    private Long id;
    private String name;
    private ArrayList<Card> cards = new ArrayList<>();    //TODO: Set for random access


    public Pack(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Pack(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pack(String name) {
        this.name = name;
    }

    public Pack(Long id, ArrayList<Card> cards) {
        this.id = id;
        this.cards = cards;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
