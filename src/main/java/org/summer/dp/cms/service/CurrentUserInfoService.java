package org.summer.dp.cms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.summer.dp.cms.entity.base.Account;
import org.summer.dp.cms.entity.base.Department;
import org.summer.dp.cms.entity.base.Employee;
import org.summer.dp.cms.entity.base.EmployeePosts;
import org.summer.dp.cms.entity.base.Function;
import org.summer.dp.cms.entity.base.Post;
import org.summer.dp.cms.entity.base.PostFunction;
import org.summer.dp.cms.repository.AccountRepository;
import org.summer.dp.cms.repository.DepartmentRepository;
import org.summer.dp.cms.repository.EmployeeRepository;
import org.summer.dp.cms.repository.FunctionRepository;
import org.summer.dp.cms.support.persistence.DynamicSpecifications;
import org.summer.dp.cms.support.persistence.SearchFilter;
import org.summer.dp.cms.vo.CurrentInfo;
import org.summer.dp.cms.vo.Menu;
import org.summer.dp.cms.vo.Permission;

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
	@Resource
	FunctionRepository functionRepository;

	/**
	 * 
	 * @param userId 
	 * @return 页面使用的VO
	 */
	@SuppressWarnings("null")
	public CurrentInfo findCurrentUserInfo(Account account){

		Employee employee = null;
		Set<Post> postList = null;
		List<Function> menus = new ArrayList<Function>();
		List<Function> permissions = new ArrayList<Function>();
		
		//根据USER_ID 获取员工         USER_ID有唯一索引 
		employee = employeeRepository.findByAccountId(account.getId());
		
		if(employee!=null){
			Set<EmployeePosts> employeePostSet = employee.getEmployeePostses();
			account = employee.getAccount();
			if(!employeePostSet.isEmpty()){
				postList=new HashSet<Post>();
				Iterator<EmployeePosts> it = employeePostSet.iterator();
				while(it.hasNext()){
					EmployeePosts employeePosts = it.next();
					postList.add(employeePosts.getPost());
					
					Map<String, Object> searchParams = new HashMap<String, Object>();
					searchParams.put("postFunctions.post.id", employeePosts.getPost().getId());
					Map<String, SearchFilter> filters = SearchFilter.parse2Filter(searchParams);
					Specification<Function> spec = DynamicSpecifications.bySearchFilter(filters.values(), Function.class);
					Iterable<Function> iterableFunction =  functionRepository.findAll(spec);
					Iterator<Function> itFunction = iterableFunction.iterator();
					while(itFunction.hasNext()){
						Function function = itFunction.next();
						if(function.getIsMenu()>=1){
							menus.add(function);
						}else{
							permissions.add(function);
						}
					}
				}
			}
		}
		
		return new CurrentInfo(account,employee,postList);
	}
}
