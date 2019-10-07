package io.tools.trellobacklogsaggregator.controllers;

import com.julienvey.trello.domain.Member;
import io.tools.trellobacklogsaggregator.model.CalendarModel;
import io.tools.trellobacklogsaggregator.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;


    @PostMapping("/saves/{boardId}")
    public List<CalendarModel> save(@PathVariable @NotNull String boardId,@RequestBody @Valid List<CalendarModel> body) {
        calendarService.setBoardId(boardId);
        return calendarService.saveAll(body);
//        return body;
    }

    @GetMapping("/members/{boardId}")
    public List<Member> listAll(@PathVariable @NotNull String boardId){
        return calendarService.getMembersFromBoard(boardId);
    }

}
