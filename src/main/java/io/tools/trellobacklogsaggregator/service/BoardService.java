package io.tools.trellobacklogsaggregator.service;

import com.julienvey.trello.domain.Board;
import io.tools.trellobacklogsaggregator.configuration.CustomConfiguration;
import io.tools.trellobacklogsaggregator.repository.BacklogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.julienvey.trello.domain.Card;

import io.tools.trellobacklogsaggregator.model.BoardDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BoardService {


    private CardService cardService;

    private BacklogsRepository backlogsRepository;

    private CustomConfiguration customConfiguration;

    private Map<String,String> labelPrincipal;

    @Autowired
    public BoardService( BacklogsRepository backlogsRepository,CardService cardService, CustomConfiguration customConfiguration) {
        this.backlogsRepository = backlogsRepository;
        this.cardService = cardService;
        this.customConfiguration = customConfiguration;
    }

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

    public Map<String, String> getLabelPrincipal() {
        if(labelPrincipal == null) {
            BoardDetail boardDetail = getBoardDetail();
            Map<String, String> labelNames = boardDetail.getSource().getLabelNames();
            List<String> labelsSecondaires = customConfiguration.getTrelloLabelsSecondaire();
            labelPrincipal = labelNames.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            labelNames.forEach((key, value) -> {
                labelsSecondaires.forEach(labelSecondaire -> {
                    if (value.equalsIgnoreCase(labelSecondaire)) {
                        labelPrincipal.remove(key);
                    }
                });
            });
        }

        return labelPrincipal;
    }

    public BoardDetail getBoardDetail(){
        return backlogsRepository.read().getBoards().get(0);

    }

    public boolean isReadableRepository(){
        return  backlogsRepository.read() != null;
    }
}
