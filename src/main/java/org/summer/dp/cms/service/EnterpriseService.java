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
import org.summer.dp.cms.entity.base.Enterprise;
import org.summer.dp.cms.helper.string.ArrayToString;
import org.summer.dp.cms.repository.EnterpriseRepository;
import org.summer.dp.cms.support.PageRequest;
import org.summer.dp.cms.support.persistence.DynamicSpecifications;
import org.summer.dp.cms.support.persistence.SearchFilter;
import org.summer.dp.cms.vo.CurrentInfo;


@Component
@Transactional
@Service("enterpriseService")
public class EnterpriseService {
	@Resource
	EnterpriseRepository enterpriseRepository;

	@PersistenceContext 
	private EntityManager em;
	/**
	 * 批量删除
	 * @param ids
	 */
	public void deletes(Long[] ids) {
		String inString = ArrayToString.toString(ids, "in");
		Query query = em.createQuery("delete from Enterprise b where b.id "+inString);
		query.executeUpdate();
	}

	/**
	 * 批量持久化
	 * @param enterpriseList
	 */
	public void saveBatch(List<Enterprise> enterpriseList){
		int i=0 ;
		for(;i<enterpriseList.size();i++){
			em.persist(enterpriseList.get(i));
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
	 * @param enterpriseList
	 */
	public void updateBatch(List<Enterprise> enterpriseList){
		int i=0 ;
		for(;i<enterpriseList.size();i++){
			em.merge(enterpriseList.get(i));
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

	public Page<Enterprise> findPage(Pageable pageRequest,Map<String, Object> searchParams){
		Map<String, SearchFilter> filters = SearchFilter.parse2Filter(searchParams);
		Specification<Enterprise> spec = DynamicSpecifications.bySearchFilter(filters.values(), Enterprise.class);
		return enterpriseRepository.findAll(spec,pageRequest);
	}
	
	public void delete(long id){
		enterpriseRepository.delete(id);
	}

	public Enterprise getOne(Long id){
		return enterpriseRepository.findOne(id);
	}
	
	
	public Enterprise save(Enterprise enterprise, CurrentInfo currentInfo){
		if(enterprise.getId()==null){
			enterprise.setAddDate(new Date());
			enterprise.setAddUserId(currentInfo.getAccount().getId());
		}else{
			enterprise.setModifyDate(new Date());
			enterprise.setModifyUserId(currentInfo.getAccount().getId());
		}

		enterprise  =  enterpriseRepository.save(enterprise);
		return enterprise;
	}
	
	
	public Page<Enterprise> findPage(Map<String, Object> searchParams, PageRequest pageRequest){
		Map<String, SearchFilter> filters = SearchFilter.parse2Filter(searchParams);
		Specification<Enterprise> spec = DynamicSpecifications.bySearchFilter(filters.values(), Enterprise.class);
		return enterpriseRepository.findAll(spec,pageRequest);
	}
}
