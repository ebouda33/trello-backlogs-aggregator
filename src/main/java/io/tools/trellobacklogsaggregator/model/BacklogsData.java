package io.tools.trellobacklogsaggregator.model;

import java.util.List;

public class BacklogsData {
    private List<BoardDetail> boards;

    public List<BoardDetail> getBoards() {
        return boards;
    }

    public void setBoards(List<BoardDetail> boards) {
        this.boards = boards;
    }

}
