package org.summer.dp.cms.vo;

import java.util.List;
import java.util.Set;

import org.summer.dp.cms.entity.base.Account;
import org.summer.dp.cms.entity.base.Employee;
import org.summer.dp.cms.entity.base.Function;
import org.summer.dp.cms.entity.base.Post;

/**
 * 当登录用户 公司部门岗位信息
 * @author 赵宝东
 *
 */
public class CurrentInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8608567303375326512L;
	
	private Account account; 
	private Employee employee;
	private Set<Post> postList;
	private boolean needFilter = true;//是否需要过滤，这个变量登录完后才会有值
	private long defaultPostId ;
	private String indexPage ;
	private List<Function> menus;
	private List<Function> permissions;
	

	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}

	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public Set<Post> getPostList() {
		return postList;
	}
	public void setPostList(Set<Post> postList) {
		this.postList = postList;
	}
	public boolean isNeedFilter() {
		return needFilter;
	}
	public void setNeedFilter(boolean needFilter) {
		this.needFilter = needFilter;
	}
	public long getDefaultPostId() {
		return defaultPostId;
	}
	public void setDefaultPostId(long defaultPostId) {
		this.defaultPostId = defaultPostId;
	}
	public String getIndexPage() {
		return indexPage;
	}
	public void setIndexPage(String indexPage) {
		this.indexPage = indexPage;
	}
	public List<Function> getMenus() {
		return menus;
	}
	public void setMenus(List<Function> menus) {
		this.menus = menus;
	}
	public List<Function> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<Function> permissions) {
		this.permissions = permissions;
	}
	public CurrentInfo(){};
	public CurrentInfo(Account account, Employee employee, Set<Post> postList) {
		super();
		this.account = account;
		this.employee = employee;
		this.postList = postList;
	}

}
