package org.summer.dp.cms.vo;

import java.util.Set;

import org.summer.dp.cms.entity.base.Account;
import org.summer.dp.cms.entity.base.Employee;
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
	private Employee emplpyee;
	private Set<Post> postList;
	private boolean needFilter = true;//是否需要过滤，这个变量登录完后才会有值
	private long defaultPostId ;
	private String indexPage ;
	
	
	

	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Employee getEmplpyee() {
		return emplpyee;
	}
	public void setEmplpyee(Employee emplpyee) {
		this.emplpyee = emplpyee;
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
	public CurrentInfo(){};
	public CurrentInfo(Account account, Employee emplpyee, Set<Post> postList) {
		super();
		this.account = account;
		this.emplpyee = emplpyee;
		this.postList = postList;
	}

}
