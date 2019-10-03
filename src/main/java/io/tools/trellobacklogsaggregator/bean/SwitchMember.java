package io.tools.trellobacklogsaggregator.bean;

public class SwitchMember {
    private String id;
    private String board;
    private boolean status;

    public SwitchMember() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
