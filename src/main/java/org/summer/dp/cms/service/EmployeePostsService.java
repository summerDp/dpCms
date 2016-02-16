package org.summer.dp.cms.service;

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
import org.summer.dp.cms.entity.base.EmployeePosts;
import org.summer.dp.cms.helper.string.ArrayToString;
import org.summer.dp.cms.repository.EmployeePostsRepository;
import org.summer.dp.cms.support.PageRequest;
import org.summer.dp.cms.support.persistence.DynamicSpecifications;
import org.summer.dp.cms.support.persistence.SearchFilter;


@Component
@Transactional
@Service("employeePostsService")
public class EmployeePostsService {
	@Resource
	EmployeePostsRepository employeePostsRepository;

	@PersistenceContext 
	private EntityManager em;
	/**
	 * 批量删除
	 * @param ids
	 */
	public void deletes(Long[] ids) {
		String inString = ArrayToString.toString(ids, "in");
		Query query = em.createQuery("delete from EmployeePosts b where b.id "+inString);
		query.executeUpdate();
	}

	/**
	 * 批量持久化
	 * @param employeePostsList
	 */
	public void saveBatch(List<EmployeePosts> employeePostsList){
		int i=0 ;
		for(;i<employeePostsList.size();i++){
			em.persist(employeePostsList.get(i));
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
	 * @param employeePostsList
	 */
	public void updateBatch(List<EmployeePosts> employeePostsList){
		int i=0 ;
		for(;i<employeePostsList.size();i++){
			em.merge(employeePostsList.get(i));
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
	
	public Page<EmployeePosts> findPage(Pageable pageRequest,Map<String, Object> searchParams){
		Map<String, SearchFilter> filters = SearchFilter.parse2Filter(searchParams);
		Specification<EmployeePosts> spec = DynamicSpecifications.bySearchFilter(filters.values(), EmployeePosts.class);
		return employeePostsRepository.findAll(spec,pageRequest);
	}
	
	public void delete(long id){
		employeePostsRepository.delete(id);
	}

	public EmployeePosts getOne(Long id){
		return employeePostsRepository.findOne(id);
	}
	
	public EmployeePosts save(EmployeePosts employeePosts){
		employeePosts  =  employeePostsRepository.save(employeePosts);
		return employeePosts;
	}
	
	public Page<EmployeePosts> findPage(Map<String, Object> searchParams, PageRequest pageRequest){
		Map<String, SearchFilter> filters = SearchFilter.parse2Filter(searchParams);
		Specification<EmployeePosts> spec = DynamicSpecifications.bySearchFilter(filters.values(), EmployeePosts.class);
		return employeePostsRepository.findAll(spec,pageRequest);
	}
}
