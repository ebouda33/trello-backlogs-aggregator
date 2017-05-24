package io.tools.trellobacklogsaggregator.model;

import java.util.ArrayList;
import java.util.List;

import com.julienvey.trello.domain.Card;

public class Column {
    private String name;
    private List<Card> cards = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

}
