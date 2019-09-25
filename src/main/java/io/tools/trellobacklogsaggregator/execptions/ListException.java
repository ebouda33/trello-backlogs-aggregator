package io.tools.trellobacklogsaggregator.execptions;

public class ListException extends RuntimeException {

    public ListException() {
        super("NO List found");
    }
}
