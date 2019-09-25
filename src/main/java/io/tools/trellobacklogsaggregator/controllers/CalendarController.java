package io.tools.trellobacklogsaggregator.controllers;

import io.tools.trellobacklogsaggregator.model.CalendarModel;
import io.tools.trellobacklogsaggregator.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @PostMapping("/save")
    public CalendarModel save(@RequestBody CalendarModel body) {
        return calendarService.save(body);
    }

    @PostMapping("/saves")
    public List<CalendarModel> save(@RequestBody List<CalendarModel> body) {
//        return calendarService.saveAll(body);
        return body;
    }
}
