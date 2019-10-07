package io.tools.trellobacklogsaggregator.service;

import io.tools.trellobacklogsaggregator.model.MonthRest;
import io.tools.trellobacklogsaggregator.model.WeekRest;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class UtilService {
    private static List<MonthRest> listMonthsRest;

    private static void initDate() {
        listMonthsRest = new ArrayList<>();

        LocalDate local = LocalDate.now();
        int montNow = local.getMonthValue();

        for (int i = 1; i < 13; i++) {
            LocalDate tmp = local.withDayOfMonth(1).withMonth(i);
            if (i < montNow-1) {
                tmp = tmp.plusYears(1);
            }

            final MonthRest monthRest = new MonthRest(getDisplayMonth(tmp), tmp.getMonthValue(), tmp.lengthOfMonth(), tmp.getDayOfWeek().getValue(), tmp.getYear());
            int weeks = getWeeksMonth(tmp, tmp.plusDays(tmp.lengthOfMonth()));
            DateTime result = convertLocalDateToDateTime(tmp);
//            DateTime result = dateTime.dayOfWeek().setCopy(DateTimeConstants.MONDAY);
            for (int j = 0; j <= weeks; j++) {
                if (result.getDayOfWeek() < DateTimeConstants.SATURDAY) {
                    final DateTime endWeek = result.dayOfWeek().setCopy(DateTimeConstants.FRIDAY);
                    monthRest.addWeek(result.getDayOfMonth(), endWeek.getDayOfMonth(), j, result.getDayOfWeek(), endWeek.getDayOfWeek());
                } else {
                    /**la premiere semaine du mois demarre en fin de semaine**/
                    j = -1;
                }
                result = result.plusWeeks(1).dayOfWeek().setCopy(DateTimeConstants.MONDAY);
            }
            listMonthsRest.add(monthRest);
        }

    }

    public static DateTime convertLocalDateToDateTime(LocalDate localDate) {
        return new DateTime(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0);
    }

    public static List<MonthRest> getListMonthsRest() {
        if (listMonthsRest == null) {
            initDate();
        }
        return listMonthsRest;
    }

    private static int getWeeksMonth(LocalDate localDateDeb, LocalDate localDateEnd) {

        DateTime dateTimeDeb = convertLocalDateToDateTime(localDateDeb);
        DateTime dateTimeFin = convertLocalDateToDateTime(localDateEnd);
        Period p = new Period(dateTimeDeb, dateTimeFin, PeriodType.yearWeekDay());
        return p.getWeeks();
    }

    private static String getDisplayMonth(LocalDate localDate) {
        Locale locale = Locale.getDefault();
        return localDate.getMonth().getDisplayName(TextStyle.FULL, locale);
    }

    public static int getDaysOfWeek(int month, int week, int indexDay) {
        int tmp = 0;
        final Optional<WeekRest> weekRest;
        final Optional<MonthRest> first = getListMonthsRest().stream()
                .filter(item -> item.getIndex() == month)
                .findFirst();
        if (first.isPresent()) {
            weekRest = first.get()
                    .getWeekRestList().stream()
                    .filter(item -> item.getIndex() == week)
                    .findFirst();
            if (weekRest.isPresent()) {
                final WeekRest weekRest1 = weekRest.get();

                if (weekRest1.getIndexFirstDay() == indexDay) {
                    tmp = weekRest1.getFirstDay();
                } else if (weekRest1.getIndexLastDay() == indexDay && weekRest1.getLastDay() > weekRest1.getFirstDay()) {
                    tmp = weekRest1.getLastDay();
                } else if (indexDay < weekRest1.getIndexFirstDay()) {
                    tmp = 0;
                } else {
                    tmp = weekRest1.getFirstDay() + (indexDay - weekRest1.getIndexFirstDay());
                    tmp = (tmp > first.get().getLength())? 0: tmp;
                }
            }
        }
        return tmp;
    }

    public static int getWeekOfDay(LocalDate date) {
        int week = 0;
        final Optional<MonthRest> currentMonth = getListMonthsRest().stream()
                .filter(item -> item.getIndex() == date.getMonthValue())
                .findFirst();
        if (currentMonth.isPresent()) {
            Optional<WeekRest> weekRestOptional = currentMonth.get().getWeekRestList().stream()
                    .filter(item ->
                            date.getDayOfMonth() >= item.getFirstDay() && (date.getDayOfMonth() <= item.getLastDay() || (item.getLastDay() < item.getFirstDay()))
                    )
                    .findFirst();
            if (weekRestOptional.isPresent()) {
                week = weekRestOptional.get().getIndex();
            }
        }
        return week;
    }
}
