package io.tools.trellobacklogsaggregator.controllers;

import io.tools.trellobacklogsaggregator.batch.BacklogsReaderBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executor;

@RestController
@RequestMapping("/batch")
public class BatchController {

    @Autowired
    private BacklogsReaderBatch backlogsReaderBatch;


    @GetMapping("/start")
    public ResponseEntity run() {
        String message = "not possible : running batch";
        if (!backlogsReaderBatch.isRunning()) {
            backlogsReaderBatch.execute();
            message = "batch started";
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/status")
    public ResponseEntity isRunning() {
        String message = "Batch never started";
        if (backlogsReaderBatch.getBatchDate() != null) {
            message = "Batch " + (backlogsReaderBatch.isRunning() ? " running" : " stopped") + " from " + backlogsReaderBatch.getBatchDate();
            message += (!backlogsReaderBatch.isRunning() ? " during " + backlogsReaderBatch.getDuration() : "");
        }
        return ResponseEntity.ok(message);
    }
}
