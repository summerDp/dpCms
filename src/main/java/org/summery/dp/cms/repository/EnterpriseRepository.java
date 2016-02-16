package org.summery.dp.cms.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.summery.dp.cms.entity.base.Enterprise;

public interface EnterpriseRepository extends PagingAndSortingRepository<Enterprise, Long>, JpaSpecificationExecutor<Enterprise>{

}
