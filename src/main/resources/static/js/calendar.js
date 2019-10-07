let currentCalendar = undefined;

function generateCalendar(labels, year, month, week, firstDay, lastDay, indexFirtDay) {
    currentCalendar = new CalendarClass(labels, year, month, week, firstDay, lastDay, indexFirtDay);
    // parser toutes les colonnes pour mettre les valeurs
    $("[listener=calendar]").each((index, el) => {
        currentCalendar.addListener(new ListenerCalendar(el.id, el.getAttribute('label'), el.getAttribute('day')));
    });
    if (Array.isArray(content) && content.length > 0) {
        currentCalendar.dataToCalendar(content);
    }
    const maxDay = 5;
    let i = 0;
    Object.keys(labels).forEach(key => {
        for (let j = 0; j < maxDay; j++) {
            displayTime(currentCalendar.getTime(labels[key], j), i + '_' + j);
        }
        i++;
    });

    return currentCalendar;
}

function checkCardToCalendar(checkbox, label, day) {
    // add or remove card from calendar
    const id = checkbox.id.replace('checkbox_', '');
    $('#' + id).toggleClass("fluo");
    currentCalendar.toggleCard(label, day, id);
}

function checkCardToCalendarClick(checkbox) {
    checkbox.click();
}

function isValidCalendar() {
    if (!currentCalendar.isValid()) {
        displayMsg("Saisie incomplète - Séléctionner les Cartes Trello");

    } else {
        const idMember = $('.member')[0].value;
        if (idMember === "0") {
            displayMsg("Qui êtes vous ?");

            return false;
        } else {
            currentCalendar.user = idMember;
        }
        displayMsg("Saisie cohérente", "success");
    }

    return currentCalendar.isValid();
}

function saveCalendar(button) {

    button.setAttribute("disabled", true)
    if (isValidCalendar()) {
        displayMsg("Sauvegarde en cours ...", "warning");
        const url = contextPath+"calendar/saves/" + boardId;
        $.postJSON(url, currentCalendar.toJson(), (data) => {
            currentCalendar.dataToCalendar(data);
            displayMsg("Données sauvegardées", "success");
            button.removeAttribute("disabled")
        }, (error) => {
            let currentMessage = "";
            const standarMsg = "Erreur enregistrement Cf Admin";
            if (error !== undefined && error.responseJSON !== undefined && error.responseJSON.message === "ConstraintViolationException" && error.status === 400) {
                $.each(error.responseJSON.multiMessage, function (index, message) {
                    const json = JSON.parse(message);
                    if (json.property === 'user') {
                        currentMessage += " Qui êtes vous ?";
                    }
                });
            }
            if (currentMessage.trim().length === 0) {
                currentMessage = standarMsg;
            }
            displayMsg(currentMessage);
            button.removeAttribute("disabled")
        });

    }
}


function displayMsg(message, error = "danger") {
    $('#msgErrorValidation > span')[0].innerHTML = message;
    switch (error) {
        case "danger":
            $('#msgErrorValidation').addClass("bg-danger");
            $('#msgErrorValidation').removeClass("bg-warning");
            $('#msgErrorValidation').removeClass("bg-success");
            break;
        case "success":
            $('#msgErrorValidation').addClass("bg-success");
            $('#msgErrorValidation').removeClass("bg-danger");
            $('#msgErrorValidation').removeClass("bg-warning");
            break;
        case "warning":
            $('#msgErrorValidation').addClass("bg-warning");
            $('#msgErrorValidation').removeClass("bg-danger");
            $('#msgErrorValidation').removeClass("bg-success");
            break;

    }
}

function checkMember(input, id) {
    const url = "administration";
    $.putJSON(url, JSON.stringify({"id": id, "board": boardId, "status": input.checked}), function (data) {
    }, function (error) {
        console.error(error);
    });
}

