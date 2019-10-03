package io.tools.trellobacklogsaggregator.controllers;

import com.julienvey.trello.domain.Member;
import io.tools.trellobacklogsaggregator.bean.SwitchMember;
import io.tools.trellobacklogsaggregator.execptions.NumericException;
import io.tools.trellobacklogsaggregator.model.BoardDetail;
import io.tools.trellobacklogsaggregator.model.MonthRest;
import io.tools.trellobacklogsaggregator.model.UserModel;
import io.tools.trellobacklogsaggregator.model.WeekRest;
import io.tools.trellobacklogsaggregator.repository.BacklogsRepository;
import io.tools.trellobacklogsaggregator.service.CalendarService;
import io.tools.trellobacklogsaggregator.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/administration")
public class AdministrationController {
    @Autowired
    private BacklogsRepository backlogsRepository;

    @Autowired
    private CalendarService calendarService;


    final List<MonthRest> listMonthsRest = UtilService.getListMonthsRest();

    @GetMapping("")
    public String index(Model model) {
        getModel(model, Optional.empty(), Optional.empty());
        return "administration";
    }

    @GetMapping("/{month}")
    public String index(Model model,@Valid @PathVariable int month)  {
        validMont(month);
        getModel(model, Optional.of(month), Optional.empty());
        return "administration";
    }

    @GetMapping("/{month}/{week}")
    public String index(Model model,@PathVariable int month,@PathVariable int week)  {
        validMont(month);
        validWeek(week,month);
        getModel(model, Optional.of(month), Optional.of(week));
        return "administration";
    }


    @PutMapping(value = "", produces = "application/json")
    public @ResponseBody
    ResponseEntity updateMember(@RequestBody SwitchMember payload) {

        calendarService.setMember(payload.getId(), payload.isStatus(), payload.getBoard());


        return ResponseEntity.ok("{\"update\":\"member\"}");

    }

    private void getModel(Model model, Optional<Integer> month, Optional<Integer> week) {
        BoardDetail boardDetail = backlogsRepository.read().getBoards().get(0);
        final String id = boardDetail.getSource().getId();
        final Map<Member, Boolean> members = calendarService.getMembersForScreen(id);
        final List<UserModel> users = calendarService.getMembers();

        final LocalDate now = LocalDate.now();
        MonthRest currentMonth = listMonthsRest.get(now.getMonthValue() - 1);

        if (month.isPresent()) {
            currentMonth = listMonthsRest.get(month.get() - 1);
        }
        WeekRest currentWeek = currentMonth.getWeekFor(now);
        if (week.isPresent()) {
            currentWeek = currentMonth.getWeekRestList().get(week.get());
        }

        Map<String, String> labelNames = boardDetail.getSource().getLabelNames();
        final int pMonth = currentMonth.getIndex();
        final int pWeek = currentWeek.getIndex();

        List<String> listDays = new ArrayList<String>() {{
            add(String.format("%02d", UtilService.getDaysOfWeek(pMonth, pWeek, 1)));
            add(String.format("%02d", UtilService.getDaysOfWeek(pMonth, pWeek, 2)));
            add(String.format("%02d", UtilService.getDaysOfWeek(pMonth, pWeek, 3)));
            add(String.format("%02d", UtilService.getDaysOfWeek(pMonth, pWeek, 4)));
            add(String.format("%02d", UtilService.getDaysOfWeek(pMonth, pWeek, 5)));
        }};

        model.addAttribute("months", listMonthsRest);
        model.addAttribute("currentMonth", currentMonth);
        model.addAttribute("currentWeek", currentWeek.getIndex());
        model.addAttribute("boardID", id);
        model.addAttribute("boardName", boardDetail.getName());
        model.addAttribute("members", members);
        model.addAttribute("users", users);
        model.addAttribute("labelTrello", labelNames);
        model.addAttribute("listDays", listDays);
        model.addAttribute("today", now);
        model.addAttribute("todayDay", now.format(DateTimeFormatter.ofPattern("dd")));

        model.addAttribute("stats", calendarService.getStatTimeByLabel(currentWeek.getLocalFirstDay(), currentWeek.getLocalLastDay()));
        model.addAttribute("statsByDay", calendarService.getStatTimeByUserAndDate(currentWeek.getLocalFirstDay(), currentWeek.getLocalLastDay()));

    }



    private void validMont(int month){
        if(month <1 || month > 12){
            throw new NumericException.MonthValueException();
        }
    }

    private void validWeek(int week,int month){
        final int size = listMonthsRest.get(month - 1).getWeekRestList().size();
        if(week <0 || week > size - 1){
            throw new NumericException.WeekValueException(size-1);
        }
    }
}
