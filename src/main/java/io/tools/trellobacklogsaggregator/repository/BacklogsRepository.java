package io.tools.trellobacklogsaggregator.repository;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import io.tools.trellobacklogsaggregator.model.BacklogsData;

@Repository
public class BacklogsRepository {
    private BacklogsData backlogsData;

    public void save(BacklogsData backlogsData) {
        this.backlogsData = backlogsData;
    }

    public BacklogsData read() {
        return backlogsData;
    }
}
