class ListenerCalendar {


    constructor(id, label, day) {
        this._id = id;
        this._label = label;
        this._day = day;
    }

    countCards() {
        if (this._label !== null && this._label !== undefined) {
            $('#' + this._id)[0].innerHTML = `(${currentCalendar.cards[this._label][this._day].length})`;
        }
    }

    totalDay() {
        if (this._label === null || this._label === undefined) {
            $('#' + this._id)[0].innerHTML = currentCalendar.totalOfZeDay(this._day);
        }
    }
}

class ExportCalendar {


    constructor(id, date, time, labelTrello, user, cards = [], day) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.labelTrello = labelTrello;
        this.user = {id: user};
        this.cards = cards.map(cal =>{ return {id:cal}});
        this.dayInWeek = day;
        this.month = date.getMonth() + 1;
        this.year = date.getFullYear();
    }
}

class CalendarClass {



    constructor( label, year, month, week, firstDay, lastDay, indexFirstDay) {
        this.init(label);
        this.weekNumber = week;
        this.monthNumber = month;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
        this.year = year;
        this.indexFirstDay = indexFirstDay;
        this._listeners = [];

    }


    get indexFirstDay() {
        return this._indexFirstDay;
    }

    set indexFirstDay(value) {
        this._indexFirstDay = value;
    }

    get id() {
        return this._id;
    }

    get year() {
        return this._year;
    }

    get month() {
        return this._monthNumber;
    }

    get limitDay() {
        return this._limitDay;
    }

    get firstDay() {
        return this._firstDay;
    }

    get lastDay() {
        return this._lastDay;
    }

    get listeners() {
        return this._listeners;
    }

    get cards() {
        return this._cards;
    }

    get calendar() {
        return this._calendar;
    }

    set id(value) {
        this._id = value;
    }

    set weekNumber(value) {
        this._weekNumber = value;
    }

    set monthNumber(value) {
        this._monthNumber = value;
    }


    set year(value) {
        this._year = value;
    }

    set firstDay(value) {
        this._firstDay = value;
    }

    set lastDay(value) {
        this._lastDay = value;
    }


    set user(value) {
        this._user = value;
    }

    init(label) {
        this._id = {};
        this._calendar = {};
        this._cards = {};
        this._user = null;
        this._year = 0;
        this._monthNumber = 0;
        this._weekNumber = 0;

        this._limitDay = 5;
        this._firstDay = 0;
        this._lastDay = 0;
        this._indexFirstDay = 0;
        Object.values(label).forEach(value => {
            this._calendar[value.name] = [0, 0, 0, 0, 0];
            this._cards[value.name] = [[], [], [], [], []];
            this._id[value.name] = [undefined, undefined, undefined, undefined, undefined];
        });
    }

    writeTimeToCalendar(label, day, value) {
        if (this._calendar[label] !== undefined) {
            value = isNaN(value) || value.trim().length === 0 ? 0 : value * 1;
            value = value > 1 ? 1 : value < 0 ? 0 : value * 1;
            if(value ==0 ) this._cards[label][day] = [];
            value = this.controleCoherenceDay(label, day, value);
            this._calendar[label][day] = value;
            this.preventListeners();
        }
    }

    addCard(label, day, card) {
        if (this._calendar[label] !== undefined && !this.isCardIn(card, label, day)) {
            this._cards[label][day].push(card);
        }
    }

    toggleCard(label, day, card) {
        if (this.isCardIn(card, label, day)) {
            this._cards[label][day] = this._cards[label][day].filter(id => id !== card);
        } else {
            this._cards[label][day].push(card);
        }
        this.preventListeners();
    }

    getTime(label, day) {
        return this._calendar[label][day];
    }


    toString() {
        return "{ M:" + this._monthNumber + ", W: " + this._weekNumber + " , L: " + JSON.stringify(this._calendar) + "}";
    }

    addListener(element) {
        this._listeners.push(element);
    }

    preventListeners() {
        this._listeners.forEach(listener => {
            listener.countCards();
            listener.totalDay();
        });
    }

    isCardIn(card, label, day) {
        return this._cards[label][day].find(current => current === card) !== undefined;
    }

    controleCoherenceDay(label, day, value) {
        let total = 0;
        Object.keys(this._calendar).forEach(key => {
            if (key !== label) {
                total += this._calendar[key][day];
            }
        });
        if (total + value > 1) {
            console.error("Saisie incorrect", total, value);
            value = 0;
        }
        return value;

    }

    totalOfZeDay(day) {
        let total = 0;
        Object.keys(this._calendar).forEach(key => {
            total += this._calendar[key][day];
        });

        return total;
    }

    isValid() {
        let total = 0;
        let ok = true;
        for (let i = 0; i < this._limitDay; i++) {
            total += this.totalOfZeDay(i);
            ok = ok && this.controlCardsByDay(i);
        }

        return ok && total <= this._limitDay;
    }

    controlCardsByDay(day) {
        let correct = true;
        Object.keys(this._calendar).forEach(key => {
            const total = this._calendar[key][day];
            if (total > 0) {
                correct = correct && (this._cards[key][day].length > 0);
            }

        });
        return correct;
    }

    toJson() {
        const calendars = [];
        Object.keys(this._calendar).forEach(key => {
            for (let day = 0; day < this._limitDay; day++) {
                const time = this._calendar[key][day];
                const cards = this._cards[key][day];
                const id = this._id[key][day];
                if(day >= this.indexFirstDay) {
                    const currentDate = TransformerCalendar.transformDayToDate(this, day, this.indexFirstDay);
                    // TODO controler la coherence de la date de début avec celle de la semaine exemple 01/10/2019 => mardi par lundi
                    calendars.push(new ExportCalendar(id, currentDate, time, key, this._user, cards, day));

                }
            }
        });
        return JSON.stringify(calendars);
    }

    dataToCalendar(data) {
        const that = this;
        $.each(data , function (index, calendar) {
            if(that._id[calendar.labelTrello] !== undefined) {
                that._id[calendar.labelTrello][calendar.dayInWeek] = calendar.id;
                that._calendar[calendar.labelTrello][calendar.dayInWeek] = calendar.time;
                that._cards[calendar.labelTrello][calendar.dayInWeek] = calendar.cards.map(cal => cal.id);
            }
        });
        that.preventListeners();
    }
}


class TransformerCalendar {

    static transformDayToDate(calendar, dayIndex, firstDayIndex) {
        return new Date(Date.UTC(calendar.year, calendar.month - 1, dayIndex === firstDayIndex ? calendar.firstDay : dayIndex === calendar.limitDay ? calendar.lastDay : calendar.firstDay + (dayIndex-firstDayIndex)));
    }
}
