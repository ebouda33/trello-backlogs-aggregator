package io.tools.trellobacklogsaggregator.service;

import com.julienvey.trello.domain.Label;
import io.tools.trellobacklogsaggregator.configuration.CustomConfiguration;
import io.tools.trellobacklogsaggregator.repository.BacklogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.julienvey.trello.domain.Card;

import io.tools.trellobacklogsaggregator.model.BoardDetail;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class BoardService {


    private CardService cardService;

    private BacklogsRepository backlogsRepository;

    private CustomConfiguration customConfiguration;

    private List<Label> labelPrincipal;

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

    public List<Label> getLabelPrincipal(Optional<List<Label>> labelsTrello) {
        if(labelPrincipal == null) {
//            Map<String, String> labelNames = boardDetail.getSource().getLabelNames();
            List<Label> listLabels ;
            if(labelsTrello.isPresent()){
                listLabels = labelsTrello.get();
            }else {
                BoardDetail boardDetail = getBoardDetail();
                listLabels = boardDetail.getListLabels();
            }
            List<String> labelsSecondaires = customConfiguration.getTrelloLabelsSecondaire();

            labelPrincipal = listLabels.stream()
                    .filter(label -> !labelsSecondaires.contains(label.getName().toLowerCase()))
                    .collect(Collectors.toList());

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
