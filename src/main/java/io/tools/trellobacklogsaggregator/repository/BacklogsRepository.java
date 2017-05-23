package io.tools.trellobacklogsaggregator.repository;

import org.springframework.stereotype.Service;

import io.tools.trellobacklogsaggregator.model.BacklogsData;

@Service
public class BacklogsRepository {
    private BacklogsData backlogsData;

    public void save(BacklogsData backlogsData) {
        this.backlogsData = backlogsData;
    }

    public BacklogsData read() {
        return backlogsData;
    }
}
