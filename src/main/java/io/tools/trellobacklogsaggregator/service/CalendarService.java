package io.tools.trellobacklogsaggregator.service;

import io.tools.trellobacklogsaggregator.model.CalendarModel;
import io.tools.trellobacklogsaggregator.repository.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarService {

    private CalendarRepository calendarRepository;

    @Autowired
    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    public CalendarModel save(CalendarModel calendar){
        return calendarRepository.save(calendar);
    }

    public List<CalendarModel> saveAll(List<CalendarModel> calendarsList){
        calendarsList.stream().forEach(calendarModel -> calendarRepository.save(calendarModel));
        return calendarsList;
    }
}
