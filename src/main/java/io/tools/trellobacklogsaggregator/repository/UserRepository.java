package io.tools.trellobacklogsaggregator.repository;

import io.tools.trellobacklogsaggregator.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserModel, Long> {

}
