const KeyEnter = 13;
let listByDefault = undefined;

function onChangeMonth(combo) {
    const month = combo.options[combo.selectedIndex].value;
    window.location = contextPath+"spentTime/" + month;
}

function onChangeWeek(combo, currentMonth) {

    const week = combo.options[combo.selectedIndex].value;
    window.location = contextPath + "spentTime/" + currentMonth + "/" + week;
}


function showLine(day, label, id, column) {
    if (day !== '00') {
        $('#' + id + ' > span').hide();
        $('#' + id + '> input').show();
        $('#' + id + '> input').focus();
    }
}

function validateTime(input, e, id, label, column) {
    var keycode = e.keyCode ? e.keyCode : e.which;
    // console.error(keycode);
    if (keycode === KeyEnter) {
        e.preventDefault();
        endSetTime(e, id, label, column);
    }
}

function endSetTime(e, id, label, column) {
    const value = $('#' + id + ' > input')[0].value;
    $('#' + id + ' > input').hide();
    currentCalendar.writeTimeToCalendar(label, column, value);
    const time = currentCalendar.getTime(label, column);
    displayTime(time, id);
    // if(time == 1) {
    //     saveCalendar($('#bt_save').get(0));
    // }
}

function displayTime(time, id) {
    if ($('#' + id + ' > input')[0] !== undefined) {
        let value = $('#' + id + ' > input')[0].value;
        const $1 = $('#' + id + ' > span');
        $1[0].innerHTML = time;
        $('#' + id + ' > input')[0].value = time;
        if (value === "") {
            value = time;
        }
        $1.show();
        if (value > 0) {
            $('#addCards_' + id + ' > a').show();
        } else {
            $('#addCards_' + id + ' > a').hide();
        }
    }
}

function init(idBoard) {
    getDefaultList();
}

function getDefaultList() {
    const url = "lists/default";
    $.ajax(contextPath + url)
        .done(function (data) {
            listByDefault = data;
        })
        .fail(function (error) {
            console.error(error);
        });
}

function getlistsTrello(idBoard, id) {
    const url = "lists/" + idBoard;
    $.ajax(contextPath + url)
        .done(function (data) {
            const select = '#modalCards_' + id + '> p > select';
            $(select + ' option').remove();
            $(select).append(
                $('<option>', {
                        text: "Selectionné une liste",
                        value: "0",
                        selected: true,
                        disabled: true
                    }
                ));
            $.each(data, function (i, item) {
                $(select).append(
                    $('<option>', {
                            text: item.name,
                            value: item.id,
                        }
                    ));
            });
            // pour selectionne et active la recherce de cartes de la liste
            $(select).val(getIdList(select, listByDefault)).change();
        })
        .fail(function (error) {
            console.error(error);
        });
}

function getIdList(select, list) {
    return $(select + ' option:contains(\'' + list + '\')').val();
}

function getCards(select, id, label, day) {
    const url = select.getAttribute("url") + select.value;
    const zoneCards = $("#" + id)[0];
    $.ajax(contextPath + url)
        .done(function (data) {
            if (Array.isArray(data) && data.length === 0) {
                zoneCards.innerHTML = 'Aucune Carte correspondante';
            } else {
                zoneCards.innerHTML = generateCardTrello(data, label, day);
            }
        })
        .fail(function (err) {
            console.error(err);
        })
}

function generateCardTrello(data, label, day) {

    const template = `<div class='cardsTrello col-md-4 [class]' id='cardID' onclick="checkCardToCalendarClick($('#checkbox_cardID')[0])"><div > <input type="checkbox" [checked] id='checkbox_cardID' onclick="checkCardToCalendar(this,'${label}',${day})"></div><span>[name]</span><span><a href='[url]' target='_blank'>Voir</a> </span></div>`;
    let render = "";
    let count = 0;
    $.each(data, function (index, value) {
        let cardIn = currentCalendar.isCardIn(value.id, label, day);
        const checked = cardIn ? 'checked' : '';
        count++;
        const div = template.replace("[class]", cardIn ? 'fluo' : '')
            .replace("[checked]", checked)
            .replace("[name]", value.name)
            .replace("[url]", value.shortUrl)
            .replace(new RegExp("cardID", "gi"), value.id);

        if (index % 3 === 0) {
            render += "<div class='row'>";
        }
        render += div;

        if (count === 3) {
            render += "</div>";
            count = 0;
        }
    });

    return "<div class='col-md-12'>" + render + "</div>";
}

function onChangeMember(select) {

    $.postJSON("spentTime/currentMember",
        select.value, function (data) {
            window.document.location.reload();
        }, function (error) {
            console.error(error);
            window.document.location.reload();
        }, "text/plain");
}


