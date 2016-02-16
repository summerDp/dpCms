package org.summer.dp.cms.helper.json.node;

import java.util.HashMap;
import java.util.Map;

public class TreeNode {
	public Map<String, TreeNode> children = new HashMap<String, TreeNode>();
	
	public TreeNode() { }

	public Map<String, TreeNode> getChildren() {
		return children == null || children.size() == 0? null : children;
	}

	public boolean isLeaf() {  // 说明到了最后一个节点
		return children.size() == 0;
	}
}
