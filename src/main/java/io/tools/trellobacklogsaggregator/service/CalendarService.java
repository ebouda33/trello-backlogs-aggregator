package io.tools.trellobacklogsaggregator.service;

import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.Member;
import io.tools.trellobacklogsaggregator.bean.TimeByLabel;
import io.tools.trellobacklogsaggregator.model.*;
import io.tools.trellobacklogsaggregator.repository.CalendarRepository;
import io.tools.trellobacklogsaggregator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CalendarService {

    private String boardId;
    private CalendarRepository calendarRepository;
    private TrelloApi trelloApi;
    private CardService cardService;
    private UserRepository userRepository;

    @Autowired
    public CalendarService(CalendarRepository calendarRepository, TrelloApi trelloApi, CardService cardService, UserRepository userRepository) {
        this.calendarRepository = calendarRepository;
        this.trelloApi = trelloApi;
        this.cardService = cardService;
        this.userRepository = userRepository;
    }


    /**
     * Divise le temps saisie par cartes et affecte a chaque carte son temps
     * Si c est une modification il supprime au préalable dans
     * @param calendar
     * @return
     */
    private CalendarModel save(CalendarModel calendar) {
        if(calendar.getId() != null){
            unsplitTimeByCard(calendar);
        }
        splitTimeByCard(calendar);
        return calendarRepository.save(calendar);
    }

    @Transactional
    public List<CalendarModel> saveAll(List<CalendarModel> calendarsList) {
        calendarsList.stream().forEach(calendarModel -> {
            calendarModel.setBoardID(this.boardId);
            existanceCard(calendarModel);
            save(calendarModel);
        });
        return calendarsList;
    }

    private void existanceCard(CalendarModel calendarModel) {
        calendarModel.getCards().stream().forEach(card -> cardService.findAndSave(card));
    }

    private void unsplitTimeByCard(CalendarModel calendar){
        final Optional<CalendarModel> byId = calendarRepository.findById(calendar.getId());
        if(byId.isPresent()){
            final CalendarModel previous = byId.get();
            Double unit = calcTimeBySizeCards(previous.getTime() , previous.getCards().size());
            removeTimeToCards(previous.getCards(), unit);
        }
    }

    private void splitTimeByCard(CalendarModel calendar) {
        Double unit = calcTimeBySizeCards(calendar.getTime() , calendar.getCards().size());
        addTimeToCard(calendar.getCards(), unit);
    }

    public void addTimeToCard(List<CardModel> cards, Double unit) {
        SumUpTime(cards, unit, 1);
    }

    public void removeTimeToCards(List<CardModel> cards, Double unit){
        SumUpTime(cards, unit, -1);
    }

    private void SumUpTime(List<CardModel> cards, Double unit, int sign) {
        cards.stream().forEach(cardModel -> {
            Card trelloCard = trelloApi.getBoardCard(boardId, cardModel.getId());
            final Double timeSpent = cardService.getConsumedComplexity(trelloCard);
            cardService.setConsumedComplexity(trelloCard, timeSpent + (sign* unit));
        });
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public List<Member> getMembersFromBoard(String boardId) {
        return trelloApi.getBoardMembers(boardId);
    }

    public List<UserModel> getMembers() {
        return userRepository.findAll();
    }

    public Map<Member, Boolean> getMembersForScreen(String boardId) {
        final List<Member> members = getMembersFromBoard(boardId);
        final Map<Member, Boolean> memberBooleanMap = new LinkedHashMap<>();
        final List<UserModel> all = getMembers();
        members.stream().sorted((m1, m2) -> m1.getFullName().toLowerCase().compareTo(m2.getFullName().toLowerCase())).forEach(member ->
        {
            final boolean present = all.stream().filter(userModel -> userModel.getIdentifiant_trello().equalsIgnoreCase(member.getId())).findFirst().isPresent();
            memberBooleanMap.putIfAbsent(member, present);
        });
        return memberBooleanMap;
    }

    public void setMembers(List<Member> members) {
        members.stream().forEach(member -> {
            UserModel userModel = new UserModel();
            userModel.setIdentifiant_trello(member.getId());
            userModel.setName(member.getFullName());
            userRepository.save(userModel);
        });
    }

    public void setMember(String id, boolean status, String boardId) {
        final List<Member> membersFromBoard = getMembersFromBoard(boardId);
        final Optional<Member> first = membersFromBoard.stream().filter(member -> member.getId().equalsIgnoreCase(id)).findFirst();
        if (status && first.isPresent()) {
            UserModel user = new UserModel();
            user.setIdentifiant_trello(first.get().getId());
            user.setName(first.get().getFullName());
            userRepository.save(user);
        } else if (first.isPresent()) {
            userRepository.deleteById(first.get().getId());
        }
    }

    public List<CalendarModel> getCalendarFor(int month, int week, String member) {
        final List<MonthRest> listMonthsRest = UtilService.getListMonthsRest();
        final MonthRest monthRest = listMonthsRest.get(month -1 );
        final WeekRest currentWeek = monthRest.getWeekRestList().get(week);
        LocalDate debut = currentWeek.getLocalFirstDay();
        LocalDate fin = currentWeek.getLocalLastDay();

        if (currentWeek.getLastDay() < currentWeek.getFirstDay()) {
            fin = monthRest.getLastDay();
        }
        UserModel userModel = new UserModel();
        userModel.setIdentifiant_trello(member);
        List<CalendarModel> allByDateIsBetweenAndUser = new ArrayList<>();
        if (member != null) {
            allByDateIsBetweenAndUser = calendarRepository.findAllByDateIsBetweenAndUser(debut, fin, userModel);
        }


        return allByDateIsBetweenAndUser;
    }


    private Double calcTimeBySizeCards(Double time, int size){
        return Math.round( ( time / size) *100.0) / 100.0;
    }

    public Map<String, Map<String, Double>> getStatTimeByLabel(LocalDate depart, LocalDate fin) {

        final List<TimeByLabel> statTimeByLabel = calendarRepository.getStatTimeByLabel(depart, fin);
        Map<String, Map<String, Double>> pivot = new LinkedHashMap<>();

        final Map<String, List<TimeByLabel>> collect = statTimeByLabel.stream().collect(Collectors.groupingBy(TimeByLabel::getName));
        collect.forEach((s, timeByLabels) -> {
            Map<String, Double> info = new LinkedHashMap<>();
            timeByLabels.forEach(timeByLabel -> {
                info.putIfAbsent(timeByLabel.getLabel(),timeByLabel.getTotal());
            });
            pivot.putIfAbsent(s, info);
        });

        return pivot;
    }

    public Map<String, List<TimeByLabel>> getStatTimeByUserAndDate(LocalDate depart, LocalDate fin) {
        final List<TimeByLabel> statTimeByUserAndDate = calendarRepository.getStatTimeByUserAndDate(depart, fin);

        final Map<String, List<TimeByLabel>> pivot = statTimeByUserAndDate.stream().collect(Collectors.groupingBy(TimeByLabel::getName));

        return pivot;
    }
}
