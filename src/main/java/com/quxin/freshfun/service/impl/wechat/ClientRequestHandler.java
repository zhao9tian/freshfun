package com.quxin.freshfun.service.impl.wechat;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClientRequestHandler extends PrepayIdRequestHandler {

	public ClientRequestHandler() {
		super();
	}

	public String getJsonBody() {
		StringBuilder sb = new StringBuilder();
		Set<Object> es = super.getAllParameters().entrySet();
		Iterator<Object> it = es.iterator();
		sb.append('{');
		while (it.hasNext()) {
			Map.Entry<String,String> entry = (Map.Entry<String,String>) it.next();
			String k = entry.getKey();
			String v = entry.getValue();
			sb.append("\"" + k + "\":\"" + v + "\",");
		}
		String reqPars = sb.substring(0, sb.lastIndexOf(","));
		reqPars+='}';
		return reqPars;
	}
}
