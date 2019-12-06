package io.tools.trellobacklogsaggregator.repository;

import io.tools.trellobacklogsaggregator.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {

    public List<UserModel> findAllByActif(boolean actif);

}
