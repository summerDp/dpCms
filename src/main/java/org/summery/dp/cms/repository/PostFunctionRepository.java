package org.summery.dp.cms.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.summery.dp.cms.entity.base.PostFunction;

public interface PostFunctionRepository extends PagingAndSortingRepository<PostFunction, Long>, JpaSpecificationExecutor<PostFunction>{

}
