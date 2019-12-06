package io.tools.trellobacklogsaggregator.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.persistence.Column;
import java.io.Serializable;

@Entity(name = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserModel implements Serializable {

    private String name;

    @Id
    @JsonProperty(value = "id")
    private String identifiant_trello;

    @Column(columnDefinition = "boolean default false")
    private boolean actif;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserModel)) return false;

        UserModel userModel = (UserModel) o;

        if (getName() != null ? !getName().equals(userModel.getName()) : userModel.getName() != null) return false;
        return getIdentifiant_trello().equals(userModel.getIdentifiant_trello());
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + getIdentifiant_trello().hashCode();
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifiant_trello() {
        return identifiant_trello;
    }

    public void setIdentifiant_trello(String identifiant_trello) {
        this.identifiant_trello = identifiant_trello;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }
}
