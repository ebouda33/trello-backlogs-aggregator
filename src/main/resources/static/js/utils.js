jQuery["postJSON"] = function (url, data, success, failure,dataType) {
    return ajax(url,"POST", data,success,failure,dataType);
};
jQuery["putJSON"] = function (url, data, success, failure, dataType) {
    return ajax(url,"PUT", data,success,failure,dataType);
};
jQuery["getJSON"] = function (url, data, success, failure, dataType) {
    return ajax(url,"GET", data,success,failure,dataType);
};

function ajax(url,method, data, success, failure, dataType){
    if (jQuery.isFunction(data)) {
        success = data;
        data = undefined;
    }

    return jQuery.ajax({
        url: url,
        type: method,
        contentType: "application/json; charset=utf-8",
        dataType: dataType | "json",
        data: data,
        success: success,
        error: failure
    });
}
