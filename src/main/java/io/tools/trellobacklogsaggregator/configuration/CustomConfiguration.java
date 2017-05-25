package io.tools.trellobacklogsaggregator.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config.properties")
public class CustomConfiguration {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${proxy.host}")
    private String proxyHost;

    @Value("${proxy.port}")
    private String proxyPort;

    @Value("${proxy.user}")
    private String proxyUser;

    @Value("${proxy.password}")
    private String proxyPassword;

    @Value("${trello.organisation.id}")
    private String organisationId;

    @Value("${trello.application.key}")
    private String applicationKey;

    @Value("${trello.access.token}")
    private String accessToken;

    @Value("${trello.column.allowed}")
    private String trelloColumnAllowed;

    @Value("${trello.column.sprint}")
    private String trelloColumnInSprint;

    private List<String> columnAllowed;
    private List<String> columnInSprintAllowed;

    @PostConstruct
    private void initColumns() {
        columnAllowed = parseColumnsField(trelloColumnAllowed);
        columnInSprintAllowed = parseColumnsField(trelloColumnInSprint);

        for (String column : columnInSprintAllowed) {
            if (!columnAllowed.contains(column)) {
                logger.error(column + " is not allowed by the configuration");
            }
        }
    }

    private List<String> parseColumnsField(String columnToParse) {
        String[] columnArray = columnToParse.split(",");
        List<String> columnList = Arrays.asList(columnArray);
        List<String> columnToReturn = new ArrayList<String>();
        columnList.forEach(column -> {
            columnToReturn.add(column.trim().toLowerCase());
        });
        return columnToReturn;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public String getOrganisationId() {
        return organisationId;
    }

    public String getApplicationKey() {
        return applicationKey;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public List<String> getColumnAllowed() {
        return columnAllowed;
    }

    public List<String> getColumnInSprintAllowed() {
        return columnInSprintAllowed;
    }

}
