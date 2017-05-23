package io.tools.trellobacklogsaggregator.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config.properties")
public class CustomConfiguration {

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
    
    
}
