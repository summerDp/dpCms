package org.summer.dp.cms.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.summer.dp.cms.entity.base.Function;

public interface FunctionRepository extends PagingAndSortingRepository<Function, Long>, JpaSpecificationExecutor<Function>{
   
}
