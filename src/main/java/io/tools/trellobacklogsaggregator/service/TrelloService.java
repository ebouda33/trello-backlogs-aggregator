package io.tools.trellobacklogsaggregator.service;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.julienvey.trello.domain.Argument;
import com.julienvey.trello.domain.Board;
import com.julienvey.trello.domain.Label;
import com.julienvey.trello.domain.Member;
import com.julienvey.trello.domain.TList;

import io.tools.trellobacklogsaggregator.configuration.CustomConfiguration;
import io.tools.trellobacklogsaggregator.model.BacklogError;
import io.tools.trellobacklogsaggregator.model.BacklogsData;
import io.tools.trellobacklogsaggregator.model.BoardDetail;
import io.tools.trellobacklogsaggregator.model.CardWithMembers;
import io.tools.trellobacklogsaggregator.model.Sprint;

@Service
public class TrelloService {

    private TrelloApi trelloApi;

    private BoardService boardService;

    private ListService listService;

    private SprintService sprintService;

    private CardService cardService;

    private CustomConfiguration customConfiguration;

    @Autowired
    public TrelloService(TrelloApi trelloApi, BoardService boardService, ListService listService, SprintService sprintService, CardService cardService, CustomConfiguration customConfiguration) {
        this.trelloApi = trelloApi;
        this.boardService = boardService;
        this.listService = listService;
        this.sprintService = sprintService;
        this.cardService = cardService;
        this.customConfiguration = customConfiguration;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private BoardDetail detailedBoard;
    private Sprint sprint;

    public BacklogsData readBacklogs(String organizationId) {
        List<Board> storiesBoards = getBoards(organizationId);
        Map<String, Member> members = getMembers(organizationId);
        List<CardWithMembers> cardsWithMembersReadyToDeliver = new ArrayList<>();
        List<BacklogError> errors = new ArrayList<>();

        List<BoardDetail> storiesDetailedBoards = new ArrayList<>();
        sprint = new Sprint();
        int i = 0;

        for (Board board : storiesBoards) {
            List<TList> tLists = board.fetchLists();
            List<Label> boardLabels = trelloApi.getBoardLabels(board.getId());
//            final Map<String, String> map = boardLabels.stream().collect(Collectors.toMap(Label::getColor, Label::getName));
//            board.setLabelNames(map);
            final List<Label> boardCardTypeLabels = boardService.getLabelPrincipal(Optional.of(boardLabels));
//

            detailedBoard = new BoardDetail(board);
            detailedBoard.setListLabels(boardLabels);
            boardLabels.stream().forEach(label -> detailedBoard.createList(label.getName()));
            try {
                listService.checkListConsistency(tLists);
            } catch (Exception e) {
                errors.add(new BacklogError(board.getName(), e.getMessage()));
            }
            tLists.forEach(tList -> {
                // ajouter les lists en fonction des labels contenant des cartes
                String listName = tList.getName();
                if (listService.checkListAllowed(listName, customConfiguration.getColumnInSprintAllowed())) {
                    sprint = sprintService.addColumn(sprint, listName, boardCardTypeLabels);

                }
                trelloApi.getListCards(tList.getId()).forEach(card -> {
                    detailedBoard = boardService.addCard(detailedBoard, card);

                    if (listService.checkListAllowed(listName, customConfiguration.getColumnInSprintAllowed())) {
                        sprint = sprintService.addCard(sprint, listName, card, members, board.getName());
                    } else if (listService.checkListAllowed(listName, customConfiguration.getColumnDeliveredAllAllowed())) {
                        sprint = sprintService.addCard(sprint, customConfiguration.getTrelloColumnDeliveredTotal(), card, members, board.getName());
                    }
                    if (listService.checkListAllowed(listName, customConfiguration.getColumnReadyToDeliverAllowed())) {
                        cardsWithMembersReadyToDeliver.add(cardService.createCardWithMembers(card, members, board.getName()));
                    }
                });
            });

            storiesDetailedBoards.add(detailedBoard);
            logger.debug(++i + "/" + storiesBoards.size() + " (" + board.getName() + " OK)");
        }

        BacklogsData backlogsData = new BacklogsData();
        backlogsData.setBoards(storiesDetailedBoards);
        backlogsData.setSprint(sprint);
        backlogsData.setCardsWithMembersReadyToDeliver(cardsWithMembersReadyToDeliver);
        backlogsData.setErrors(errors);


        return backlogsData;
    }

    public Map<String, Member> getMembers(String organizationId) {
        List<Member> membersList = trelloApi.getOrganizationMembers(organizationId);
        Map<String, Member> members = new HashMap<>();
        membersList.forEach(member -> {
            members.put(member.getId(), member);
        });
        return members;
    }

    private List<Board> getBoards(String organizationId) {
        Argument fields = new Argument("fields", "name");
        List<Board> boards = trelloApi.getOrganizationBoards(organizationId, fields);
        List<Board> storiesBoards = new ArrayList<>();
        boards.stream().filter(board -> board.getName().matches(customConfiguration.getBoardsPattern()))
                .forEach(board -> {
                    storiesBoards.add(board);
                });
        return storiesBoards;
    }

    public List<Member> getMembersForBoard(String boardID){
        return trelloApi.getBoardMembers(boardID);
    }

}
