let intervalId = null;
let btnBatch = null;

function activateBatch(btn) {
    btnBatch = btn;
    const url = "batch/start";
    const synchro = 1000;
    $.getJSON(url, null, (data) => {
        if (data.start) {
            toggleBtn(btn, null);
            intervalId = setInterval(statusBatch, synchro);
        } else {
            intervalId = setInterval(statusBatch, synchro);
        }
        btn.innerHTML = data.message;
    }, (error) => {
        console.log(error);
        toggleBtn(btn, false);
    });
}


statusBatch = () => {
    const btn = btnBatch;
    $.getJSON("batch/status", null, (data) => {
        if (data.running) {
            toggleBtn(btn, null);
        } else {
            clearInterval(intervalId);
            btnBatch = null;
            toggleBtn(btn, true);

            window.document.location.reload();
        }
        btn.innerHTML = data.message;
    }, (error) => {
    });

}


function toggleBtn(btn, success) {
    if (success == null) {
        $(btn).addClass("btn-warning");
        $(btn).removeClass("btn-success");
        $(btn).removeClass("btn-danger");
        $(btn).removeClass("btn-default");
    } else if (success) {
        $(btn).addClass("btn-success");
        $(btn).removeClass("btn-warning");
        $(btn).removeClass("btn-danger");
        $(btn).removeClass("btn-default");
    } else {
        $(btn).addClass("btn-danger");
        $(btn).removeClass("btn-warning");
        $(btn).removeClass("btn-success");
        $(btn).removeClass("btn-default");
    }
}
