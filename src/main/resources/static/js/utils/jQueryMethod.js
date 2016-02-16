/**
 * @see 依赖jsMethod和JQuery
 */
/**
 * @see 不能删除该方法
 * @return none
 */
$.convertObj2ParamsRecurse = function(obj, newObj, dataType, key, isFunc, callback) {
	if(!dataType) {
		if($.isPlainObject(obj)) { dataType = 1; }
		if($.isArray(obj)) { dataType = 2; }
	}
	if(dataType == 1) {
		var o;
		var isOk = (key && key.length > 0);
		for(var k in obj) {
			o = obj[k];
			if(!o && o !== 0) { continue; } // 跳过空值，无用的值
			if(isFunc && (!callback(k, o))) {
				continue;
			}
			if(k == "$$hashKey") { continue; }
			var _key = isOk? key + "." + k : k;
			if(window.isPrimitive(o)) {
				if(typeof o == "string") {
					if($.trim(o).length > 0) {
						newObj[_key] = o;
					}
				}
				else {
					newObj[_key] = o;
				}
			}
			else if($.isPlainObject(o)) {
				$.convertObj2ParamsRecurse(o, newObj, 1, _key, isFunc, callback);
			}
			else if($.isArray(o)) {
				$.convertObj2ParamsRecurse(o, newObj, 2, _key, isFunc, callback);
			}
		}
	}
	else if(dataType == 2) {
		if(obj.length == 0) { return; }
		var isOK = key && key.length > 0;
		for(var i = 0, l = obj.length, a, j = 0; i < l; ++i, ++j) {
			a = obj[i];
			
			if((!a) && a != 0) { --j; continue; }
			
			if(isFunc && (!callback(i, a))) { --j; continue; }
			var _key = isOK? (key + "[" + j + "]") : ("[" + j + "]");
			if(window.isPrimitive(a)) {
				if(typeof a == "string") {
					if(a.length > 0) {
						newObj[_key] = a;
					}
					else { --j; continue; }
				}
				else {
					newObj[_key] = a;
				}
			}
			else if($.isPlainObject(a)) {
				$.convertObj2ParamsRecurse(a, newObj, 1, _key, isFunc, callback);
			}
			else if($.isArray(a)) {
				$.convertObj2ParamsRecurse(a, newObj, 2, _key, isFunc, callback);
			}
		}
	}
};

/**
 * @see 将Object转换成Ajax可提交的参数，一般用于AngularJS的表单提交
 * @see 
 * 		var obj = {person: {name: "Jack", age: 12}, isHealthy: true};
 * 		var newObj = {};
 * 		$.convertObject2Params(obj, undefined, newObj);
 * 		console.log(newObj);  // newObj = {"person.name": "Jack", "person.age": 12, isHealthy: true};
 * @param {Object} obj
 * @param {Object} key
 * @param {Object} newObj
 * @return none
 */
$.convertObject2Params = function(obj, callback) {
	var newObj = {};
	var isFunc = $.isFunction(callback);
	$.convertObj2ParamsRecurse(obj, newObj, undefined, undefined, isFunc, callback);
	return newObj;
}
