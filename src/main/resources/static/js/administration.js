function onChangeMonthAnalysis(combo) {
    window.location.href = "/administration/" + combo.value;

}

function onChangeWeekAnalysis(combo) {
    const month = $('#adminComboMonth')[0].value;
    window.location.href = "/administration/" + month + '/' + combo.value;
}
