package io.tools.trellobacklogsaggregator.model;

import java.util.List;

public class BacklogsData {
    private List<BoardDetail> boards;
    private Sprint sprint;
    private List<BacklogError> errors;

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

    public List<BacklogError> getErrors() {
        return errors;
    }

    public void setErrors(List<BacklogError> errors) {
        this.errors = errors;
    }

}
