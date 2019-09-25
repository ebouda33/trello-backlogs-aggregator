package io.tools.trellobacklogsaggregator.controllers;


import io.tools.trellobacklogsaggregator.model.BoardDetail;
import io.tools.trellobacklogsaggregator.model.MonthRest;
import io.tools.trellobacklogsaggregator.model.WeekRest;
import io.tools.trellobacklogsaggregator.repository.BacklogsRepository;
import io.tools.trellobacklogsaggregator.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/spentTime")
public class SpentTimeController extends AbstractController {

    @Autowired
    private BacklogsRepository backlogsRepository;

    @RequestMapping("")
    public String index(Model model) {
        if (backlogsRepository.read() != null) {
            final LocalDate now = LocalDate.now();
            getModel(model, now.getMonthValue(),UtilService.getWeekOfDay(now));
            errorManagement(model);
        }

        return "spenttime";
    }

    @RequestMapping("/{currentMonth}")
    public String getMonth(Model model, @PathVariable int currentMonth) {
        if (backlogsRepository.read() != null) {
            getModel(model, currentMonth, 1);
            errorManagement(model);
        }

        return "spenttime";
    }

    @RequestMapping("/{currentMonth}/{week}")
    public String getWeek(Model model, @PathVariable int currentMonth, @PathVariable int week) {
        if (backlogsRepository.read() != null) {
            getModel(model, currentMonth, week);
            errorManagement(model);
        }

        return "spenttime";
    }


    private Model getModel(Model model, int currentMonth, int week) {
        BoardDetail boardDetail = backlogsRepository.read().getBoards().get(0);
        Map<String, String> labelNames = boardDetail.getSource().getLabelNames();
        final List<MonthRest> listMonthsRest = UtilService.getListMonthsRest();
        final WeekRest currentWeek = listMonthsRest.get(currentMonth - 1).getWeekRestList().get(week);

        model.addAttribute("currentMonth", listMonthsRest.get(currentMonth - 1));
        model.addAttribute("currentWeek", week);
        model.addAttribute("firstDayWeek", currentWeek.getFirstDay());
        model.addAttribute("lastDayWeek", currentWeek.getLastDay());
        List<String> listDays = new ArrayList<String>(){{
            add(String.format("%02d", UtilService.getDaysOfWeek(currentMonth, week, 1)));
            add(String.format("%02d", UtilService.getDaysOfWeek(currentMonth, week, 2)));
            add(String.format("%02d", UtilService.getDaysOfWeek(currentMonth, week, 3)));
            add(String.format("%02d", UtilService.getDaysOfWeek(currentMonth, week, 4)));
            add(String.format("%02d", UtilService.getDaysOfWeek(currentMonth, week, 5)));
        }};

        model.addAttribute("listDays", listDays);
        model.addAttribute("months", listMonthsRest);
        model.addAttribute("labels", labelNames);
        model.addAttribute("id", boardDetail.getSource().getId());
        model.addAttribute("today", String.format("%02d",LocalDate.now().getDayOfMonth()));

        return model;
    }

}
