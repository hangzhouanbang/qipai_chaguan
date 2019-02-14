package com.anbang.qipai.chaguan.cqrs.c.domain.chaguan;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChaguanIdManager {

	// id 存放的是相同的chaguanid。考虑到线程安全和查询效率。 不使用list，set 。而是使用map
	private Map<String, String> idMap = new ConcurrentHashMap<>();

	private static char[] charsForId = new char[] { '0', '1', '2', '3', '5', '6', '7', '8', '9' };

	public String createChaguanId(long seed) {
		Random random = new Random(seed);
		String newId;
		while (true) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 6; i++) {
				int charIdx = random.nextInt(charsForId.length);
				sb.append(charsForId[charIdx]);
			}
			newId = sb.toString();
			if (idMap.containsKey(newId)) {
				continue;
			} else {
				break;
			}
		}
		idMap.put(newId, newId);
		return newId;
	}

	public Set<String> takeAllChaguanId() {
		return idMap.keySet();
	}

	public boolean hasChaguan(String chaguanId) {
		return idMap.containsKey(chaguanId);
	}

	public void removeChaguanId(String chaguanId) {
		idMap.remove(chaguanId);
	}

	public Map<String, String> getIdMap() {
		return idMap;
	}

	public void setIdMap(Map<String, String> idMap) {
		this.idMap = idMap;
	}

}
