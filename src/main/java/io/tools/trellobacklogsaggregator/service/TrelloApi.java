package io.tools.trellobacklogsaggregator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.julienvey.trello.impl.TrelloImpl;

@Service
public class TrelloApi extends TrelloImpl {

    public TrelloApi(@Value("${trello.application.key}") String applicationKey, @Value("${trello.access.token}") String accessToken) {
        super(applicationKey, accessToken);
    }

}
