var contextPath2816 = undefined;
window.getContextPath = function(isReget) {
	if ((!contextPath2816) || isReget) {
		var paths = window.location.pathname.split("/");
		contextPath2816 = "/" + paths[paths[0].length == 0 ? 1 : 0];
	}
	return contextPath2816;
};

window.getVisibleHeight = function() {
	return window.innerHeight || Math.max(document.body.clientHeight, document.documentElement.clientHeight);
};

window.getVisibleWidth = function() {
	return window.innerWidth || Math.max(document.body.clientWidth, document.documentElement.clientWidth);
};

/**
 * @see 判断是否是所需的数据类型
 * @return boolean
 */
window.isPrimitive = function(obj) {
	var t = (typeof obj).hashCode();
	/**
	 * "string" -891985903
	 * "number" -1034364087
	 * "boolean" 64711720
	 * "undefined" -1038130864
	 */
	return t == -891985903 || t == -1034364087 || t == 64711720; // || t == -1038130864;
};

String.prototype.jFormat = function() {
	var str = this.toString();
	if (arguments == undefined || arguments.length == 0) {
		return str;
	}
	var ss = str.split("%s");
	var _s = ss[0];
	for (var i = 1, len = ss.length, j = 0; i < len; ++i, ++j) {
		_s += arguments[j] + ss[i];
	}
	return _s;
};

String.prototype.hashCode = function() {
	var ret = 0;
	for (var i = 0, len = this.length; i < len; ++i) {
		ret = (31 * ret + this.charCodeAt(i)) << 0;
	}
	return ret;
};

Date.prototype.Format = function(fmt) {
	var _h = this.getHours();
	var o = {
		"M+": this.getMonth() + 1,
		"d+": this.getDate(),
		"H+": _h,
		"h+": _h - 12 > 0 ? _h - 12 : _h,
		"m+": this.getMinutes(),
		"s+": this.getSeconds(),
		"q+": Math.floor((this.getMonth() + 3) / 3),
		"S": this.getMilliseconds()
	};
	if (/(y+)/.test(fmt)) {
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	}
	for (var k in o) {
		if (new RegExp("(" + k + ")").test(fmt)) {
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		}
	}
	return fmt;
};

Array.prototype.add = function(index, elem) {
	this.push(elem);
	var size = this.length;
	if(size > 0 && index < size - 1) {
		for(var i = size - 1; i > -1; --i) {
			if(i == index) {
				this[i] = elem;
				break;
			}
			else {
				this[i] = this[i - 1];
			}
		}
	}
};

Array.prototype.removeByIndex = function(index) {
	if (index > -1 && index < this.length) {
		this.splice(index, 1);
	}
};


