
function copyValueToExcel(btn,id) {
    span = document.getElementById(id),
    sel = window.getSelection(),
    rangeObj = document.createRange();


    rangeObj.selectNodeContents(span);
    sel.removeAllRanges();
    sel.addRange(rangeObj);

    toggleBtn(btn, document.execCommand('copy'));
    sel.removeAllRanges();
}


