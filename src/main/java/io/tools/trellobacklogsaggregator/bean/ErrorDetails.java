package io.tools.trellobacklogsaggregator.bean;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

public class ErrorDetails {
        private Date timestamp;
        private String message;
        private String details;
        private Set<String> multiMessage;

        public ErrorDetails(Date timestamp, String message, String details, Set<String> multiple) {
            this.timestamp = timestamp;
            this.message = message;
            this.details = details;
            this.multiMessage = multiple;
        }

    public ErrorDetails(Date timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }


    public Set<String> getMultiMessage() {
        return multiMessage;
    }
}
