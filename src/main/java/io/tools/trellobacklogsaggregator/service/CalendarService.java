package io.tools.trellobacklogsaggregator.service;

import com.julienvey.trello.domain.Card;
import com.julienvey.trello.domain.Member;
import io.tools.trellobacklogsaggregator.bean.TimeByLabel;
import io.tools.trellobacklogsaggregator.model.*;
import io.tools.trellobacklogsaggregator.repository.CalendarRepository;
import io.tools.trellobacklogsaggregator.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CalendarService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
     *
     * @param calendar
     * @return
     */
    private CalendarModel save(CalendarModel calendar) {
        splitTimeByCard(calendar);
        return calendarRepository.save(calendar);
    }

    @Transactional
    public List<CalendarModel> saveAll(List<CalendarModel> calendarsList) {
        calendarsList.stream().forEach(calendarModel -> {
            calendarModel.setBoardID(this.boardId);
            existenceCards(calendarModel);
            save(calendarModel);
        });
        return calendarsList;
    }

    private void existenceCards(CalendarModel calendarModel) {
        //annuler tout ce que l on a fait pour ce calendrier
        clearCalendarModel(calendarModel);
        calendarModel.getCards().stream().forEach(card -> cardService.findAndSave(card));
    }

    private void clearCalendarModel(CalendarModel calendarModel) {
        if (calendarModel.getId() != null) {
            unsplitTimeByCard(calendarModel);
            final List<CardModel> cards = new ArrayList<>(calendarModel.getCards());
            calendarModel.getCards().clear();
            calendarRepository.save(calendarModel);
            calendarModel.setCards(cards);
        }
    }

    private void unsplitTimeByCard(CalendarModel calendar) {
        if (calendar.getId() != null) {
            final Optional<CalendarModel> byId = calendarRepository.findById(calendar.getId());
            if (byId.isPresent()) {
                final CalendarModel previous = byId.get();
                Double unit = calcTimeBySizeCards(previous.getTime(), previous.getCards().size());
                removeTimeToCards(previous.getCards(), unit, previous.getTime());
            }
        }
    }

    private void splitTimeByCard(CalendarModel calendar) {
        Double unit = calcTimeBySizeCards(calendar.getTime(), calendar.getCards().size());
        addTimeToCard(calendar.getCards(), unit, calendar.getTime());
    }

    public void addTimeToCard(List<CardModel> cards, Double unit, Double totalTimeEdit) {
        SumUpTime(cards, unit, 1, totalTimeEdit);
    }

    public void removeTimeToCards(List<CardModel> cards, Double unit, Double totalTimeEdit) {
        SumUpTime(cards, unit, -1, totalTimeEdit);
    }

    private void SumUpTime(List<CardModel> cards, Double unit, int sign, Double totalTimeEdit) {
        boolean uniq = cards.size() == 1;
        boolean odd = cards.size() % 2 != 0;
        final Iterator<CardModel> iterator = cards.iterator();
        while (iterator.hasNext()) {
            final CardModel next = iterator.next();
            try {
                Card trelloCard = trelloApi.getBoardCard(boardId, next.getId());
                final Double timeSpent = cardService.getConsumedComplexity(trelloCard);
                double time = Math.round((timeSpent + (sign * unit)) * 100.0) / 100.0;
                if (!iterator.hasNext() && !uniq && odd) {
                    double decal = Math.round((totalTimeEdit - unit * cards.size()) * 100.0) / 100.0;
                    time += sign * decal;
                }
                cardService.setConsumedComplexity(trelloCard, time);
            } catch (Exception exc) {
                logger.warn(String.format("Card %s suppressed", next.getId()));
            }
        }

    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public List<Member> getMembersFromBoard(String boardId) {
        return trelloApi.getBoardMembers(boardId);
    }

    public List<UserModel> getMembers() {
        return userRepository.findAllByActif(true);
    }

    public Map<Member, Boolean> getMembersForScreen(String boardId) {
        final List<Member> members = getMembersFromBoard(boardId);
        final Map<Member, Boolean> memberBooleanMap = new LinkedHashMap<>();
        members.stream().sorted(Comparator.comparing(m -> m.getFullName().toLowerCase())).forEach(member ->
        {
            final Optional<UserModel> byId = userRepository.findById(member.getId());
            boolean present = false;
            if(byId.isPresent()){
                present = byId.get().isActif();
            }
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

    public void setMember(String id, boolean status, String boardId) throws Exception {
        final List<Member> membersFromBoard = getMembersFromBoard(boardId);
        final Optional<Member> first = membersFromBoard.stream().filter(member -> member.getId().equalsIgnoreCase(id)).findFirst();
        UserModel user = new UserModel();
        if (first.isPresent()) {
            user.setIdentifiant_trello(first.get().getId());
            user.setName(first.get().getFullName());
            user.setActif(status);
            userRepository.save(user);
        }else {
            throw new Exception("Member Trello inconnu ???");
        }

    }

    public List<CalendarModel> getCalendarFor(int month, int week, String member) {
        final List<MonthRest> listMonthsRest = UtilService.getListMonthsRest();
        final MonthRest monthRest = listMonthsRest.get(month - 1);
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


    private Double calcTimeBySizeCards(Double time, int size) {
        final double res = Math.round((time / size) * 100.0) / 100.0;
        return res;
    }

    public Map<String, Map<String, Double>> getStatTimeByLabel(LocalDate depart, LocalDate fin) {

        final List<TimeByLabel> statTimeByLabel = calendarRepository.getStatTimeByLabel(depart, fin);
        Map<String, Map<String, Double>> pivot = new LinkedHashMap<>();

        final Map<String, List<TimeByLabel>> collect = statTimeByLabel.stream().collect(Collectors.groupingBy(TimeByLabel::getName));
        collect.forEach((s, timeByLabels) -> {
            Map<String, Double> info = new LinkedHashMap<>();
            timeByLabels.forEach(timeByLabel -> {
                info.putIfAbsent(timeByLabel.getLabel(), timeByLabel.getTotal());
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
