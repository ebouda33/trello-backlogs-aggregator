let currentCalendar = undefined;

function generateCalendar(labels, year, month, week) {
    currentCalendar = new CalendarClass(labels, year, month, week);
    // parser toutes les colonnes pour mettre les valeurs
    const maxLabel = Object.keys(labels).length;
    const maxDay = 5;
    let i = 0;
    Object.keys(labels).forEach(key => {
        for (let j = 0; j < maxDay; j++) {
            const span = $('#' + i + '_' + j + ' > span')[0];
            if (span !== undefined) {
                span.innerHTML = currentCalendar.getTime(labels[key], j);
            }
        }
        i++;
    });
    $("[listener=calendar]").each((index, el) => {
        currentCalendar.addListener(new ListenerCalendar(el.id, el.getAttribute('label'), el.getAttribute('day')));
    });
    return currentCalendar;
}

function checkCardToCalendar(checkbox, label, day) {
    // add or remove card from calendar
    const id = checkbox.id.replace('checkbox_', '');
    $('#' + id).toggleClass("fluo");

    currentCalendar.toggleCard(label, day, id);
}

function isValidCalendar() {
    if (!currentCalendar.isValid()) {
        $('#msgErrorValidation > span')[0].innerHTML = "Saisie incomplète - Séléctionner les Cartes Trello";
        $('#msgErrorValidation').addClass("bg-danger");
        $('#msgErrorValidation').removeClass("bg-success");
    } else {
        $('#msgErrorValidation > span')[0].innerHTML = "Saisie cohérente";
        $('#msgErrorValidation').addClass("bg-success");
        $('#msgErrorValidation').removeClass("bg-danger");
    }

    return currentCalendar.isValid();
}

function saveCalendar() {
    if (isValidCalendar()) {
        const url = "/calendar/saves";
        $.postJSON(url, currentCalendar.toJson(), done, fail);

    }
}

function done(data) {
    console.log(data);
}

function fail(error) {
    console.error(error);
}


jQuery["postJSON"] = function (url, data, callback, failure) {
    // shift arguments if data argument was omitted
    if (jQuery.isFunction(data)) {
        callback = data;
        data = undefined;
    }

    return jQuery.ajax({
        url: url,
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: data,
        success: callback,
        failure: failure
    });
};
