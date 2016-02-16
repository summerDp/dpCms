package org.summer.dp.cms.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.summer.dp.cms.entity.base.Dictionary;
import org.summer.dp.cms.helper.string.ArrayToString;
import org.summer.dp.cms.repository.DictionaryRepository;
import org.summer.dp.cms.support.PageRequest;
import org.summer.dp.cms.support.persistence.DynamicSpecifications;
import org.summer.dp.cms.support.persistence.OrderByList;
import org.summer.dp.cms.support.persistence.SearchFilter;


@Component
@Transactional
@Service("dictionaryService")
public class DictionaryService {
	@Resource
	DictionaryRepository dictionaryRepository;

	@PersistenceContext 
	private EntityManager em;
	/**
	 * 批量删除
	 * @param ids
	 */
	public void deletes(Long[] ids) {
		String inString = ArrayToString.toString(ids, "in");
		Query query = em.createQuery("delete from Dictionary b where b.id "+inString);
		query.executeUpdate();
	}

	/**
	 * 批量持久化
	 * @param dictionaryList
	 */
	public void saveBatch(List<Dictionary> dictionaryList){
		int i=0 ;
		for(;i<dictionaryList.size();i++){
			em.persist(dictionaryList.get(i));
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
	 * @param dictionaryList
	 */
	public void updateBatch(List<Dictionary> dictionaryList){
		int i=0 ;
		for(;i<dictionaryList.size();i++){
			em.merge(dictionaryList.get(i));
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
	
	public void delete(long id){
		dictionaryRepository.delete(id);
	}

	public Dictionary getOne(Long id){
		return dictionaryRepository.findOne(id);
	}
	
	public Dictionary save(Dictionary dictionary){
		dictionary  =  dictionaryRepository.save(dictionary);
		return dictionary;
	}
	
	public Page<Dictionary> findPage(Map<String, Object> searchParams, PageRequest pageRequest){
		Map<String, SearchFilter> filters = SearchFilter.parse2Filter(searchParams);
		Specification<Dictionary> spec = DynamicSpecifications.bySearchFilter(filters.values(), Dictionary.class);
		return dictionaryRepository.findAll(spec,pageRequest);
	}
	
	public List<Dictionary> findByType(int type){
		return dictionaryRepository.findByType(type);
	}
	
	public Page<Dictionary> findPage(Map<String, Object> searchParams, PageRequest pageRequest, OrderByList orderByList){
		Map<String, SearchFilter> filters = SearchFilter.parse2Filter(searchParams);
		Specification<Dictionary> spec = (orderByList == null)? DynamicSpecifications.bySearchFilter(filters.values(), Dictionary.class) :
				DynamicSpecifications.bySearchFilter(filters.values(), Dictionary.class, false, orderByList.getOrderParams());
		return dictionaryRepository.findAll(spec, pageRequest);
	}
	
	public List<Dictionary> findList(Map<String, Object> searchParams, OrderByList orderByList){
		Map<String, SearchFilter> filters = SearchFilter.parse2Filter(searchParams);
		Specification<Dictionary> spec = (orderByList == null)? DynamicSpecifications.bySearchFilter(filters.values(), Dictionary.class) 
				: DynamicSpecifications.bySearchFilter(filters.values(), Dictionary.class, false, orderByList.getOrderParams());
		return dictionaryRepository.findAll(spec);
	}
	
	public List<Dictionary> findAll() {
		
		return dictionaryRepository.findAll();
	}
}
