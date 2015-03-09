package com.javaprophet.javawebserver.plugins.javaloader.security;

import java.util.LinkedHashMap;
import com.javaprophet.javawebserver.hosts.VHost;
import com.javaprophet.javawebserver.networking.packets.RequestPacket;
import com.javaprophet.javawebserver.plugins.javaloader.JavaLoaderSecurity;

public class JLSUA extends JavaLoaderSecurity {
	
	private int returnWeight = 0;
	private boolean enabled = true;
	private String[] ua = null;
	
	public void init(VHost host, LinkedHashMap<String, Object> cfg) {
		if (!cfg.containsKey("returnWeight")) cfg.put("returnWeight", "100");
		if (!cfg.containsKey("enabled")) cfg.put("enabled", "true");
		if (!cfg.containsKey("userAgents")) cfg.put("userAgents", "wordpress,sql,php,scan");
		this.returnWeight = Integer.parseInt((String)cfg.get("returnWeight"));
		this.enabled = cfg.get("enabled").equals("true");
		this.ua = ((String)cfg.get("userAgents")).split(",");
	}
	
	public void reload(LinkedHashMap<String, Object> cfg) {
		init(null, cfg);
	}
	
	@Override
	public int check(RequestPacket req) {
		if (!enabled) return 0;
		if (!req.headers.hasHeader("User-Agent")) {
			return returnWeight;
		}
		String ua = req.headers.getHeader("User-Agent").toLowerCase().trim();
		for (String mua : this.ua) {
			if (ua.contains(mua)) {
				return returnWeight;
			}
		}
		return 0;
	}
	
	@Override
	public int check(String arg0) {
		return 0;
	}
	
}