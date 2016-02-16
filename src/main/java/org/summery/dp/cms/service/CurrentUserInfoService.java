package org.summery.dp.cms.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.summery.dp.cms.entity.base.Account;
import org.summery.dp.cms.entity.base.Employee;
import org.summery.dp.cms.entity.base.EmployeePosts;
import org.summery.dp.cms.entity.base.Post;
import org.summery.dp.cms.repository.AccountRepository;
import org.summery.dp.cms.repository.DepartmentRepository;
import org.summery.dp.cms.repository.EmployeeRepository;
import org.summery.dp.cms.vo.CurrentInfo;

/**
 * 当前登录用户信息
 * 部门  岗位 公司  访问页面权限 岗位权限  
 * @author 赵宝东
 *
 */
@Service
public class CurrentUserInfoService {
	@Resource
	AccountRepository accountRepository ;
	@Resource
	EmployeeRepository employeeRepository ;
	@Resource
	DepartmentRepository departmentRepository ;

	/**
	 * 
	 * @param userId 
	 * @return 页面使用的VO
	 */
	@SuppressWarnings("null")
	public CurrentInfo findCurrentUserInfo(Account account){

		Employee employee = null;
		Set<Post> postList = null;

		//根据USER_ID 获取员工         USER_ID有唯一索引 
		employee = employeeRepository.findByAccountId(account.getId());
		
		if(employee!=null){
			Set<EmployeePosts> employeePostSet = employee.getEmployeePostses();
			account = employee.getAccount();
			if(!employeePostSet.isEmpty()){
				postList=new HashSet<Post>();
				Iterator<EmployeePosts> it = employeePostSet.iterator();
				if(it.hasNext()){
					EmployeePosts employeePosts = it.next();
					postList.add(employeePosts.getPost());
				}
			}
		}
		return new CurrentInfo(account,employee,postList);
	}
}
