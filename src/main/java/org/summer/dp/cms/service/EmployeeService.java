package org.summer.dp.cms.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.summer.dp.cms.entity.base.Employee;
import org.summer.dp.cms.helper.string.ArrayToString;
import org.summer.dp.cms.repository.EmployeeRepository;
import org.summer.dp.cms.support.PageRequest;
import org.summer.dp.cms.support.persistence.DynamicSpecifications;
import org.summer.dp.cms.support.persistence.OrderByList;
import org.summer.dp.cms.support.persistence.SearchFilter;
import org.summer.dp.cms.vo.CurrentInfo;


@Component
@Transactional
@Service("employeeService")
public class EmployeeService {
	@Resource
	EmployeeRepository employeeRepository;

	@PersistenceContext 
	private EntityManager em;
	/**
	 * 批量删除
	 * @param ids
	 */
	public void deletes(Long[] ids) {
		String inString = ArrayToString.toString(ids, "in");
		Query query = em.createQuery("delete from Employee b where b.id "+inString);
		query.executeUpdate();
	}

	/**
	 * 批量持久化
	 * @param employeeList
	 */
	public void saveBatch(List<Employee> employeeList){
		int i=0 ;
		for(;i<employeeList.size();i++){
			em.persist(employeeList.get(i));
			if(i % 50 ==0){
				em.flush();
				em.clear();
			}
		}
		if(i%50!=0){
			em.flush();
			em.clear();
		}

	}
	
	/**
	 * 批量更新
	 * @param employeeList
	 */
	public void updateBatch(List<Employee> employeeList){
		int i=0 ;
		for(;i<employeeList.size();i++){
			em.merge(employeeList.get(i));
			if(i % 50 ==0){
				em.flush();
				em.clear();
			}
		}
		if(i%50!=0){
			em.flush();
			em.clear();
		}

	}
	
	public Page<Employee> findPage(Pageable pageRequest,Map<String, Object> searchParams){
		Map<String, SearchFilter> filters = SearchFilter.parse2Filter(searchParams);
		Specification<Employee> spec = DynamicSpecifications.bySearchFilter(filters.values(), Employee.class);
		return employeeRepository.findAll(spec,pageRequest);
	}
	
	public void delete(long id){
		employeeRepository.delete(id);
	}

	public Employee getOne(Long id){
		return employeeRepository.findOne(id);
	}
	
	
	public Employee save(Employee employee, CurrentInfo currentInfo){
		if(employee.getId()==null){
			employee.setAddDate(new Date());
			employee.setEmployeeByAddUserId(currentInfo.getEmployee());
		}else{
			employee.setModifyDate(new Date());
			employee.setEmployeeByModifyUserId(currentInfo.getEmployee());
		}

		employee  =  employeeRepository.save(employee);
		return employee;
	}
	
	
	public Page<Employee> findPage(Map<String, Object> searchParams, PageRequest pageRequest){
		Map<String, SearchFilter> filters = SearchFilter.parse2Filter(searchParams);
		Specification<Employee> spec = DynamicSpecifications.bySearchFilter(filters.values(), Employee.class);
		return employeeRepository.findAll(spec,pageRequest);
	}
	public void active(){
		
	}
	
	public Page<Employee> findPage(Map<String, Object> searchParams, PageRequest pageRequest, OrderByList orderByList){
		Map<String, SearchFilter> filters = SearchFilter.parse2Filter(searchParams);
		Specification<Employee> spec = (orderByList == null)? DynamicSpecifications.bySearchFilter(filters.values(), Employee.class) :
				DynamicSpecifications.bySearchFilter(filters.values(), Employee.class, false, orderByList.getOrderParams());
		return employeeRepository.findAll(spec, pageRequest);
	}
	
	public List<Employee> findList(Map<String, Object> searchParams, OrderByList orderByList){
		Map<String, SearchFilter> filters = SearchFilter.parse2Filter(searchParams);
		Specification<Employee> spec = (orderByList == null)? DynamicSpecifications.bySearchFilter(filters.values(), Employee.class) 
				: DynamicSpecifications.bySearchFilter(filters.values(), Employee.class, false, orderByList.getOrderParams());
		return employeeRepository.findAll(spec);
	}
	
	public Employee save(CurrentInfo currentInfo,Employee employee){
		employee  =  employeeRepository.save(employee);
		return employee;
	}
}
