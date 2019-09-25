package io.tools.trellobacklogsaggregator.model;

import javax.persistence.*;
import javax.persistence.Column;
import java.io.Serializable;

@Entity(name = "users")
public class UserModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String identifiant_trello;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserModel)) return false;

        UserModel userModel = (UserModel) o;

        if (getId() != null ? !getId().equals(userModel.getId()) : userModel.getId() != null) return false;
        if (getName() != null ? !getName().equals(userModel.getName()) : userModel.getName() != null) return false;
        return getIdentifiant_trello() != null ? getIdentifiant_trello().equals(userModel.getIdentifiant_trello()) : userModel.getIdentifiant_trello() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getIdentifiant_trello() != null ? getIdentifiant_trello().hashCode() : 0);
        return result;
    }
}
