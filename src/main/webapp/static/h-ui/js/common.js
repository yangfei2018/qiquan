function layer_alert(msg, func) {
	layer.alert(msg, {
		icon : 2
	}, function(index) {
		layer.close(index);
		func && func();
	})
}

function layer_success(msg, func) {
	layer.alert(msg, {
		icon : 1
	}, function(index) {
		layer.close(index);
		func && func();
	})
}

function isIE() { // ie?
	if (!!window.ActiveXObject || "ActiveXObject" in window)
		return true;
	else
		return false;
}

function gohref(uri) {
	if (isIE()) {
		if (uri.substring(0, 1) == "/") {
			window.location.href = uri;
		} else {
			window.location.href = "/" + uri;
		}
	} else {
		if (uri.substring(0, 1) == "/") {
			window.location.href = uri.substring(1, uri.length);
		} else {
			window.location.href = uri;
		}
	}
}