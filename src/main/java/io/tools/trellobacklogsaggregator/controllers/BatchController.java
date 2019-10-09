package io.tools.trellobacklogsaggregator.controllers;

import io.tools.trellobacklogsaggregator.batch.BacklogsReaderBatch;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@RestController
@RequestMapping("/batch")
public class BatchController {

    @Autowired
    private BacklogsReaderBatch backlogsReaderBatch;


    @GetMapping("/start")
    public ResponseEntity run() {
        String message = "not possible : running batch";
        boolean start = false;
        final Map<String, Object> sortie = new HashMap<>();
        if (!backlogsReaderBatch.isRunning()) {
            backlogsReaderBatch.execute();
            message = "batch started";
            start = true;
        }
        sortie.putIfAbsent("start", start);
        sortie.putIfAbsent("message", message);
        return ResponseEntity.ok(sortie);
    }

    @GetMapping("/status")
    public ResponseEntity isRunning() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/y HH:mm:s").withZone(DateTimeZone.forID("Europe/Paris"));
        String message = "Batch never started";
        boolean start = false;
        final Map<String, Object> sortie = new HashMap<>();
        if (backlogsReaderBatch.getBatchDate() != null) {
            if (backlogsReaderBatch.isRunning()) {
                message = "Batch running from " + fmt.print(backlogsReaderBatch.getBatchDate());
                start = true;

            } else {
                message = "Batch  stopped from " + fmt.print(backlogsReaderBatch.getBatchDate());
                message += " during " + backlogsReaderBatch.getDuration().toString();
            }
        }
        sortie.putIfAbsent("running", start);
        sortie.putIfAbsent("message", message);

        return ResponseEntity.ok(sortie);
    }
}
