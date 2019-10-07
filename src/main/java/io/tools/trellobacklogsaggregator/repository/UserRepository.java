package io.tools.trellobacklogsaggregator.repository;

import io.tools.trellobacklogsaggregator.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {

}
