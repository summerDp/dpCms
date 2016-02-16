package org.summer.dp.cms.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.summer.dp.cms.entity.base.Dictionary;

public interface DictionaryRepository extends PagingAndSortingRepository<Dictionary, Long>, JpaSpecificationExecutor<Dictionary>{
	
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	public List<Dictionary> findByType(int type);
	
	public List<Dictionary> findAll();
}
