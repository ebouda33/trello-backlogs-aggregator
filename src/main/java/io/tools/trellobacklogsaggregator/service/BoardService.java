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
        board.setBusinessComplexity(board.getBusinessComplexity() + cardService.getCardValue(card));
        board.setConsumedComplexity(board.getConsumedComplexity() + cardService.getComplexiteRealisee(card));
        board.setTotalComplexity(board.getTotalComplexity() + cardService.getComplexiteTotale(card));
        return board;
    }
}
