package org.summer.dp.cms.helper.json.node;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class TreePath {
	public int maxDepth = Integer.MIN_VALUE;
	public Map<String, TreeNode> roots = new HashMap<String, TreeNode>();
	
	public TreePath() { }
	public TreePath(String paths[]) {
		addAll(paths);
	}

	private void add(String paths[], TreeNode parent, int depth) {
		if(paths == null || paths.length <= depth) {
			// 避免深度溢出
			return;
		}
		TreeNode temp = parent.children.get(paths[depth]);
		if(temp == null) { // 找不到入口
			temp = new TreeNode();
			parent.children.put(paths[depth], temp);
			for(int i = depth + 1, l = paths.length; i < l; ++i) {
				TreeNode n = new TreeNode();
				temp.children.put(paths[i], n);
				temp = n;
			}
		}
		else {
			add(paths, temp, depth + 1);
		}
	}
	
	public void add(String path) {
		String paths[] = StringUtils.split(path, '.');
		if(maxDepth < paths.length) { maxDepth = paths.length; }
		Map<String, TreeNode> nodes = this.roots;
		int depth = 0;
		TreeNode temp = nodes.get(paths[depth]);
		if(temp == null) { // 找不到入口
			temp = new TreeNode();
			this.roots.put(paths[depth], temp);
			for(int i = depth + 1, l = paths.length; i < l; ++i) {
				TreeNode n = new TreeNode();
				temp.children.put(paths[i], n);
				temp = n;
			}
		}
		else {
			add(paths, temp, depth + 1);
		}
	}
	
	public void addAll(String paths[]) {
		for(String path : paths) { this.add(path); }
	}
	
	/*public static void main(String[] args) {
		TreePath tp = new TreePath();
		tp.add("n1.n2.n3");
		tp.add("n1.n4");
		System.out.println(JSON.toJSONString(tp));
		System.out.println(NewJsonUtil.toString(tp, new String[]{}));
	}*/
}
