package io.tools.trellobacklogsaggregator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.julienvey.trello.domain.Card;

import io.tools.trellobacklogsaggregator.model.BoardDetail;

@Service
public class BoardService {

    @Autowired
    private CardService cardService;

    public BoardDetail addCard(BoardDetail board, Card card) {
        board.setBusinessComplexity(board.getBusinessComplexity() + cardService.getBusinessComplexity(card));
        board.setConsumedComplexity(board.getConsumedComplexity() + cardService.getConsumedComplexity(card));
        board.setTotalComplexity(board.getTotalComplexity() + cardService.getTotalComplexity(card));
        board.setRemainedComplexity(board.getTotalComplexity() - board.getConsumedComplexity());
        addCardByLabel(board,card);
        return board;
    }

    public void addCardByLabel(BoardDetail detail, Card card) {
        card.getLabels().forEach(label ->
            detail.addCard(card, label.getName())
        );
    }
}
