package io.tools.trellobacklogsaggregator.execptions;

public class BoardException extends RuntimeException {

    public BoardException() {
        super("No Board found");
    }
}
