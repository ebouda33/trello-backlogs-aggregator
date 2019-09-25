package io.tools.trellobacklogsaggregator.batch;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import io.tools.trellobacklogsaggregator.configuration.CustomConfiguration;
import io.tools.trellobacklogsaggregator.repository.BacklogsRepository;
import io.tools.trellobacklogsaggregator.service.TrelloService;


@Configuration
@EnableScheduling
public class BacklogsReaderBatch {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean isRunning = false;
    private Instant batchDate;
    private Duration duration = Duration.ZERO;

    @Autowired
    private TrelloService trelloService;

    @Autowired
    private CustomConfiguration customConfiguration;

    @Autowired
    private BacklogsRepository backlogsRepository;

    public boolean isRunning() {
        return isRunning;
    }

    public Duration getDuration() {
        return duration;
    }

    public Instant getBatchDate() {
        return batchDate;
    }

    //    @Scheduled(cron = "${batch.frequency}")
    @Scheduled(fixedRateString = "${batch.delay}", initialDelay = 5000)
    @Async
    public void execute() {
        isRunning = true;
        batchDate = Instant.now();
        logger.debug("BacklogsReaderBatch starting");
        backlogsRepository.save(trelloService.readBacklogs(customConfiguration.getOrganisationId()));
        isRunning = false;
        Instant batchDateEnd = Instant.now();
        duration = new Duration(batchDate, batchDateEnd);
        batchDate = batchDateEnd;
        logger.debug("BacklogsReaderBatch done in"+ duration);

    }
}
