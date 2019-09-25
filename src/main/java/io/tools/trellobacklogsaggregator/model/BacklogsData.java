package io.tools.trellobacklogsaggregator.model;

import io.tools.trellobacklogsaggregator.execptions.BoardException;

import java.util.List;
import java.util.Optional;

public class BacklogsData {
    private List<BoardDetail> boards;
    private Sprint sprint;
    private List<CardWithMembers> cardsWithMembersReadyToDeliver;
    private List<BacklogError> errors;

    public List<BoardDetail> getBoards() {
        return boards;
    }

    public BoardDetail getBoard(String id) {
        final Optional<BoardDetail> first = boards.stream().filter(board -> board.getSource().getId().equalsIgnoreCase(id)).findFirst();
        return first.orElseThrow(BoardException::new);
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
