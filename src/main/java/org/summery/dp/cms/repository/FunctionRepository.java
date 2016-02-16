package org.summery.dp.cms.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.summery.dp.cms.entity.base.Function;

public interface FunctionRepository extends PagingAndSortingRepository<Function, Long>, JpaSpecificationExecutor<Function>{
 
}
