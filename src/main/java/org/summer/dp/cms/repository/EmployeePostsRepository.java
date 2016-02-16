package org.summer.dp.cms.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.summer.dp.cms.entity.base.EmployeePosts;

public interface EmployeePostsRepository extends PagingAndSortingRepository<EmployeePosts, Long>, JpaSpecificationExecutor<EmployeePosts>{

}
