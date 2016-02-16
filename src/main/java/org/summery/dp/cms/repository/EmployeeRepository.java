package org.summery.dp.cms.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.summery.dp.cms.entity.base.Employee;

public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Long>, JpaSpecificationExecutor<Employee>{
	Employee findByAccountId(long accountId);
}
