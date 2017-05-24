package io.tools.trellobacklogsaggregator.model;

import java.util.List;

public class BacklogsData {
    private List<BoardDetail> boards;
    private Sprint sprint;

    public List<BoardDetail> getBoards() {
        return boards;
    }

    public void setBoards(List<BoardDetail> boards) {
        this.boards = boards;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

}
