package io.tools.trellobacklogsaggregator.repository;

import io.tools.trellobacklogsaggregator.model.CalendarModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<CalendarModel, Long> {
}
