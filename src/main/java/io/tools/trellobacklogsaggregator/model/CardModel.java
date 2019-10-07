package io.tools.trellobacklogsaggregator.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "cards")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardModel implements Serializable {

    @Id
    private String id;

    @ManyToMany(mappedBy = "cards")
    private List<CalendarModel> calendars = new ArrayList<>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardModel)) return false;

        CardModel cardModel = (CardModel) o;

        return getId().equals(cardModel.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
