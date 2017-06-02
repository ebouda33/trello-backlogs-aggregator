package io.tools.trellobacklogsaggregator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.julienvey.trello.domain.Card;

import io.tools.trellobacklogsaggregator.model.BoardDetail;

@Service
public class BoardService {

    @Autowired
    private CardService cardService;

    public BoardDetail addCard(BoardDetail board, Card card, List<String> releases) {
        board.setBusinessComplexity(board.getBusinessComplexity() + cardService.getBusinessComplexity(card));
        board.setConsumedComplexity(board.getConsumedComplexity() + cardService.getConsumedComplexity(card));
        board.setTotalComplexity(board.getTotalComplexity() + cardService.getTotalComplexity(card));
        board.setRemainedComplexity(board.getTotalComplexity() - board.getConsumedComplexity());

        card.getLabels().stream().filter(label -> releases.contains(label.getName())).forEach(label -> {
            String release = label.getName();
            Double releaseBusinessComplexity = board.getReleaseBusinessComplexity().get(release);
        });

        return board;
    }
}
