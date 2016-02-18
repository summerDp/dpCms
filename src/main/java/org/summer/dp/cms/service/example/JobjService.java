package org.summer.dp.cms.service.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.summer.dp.cms.helper.json.JsonUtil;

@Service
public class JobjService {

	public static String _ID = "_ID";

	private Map<String, Object> rt = new HashMap<String, Object>();

	public Object getObj(String id) {
		if (null == id) {
			return null;
		}
		String[] paths = id.split("\\.");

		Object data = rt;
		Object parent = null;
		for (int i = 0; i < paths.length; ++i) {
			String curKey = paths[i];
			
			if ((data instanceof Map)) {
				Map dataMap = (Map) data;
				
				
				parent = data;
				data = dataMap.get(curKey);
			} else if ((data instanceof List)) {
				parent = data;
				List<Object> list = (List<Object>)data;
//				if (StrUtil.isNumeric(curKey)) {// 是数字
				if(true){
					Integer ix = Integer.parseInt(curKey);
					data = list.get(ix);
				}
			}
			
		}

		return data;
	}

	public Map<String, Object> setData(String id, Map<String, Object> obj) {
		return null;
	}

	public void printData() {
		String json = JsonUtil.toJSONString(this.rt);

		System.out.println("json=" + json);
	}

	public void makeTestData2Obj() {
		this.rt.put(_ID, "");
		this.rt.put("appName", "我是一个对象");
		this.rt.put("version", "0.1.0");

		Map<String, Object> devInfo = new HashMap<String, Object>();
		devInfo.put(_ID, "devInfo");
		devInfo.put("Arch", "Spring MVC+Redis");
		devInfo.put("Langues", "Java");
		this.rt.put("devInfo", devInfo);

		Map<String, Object> dbDesign = new HashMap<String, Object>();
		dbDesign.put(_ID, "devInfo.dbDesign");
		dbDesign.put("dbName", "MySQL");
		dbDesign.put("dbVersion", "5.6");
		devInfo.put("dbDesign", dbDesign);

		List<Map<String, Object>> developers = new ArrayList<Map<String, Object>>();
		Map<String, Object> dever1 = new HashMap<String, Object>();
		dever1.put("name", "zmx");
		dever1.put("title", "Javaer");
		Map<String, Object> dever2 = new HashMap<String, Object>();
		dever2.put("name", "tys");
		dever2.put("title", "Javaer");
		Map<String, Object> dever3 = new HashMap<String, Object>();
		dever3.put("name", "qj");
		dever3.put("title", "htmler");
		developers.add(dever1);
		developers.add(dever2);
		developers.add(dever3);
		this.rt.put("developers", developers);

	}

//	public static void main(String[] args) {
//		JobjService jService = new JobjService();
//		jService.makeTestData2Obj();
//		jService.printData();
//
//		System.out.println("developers : "
//				+ JsonUtil.toJSONString(jService.getObj("developers")));
//		System.out.println("developers.0 : "
//				+ JsonUtil.toJSONString(jService.getObj("developers.0")));
//		System.out.println("developers.0.name : "
//				+ JsonUtil.toJSONString(jService.getObj("developers.0.name")));
//		
//		System.out.println("developers.1 : "
//				+ JsonUtil.toJSONString(jService.getObj("developers.1")));
//		System.out.println("developers.1.name : "
//				+ JsonUtil.toJSONString(jService.getObj("developers.1.name")));
//		
//		System.out.println("developers.2 : "
//				+ JsonUtil.toJSONString(jService.getObj("developers.2")));
//		System.out.println("developers.2.name : "
//				+ JsonUtil.toJSONString(jService.getObj("developers.2.name")));
//		
//		//
//		// System.out.println("devInfo.dbDesign : "
//		// +JsonUtil.toJSONString(jService.getObj("devInfo.dbDesign")));
//		// System.out.println("version : "
//		// +JsonUtil.toJSONString(jService.getObj("version")));
//		//
//		// String json =
//		// JsonUtil.toJSONString(jService.getObj("devInfo.dbDesign.dbName"));
//		// System.out.println("rjson=" + json);
//	}
}
