function onChangeMonthAnalysis(combo) {
    window.location.href = contextPath+"administration/" + combo.value;

}

function onChangeWeekAnalysis(combo) {
    const month = $('#adminComboMonth')[0].value;
    window.location.href = contextPath+"administration/" + month + '/' + combo.value;
}
