package io.tools.trellobacklogsaggregator.model;

import com.julienvey.trello.domain.Card;

import java.util.ArrayList;
import java.util.List;

public class ListByLabel {

    private String name;
    private List<Card> cardList;

    public ListByLabel(String name) {
        this.name = name;
        cardList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Card> getCardList() {
        return cardList;
    }

    public void addCard(Card card){
        cardList.add(card);
    }
}
