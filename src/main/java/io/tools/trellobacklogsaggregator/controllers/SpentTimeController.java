package io.tools.trellobacklogsaggregator.controllers;


import com.julienvey.trello.domain.Label;
import io.tools.trellobacklogsaggregator.model.*;
import io.tools.trellobacklogsaggregator.repository.BacklogsRepository;
import io.tools.trellobacklogsaggregator.service.BoardService;
import io.tools.trellobacklogsaggregator.service.CalendarService;
import io.tools.trellobacklogsaggregator.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/spentTime")
@Scope("session")
@SessionAttributes("spentTimer")
public class SpentTimeController extends AbstractController {


    private CalendarService calendarService;

    private BoardService boardService;

    @Autowired
    public SpentTimeController(CalendarService calendarService, BoardService boardService) {
        this.calendarService = calendarService;
        this.boardService = boardService;
    }

    @GetMapping("")
    public String index(HttpServletRequest request, Model model) {
        saveRequest(model, request, getCurrentMonth(request), getCurrentWeek(request), getCurrentMember(request));
        return "spenttime";
    }

    @GetMapping("/{currentMonth}")
    public String getMonth(HttpSession session , HttpServletRequest request, Model model, @PathVariable int currentMonth) {
        saveRequest(model, request, currentMonth, getCurrentWeek(request), getCurrentMember(request));

        return "spenttime";
    }

    @GetMapping("/{currentMonth}/{week}")
    public String getWeek(HttpServletRequest request, Model model, @PathVariable int currentMonth, @PathVariable int week) {

        saveRequest(model, request, currentMonth, week, getCurrentMember(request));

        return "spenttime";
    }

    @PostMapping(value = "/currentMember", consumes = "application/json")
    public String getMember(HttpSession session ,Model model, @RequestBody String member, HttpServletRequest request) {
        saveRequest(model, request, getCurrentMonth(request), getCurrentWeek(request), member);
        session.setAttribute("currentMember", member);
        return "spenttime";
    }


    private void saveRequest(Model model, HttpServletRequest request, int currentMonth, int week, String idMember) {
        request.getSession().setAttribute("currentMonth", currentMonth);
        request.getSession().setAttribute("currentWeek", week);
        request.getSession().setAttribute("currentMember", idMember);

        if (boardService.isReadableRepository()) {
            getModel(model, Optional.ofNullable(getCurrentMonth(request)), Optional.ofNullable(getCurrentWeek(request)), getCurrentMember(request));
            errorManagement(model);
        }

    }

    private Model getModel(Model model, Optional<Integer> month, Optional<Integer> week, String idMember) {
        if (boardService.isReadableRepository()) {
            BoardDetail boardDetail = boardService.getBoardDetail();
            List<Label> labelNames = boardService.getLabelPrincipal(Optional.empty());

            final List<MonthRest> listMonthsRest = UtilService.getListMonthsRest();


            final LocalDate now = LocalDate.now();
            MonthRest currentMonth = listMonthsRest.get(now.getMonthValue() - 1);

            if (month.isPresent()) {
                currentMonth = listMonthsRest.get(month.get() - 1);
            }
            WeekRest currentWeek = currentMonth.getWeekFor(now);
            if (week.isPresent()) {
                currentWeek = currentMonth.getWeekRestList().get(week.get());
            }


            model.addAttribute("currentMonth", currentMonth);
            model.addAttribute("currentWeek", currentWeek.getIndex());
            model.addAttribute("firstDayWeek", currentWeek.getFirstDay());
            model.addAttribute("indexFirstDayWeek", (currentWeek.getIndexFirstDay()-1));
            model.addAttribute("lastDayWeek", currentWeek.getLastDay());
            MonthRest finalCurrentMonth = currentMonth;
            WeekRest finalCurrentWeek = currentWeek;
            List<String> listDays = new ArrayList<String>() {{
                add(String.format("%02d", UtilService.getDaysOfWeek(finalCurrentMonth.getIndex(), finalCurrentWeek.getIndex(), 1)));
                add(String.format("%02d", UtilService.getDaysOfWeek(finalCurrentMonth.getIndex(), finalCurrentWeek.getIndex(), 2)));
                add(String.format("%02d", UtilService.getDaysOfWeek(finalCurrentMonth.getIndex(), finalCurrentWeek.getIndex(), 3)));
                add(String.format("%02d", UtilService.getDaysOfWeek(finalCurrentMonth.getIndex(), finalCurrentWeek.getIndex(), 4)));
                add(String.format("%02d", UtilService.getDaysOfWeek(finalCurrentMonth.getIndex(), finalCurrentWeek.getIndex(), 5)));
            }};

            model.addAttribute("listDays", listDays);
            model.addAttribute("months", listMonthsRest);
            model.addAttribute("labels", labelNames);
            model.addAttribute("id", boardDetail.getSource().getId());
            model.addAttribute("today", now);
            model.addAttribute("todayDay", now.format(DateTimeFormatter.ofPattern("dd")));
            model.addAttribute("members", calendarService.getMembers());
            model.addAttribute("currentMember", idMember);
            model.addAttribute("content", currentCalendar(currentMonth.getIndex(), currentWeek.getIndex(), idMember));

        }
        getContext(model);
        return model;
    }


    private List<CalendarModel> currentCalendar(int currentMonth, int currentWeek, String idMember) {
        return calendarService.getCalendarFor(currentMonth, currentWeek, idMember);
    }

    private int getCurrentMonth(HttpServletRequest request) {
        final LocalDate now = LocalDate.now();
        final Object object = request.getSession().getAttribute("currentMonth");
        return (int) (object == null ? now.getMonthValue() : object);

    }

    private int getCurrentWeek(HttpServletRequest request) {
        final LocalDate now = LocalDate.now();
        final int weekOfDay = UtilService.getWeekOfDay(now);
        final Object currentWeek = request.getSession().getAttribute("currentWeek");
        return (int) (currentWeek == null ? weekOfDay : currentWeek);

    }

    private String getCurrentMember(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("currentMember");
    }


}

