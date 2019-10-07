package io.tools.trellobacklogsaggregator.repository;

import io.tools.trellobacklogsaggregator.model.CardModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository  extends JpaRepository<CardModel, String> {
}
