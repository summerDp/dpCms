package org.summer.dp.cms.helper.json;

import java.util.Arrays;

public class JsonPath {
	public String fieldPaths[] = null;
	public int maxDepth = 0;
	public int whichFieldPath = JsonUtil.NOT_FOUND;  // 当前
	public boolean isRoot = true;
	public byte strategy = JsonUtil.NOT_JUDGED;
	public boolean isVisited[];
	
	public JsonPath(String[] fieldPaths, int depth, int maxDepth) {
		super();
		this.fieldPaths = fieldPaths;
		this.maxDepth = maxDepth;
		this.isVisited  = new boolean[fieldPaths.length];
		Arrays.fill(this.isVisited, true);
	}
	
	public boolean isMaxLeaf(int depth) {
		return depth == maxDepth;
	}
	
	public boolean canVisit() {
		return isVisited[whichFieldPath];
	}
	
	public boolean contains(String name) {
		for(int i = 0, len = this.fieldPaths.length; i < len; ++i) {
			if(this.fieldPaths[i].length() > name.length()) {
				if(this.fieldPaths[i].startsWith(name)) {
					return true;
				}
			}
			else {
				if(name.startsWith(this.fieldPaths[i])) {
					return true;
				}
			}
		}
		return false;
	}
}
