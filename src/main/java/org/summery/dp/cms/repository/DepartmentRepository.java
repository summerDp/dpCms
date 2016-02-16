package org.summery.dp.cms.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.summery.dp.cms.entity.base.Department;

public interface DepartmentRepository extends PagingAndSortingRepository<Department, Long>, JpaSpecificationExecutor<Department>{

}
