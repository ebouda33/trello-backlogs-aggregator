package io.tools.trellobacklogsaggregator.model;

public class BacklogError {
    private final String backlog;
    private final String message;

    public BacklogError(String backlog, String message) {
        this.backlog = backlog;
        this.message = message;
    }

    public String getBacklog() {
        return backlog;
    }

    public String getMessage() {
        return message;
    }

}
