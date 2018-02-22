package io.tools.trellobacklogsaggregator.configuration;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProxyConfiguration {

    @Autowired
    private CustomConfiguration customConfiguration;

    @PostConstruct
    public void init() {
        Properties props = System.getProperties();
        if (!customConfiguration.getProxyHost().isEmpty()) {
            props.put("http.proxyHost", customConfiguration.getProxyHost());
            props.put("http.proxyPort", customConfiguration.getProxyPort());

            props.put("https.proxyHost", customConfiguration.getProxyHost());
            props.put("https.proxyPort", customConfiguration.getProxyPort());

            if (!customConfiguration.getProxyUser().isEmpty()) {
                final String authUser = customConfiguration.getProxyUser();
                final String authPassword = customConfiguration.getProxyPassword();
                Authenticator.setDefault(new Authenticator() {
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(authUser, authPassword.toCharArray());
                    }
                });

                props.put("jdk.http.auth.tunneling.disabledSchemes", "");

                props.put("http.proxyUser", authUser);
                props.put("http.proxyPassword", authPassword);
                props.put("https.proxyUser", authUser);
                props.put("https.proxyPassword", authPassword);
            }
        }

    }
}
