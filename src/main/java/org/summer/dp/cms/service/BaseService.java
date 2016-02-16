package org.summer.dp.cms.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * Entity 的一些批量操作
 * @author 赵宝东
 *
 */
public class BaseService<T> {
	@PersistenceContext 
	protected EntityManager em;

	/**
	 * 批量持久化
	 * @param accountList
	 */
	@Transactional
	public void saveBatch(List<T> accountList){
		int i=0 ;
		for(;i<accountList.size();i++){
			em.persist(accountList.get(i));
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
	 * @param accountList
	 */
	@Transactional
	public void updateBatch(List<T> list){
		int i=0 ;
		for(;i<list.size();i++){
			em.merge(list.get(i));
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
	

}
