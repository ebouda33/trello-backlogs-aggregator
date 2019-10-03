package io.tools.trellobacklogsaggregator.repository;

import io.tools.trellobacklogsaggregator.bean.TimeByLabel;
import io.tools.trellobacklogsaggregator.model.CalendarModel;
import io.tools.trellobacklogsaggregator.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<CalendarModel, Long> {

    List<CalendarModel> findAllByDateIsBetweenAndUser(LocalDate debut, LocalDate fin, UserModel member);

    @Query(value = "SELECT users.nAME as name , LABEL_TRELLO as label, sum(time)as total \n" +
            "FROM CALENDAR \n" +
            "inner join users on users.IDENTIFIANT_TRELLO  = calENDAR.USER_ID \n" +
            "where date >= :debut and date <= :fin \n"+
            "group by LABEL_TRELLO, user_id ", nativeQuery = true)
    List<TimeByLabel> getStatTimeByLabel(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);

    @Query(value = "select u.name, c.date, sum(time) as total from \n" +
            " calendar c inner join users u on u.identifiant_trello = c.user_id\n" +
            " where date >= :debut and date <= :fin \n" +
            " group by c.user_id, c.date", nativeQuery = true)
    List<TimeByLabel> getStatTimeByUserAndDate(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);
}
