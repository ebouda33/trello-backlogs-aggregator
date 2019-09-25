class ListenerCalendar {
    #_id;
    #_label;
    #_day;


    constructor(id, label, day) {
        this.#_id = id;
        this.#_label = label;
        this.#_day = day;
    }

    countCards() {
        if (this.#_label !== null && this.#_label !== undefined) {
            $('#' + this.#_id)[0].innerHTML = `(${currentCalendar.cards[this.#_label][this.#_day].length})`;
        }
    }

    totalDay() {
        if (this.#_label === null || this.#_label === undefined) {
            $('#' + this.#_id)[0].innerHTML = currentCalendar.totalOfZeDay(this.#_day);
        }
    }
}

class ExportCalendar {
    date = null;
    time = 0;
    labelTrello = null;
    user = null;
    cards = [];


    constructor(date, time, labelTrello, user, cards = []) {
        this.date = date;
        this.time = time;
        this.labelTrello = labelTrello;
        this.user = user;
        this.cards = cards;
    }
}

class CalendarClass {


    #_calendar = {};
    #user;
    #_year;
    #_monthNumber;
    #_weekNumber;
    #_cards = {};
    #_listeners = [];
    #_limitDay = 5;
    #_firstDay;
    #_lastDay;


    constructor(label,year, month, week, firstDay, lastDay) {
        this.init(label);
        this.weekNumber = week;
        this.monthNumber = month;
        this.year = year;
        this.firstDay = firstDay;
        this.lastDay = lastDay;

    }


    get firstDay() {
        return this._firstDay;
    }

    get lastDay() {
        return this._lastDay;
    }

    get listeners() {
        return this.#_listeners;
    }

    get cards() {
        return this.#_cards;
    }

    get calendar() {
        return this.#_calendar;
    }

    set weekNumber(value) {
        this.#_weekNumber = value;
    }

    set monthNumber(value) {
        this.#_monthNumber = value;
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

    init(label) {
        Object.keys(label).forEach(key => {
            this.#_calendar[label[key]] = [0, 0, 0, 0, 0];
            this.#_cards[label[key]] = [[], [], [], [], []];
        });
    }

    writeTimeToCalendar(label, day, value) {
        if (this.#_calendar[label] !== undefined) {
            value = isNaN(value) || value.trim().length === 0 ? 0 : value * 1;
            value = value > 1 ? 1 : value < 0 ? 0 : value * 1;
            value = this.controleCoherenceDay(label, day, value);
            this.#_calendar[label][day] = value;
            this.preventListeners();
        }
    }

    addCard(label, day, card) {
        if (this.#_calendar[label] !== undefined && !this.isCardIn(card, label, day)) {
            this.#_cards[label][day].push(card);
        }
    }

    toggleCard(label, day, card) {
        let find = this.#_cards[label][day].find(card => card === card);
        if (this.isCardIn(card, label, day)) {
            this.#_cards[label][day] = this.#_cards[label][day].filter(card => card !== card);
        } else {
            this.#_cards[label][day].push(card);
        }
        this.preventListeners();
    }

    getTime(label, day) {
        return this.#_calendar[label][day];
    }


    toString() {
        return "{ M:" + this.#_monthNumber + ", W: " + this.#_weekNumber + " , L: " + JSON.stringify(this.#_calendar) + "}";
    }

    addListener(element) {
        this.#_listeners.push(element);
    }

    preventListeners() {
        this.#_listeners.forEach(listener => {
            listener.countCards();
            listener.totalDay();
        });
    }

    isCardIn(card, label, day) {
        return this.#_cards[label][day].find(current => current === card) !== undefined;
    }

    controleCoherenceDay(label, day, value) {
        let total = 0;
        Object.keys(this.#_calendar).forEach(key => {
            if (key !== label) {
                total += this.#_calendar[key][day];
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
        Object.keys(this.#_calendar).forEach(key => {
            total += this.#_calendar[key][day];
        });

        return total;
    }

    isValid() {
        let total = 0;
        let ok = true;
        for (let i = 0; i < this.#_limitDay; i++) {
            total += this.totalOfZeDay(i);
            ok = ok && this.controlCardsByDay(i);
        }

        return ok && total <= this.#_limitDay;
    }

    controlCardsByDay(day) {
        let correct = true;
        Object.keys(this.#_calendar).forEach(key => {
            const total = this.#_calendar[key][day];
            if (total > 0) {
                correct = correct && (this.#_cards[key][day].length > 0);
            }

        });
        return correct;
    }

    toJson() {
        const calendars = [];
        Object.keys(this.#_calendar).forEach(key => {
            for(let day =0 ; day < this.#_limitDay; day++){
                const time = this.#_calendar[key][day];
                const cards = this.#_cards[key][day];
                calendars.push(new ExportCalendar(null, time, key, this.#user, cards));
            }
        });
        return JSON.stringify(calendars);
    }
}


class TransformerCalendar{

    static transformDayToDate(month,week,day){

    }
}
