package io.tools.trellobacklogsaggregator.model;

import java.util.List;

import com.julienvey.trello.domain.Board;

public class BacklogsData {
    private List<Board> boards;

    public List<Board> getBoards() {
        return boards;
    }

    public void setBoards(List<Board> boards) {
        this.boards = boards;
    }

}
