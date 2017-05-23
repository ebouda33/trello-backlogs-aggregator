package io.tools.trellobacklogsaggregator.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import io.tools.trellobacklogsaggregator.configuration.CustomConfiguration;
import io.tools.trellobacklogsaggregator.service.TrelloService;

@Configuration
@EnableScheduling
public class BacklogsReaderBatch {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired 
    private TrelloService trelloService;
    
    @Autowired 
    private CustomConfiguration customConfiguration;
    
    @Scheduled(cron = "*/10 * * * * *")
    public void execute() {
        logger.debug("BacklogsReaderBatch starting");
        trelloService.readBacklogs(customConfiguration.getOrganisationId());
        logger.debug("BacklogsReaderBatch done");
    }
}
