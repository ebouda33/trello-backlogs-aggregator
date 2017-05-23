package io.tools.trellobacklogsaggregator.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.julienvey.trello.domain.Argument;
import com.julienvey.trello.domain.Board;

import io.tools.trellobacklogsaggregator.model.BacklogsData;

@Service
public class TrelloService {

    @Autowired
    private TrelloApi trelloApi;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public BacklogsData readBacklogs(String organizationId) {
        Argument fields = new Argument("fields", "name");
        List<Board> boards = trelloApi.getOrganizationBoards(organizationId, fields);
        List<Board> storiesBoard = new ArrayList<>();
        boards.stream().filter(board -> board.getName().startsWith("Backlog")).forEach(board -> {
            storiesBoard.add(board);
            logger.debug(board.getName());
        });

        BacklogsData backlogsData = new BacklogsData();
        backlogsData.setBoards(storiesBoard);
        return backlogsData;
    }
}
