package io.tools.trellobacklogsaggregator.model;

import java.util.List;

import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.Member;

public class CardWithMembers {
    private final Card card;
    private final List<Member> members;

    public CardWithMembers(Card card, List<Member> members) {
        super();
        this.card = card;
        this.members = members;
    }

    public Card getCard() {
        return card;
    }

    public List<Member> getMembers() {
        return members;
    }

}
