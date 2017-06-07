package io.tools.trellobacklogsaggregator.model;

import java.util.List;

public class BacklogsData {
    private List<BoardDetail> boards;
    private Sprint sprint;
    private List<CardWithMembers> cardsWithMembersReadyToDeliver;
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

    public List<CardWithMembers> getCardsWithMembersReadyToDeliver() {
        return cardsWithMembersReadyToDeliver;
    }

    public void setCardsWithMembersReadyToDeliver(List<CardWithMembers> cardsWithMembersReadyToDeliver) {
        this.cardsWithMembersReadyToDeliver = cardsWithMembersReadyToDeliver;
    }

    public List<BacklogError> getErrors() {
        return errors;
    }

    public void setErrors(List<BacklogError> errors) {
        this.errors = errors;
    }

}
