package io.tools.trellobacklogsaggregator.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface TimeByLabel {

    String getName();

    String getLabel();

    LocalDate getDate();

    Double getTotal();

}
