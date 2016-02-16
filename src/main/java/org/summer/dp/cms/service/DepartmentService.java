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
import org.summer.dp.cms.entity.base.Department;
import org.summer.dp.cms.helper.string.ArrayToString;
import org.summer.dp.cms.repository.DepartmentRepository;
import org.summer.dp.cms.support.PageRequest;
import org.summer.dp.cms.support.persistence.DynamicSpecifications;
import org.summer.dp.cms.support.persistence.SearchFilter;
import org.summer.dp.cms.vo.CurrentInfo;


@Component
@Transactional
@Service("departmentService")
public class DepartmentService {
	@Resource
	DepartmentRepository departmentRepository;

	@PersistenceContext 
	private EntityManager em;
	/**
	 * 批量删除
	 * @param ids
	 */
	public void deletes(Long[] ids) {
		String inString = ArrayToString.toString(ids, "in");
		Query query = em.createQuery("delete from Department b where b.id "+inString);
		query.executeUpdate();
	}

	/**
	 * 批量持久化
	 * @param departmentList
	 */
	public void saveBatch(List<Department> departmentList){
		int i=0 ;
		for(;i<departmentList.size();i++){
			em.persist(departmentList.get(i));
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
	 * @param departmentList
	 */
	public void updateBatch(List<Department> departmentList){
		int i=0 ;
		for(;i<departmentList.size();i++){
			em.merge(departmentList.get(i));
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
	
	public Page<Department> findPage(Pageable pageRequest,Map<String, Object> searchParams){
		Map<String, SearchFilter> filters = SearchFilter.parse2Filter(searchParams);
		Specification<Department> spec = DynamicSpecifications.bySearchFilter(filters.values(), Department.class);
		return departmentRepository.findAll(spec,pageRequest);
	}
	
	public void delete(long id){
		departmentRepository.delete(id);
	}

	public Department getOne(Long id){
		return departmentRepository.findOne(id);
	}
	
	
	public Department save(Department department, CurrentInfo currentInfo){
		if(department.getId()==null){
			department.setAddDate(new Date());
			department.setAddUserId(currentInfo.getAccount().getId());
		}else{
			department.setModifyDate(new Date());
			department.setModifyUserId(currentInfo.getAccount().getId());
		}

		department  =  departmentRepository.save(department);
		return department;
	}
	
	
	public Page<Department> findPage(Map<String, Object> searchParams, PageRequest pageRequest){
		Map<String, SearchFilter> filters = SearchFilter.parse2Filter(searchParams);
		Specification<Department> spec = DynamicSpecifications.bySearchFilter(filters.values(), Department.class);
		return departmentRepository.findAll(spec,pageRequest);
	}
}
