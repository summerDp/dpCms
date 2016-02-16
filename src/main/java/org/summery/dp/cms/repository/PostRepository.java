package org.summery.dp.cms.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.summery.dp.cms.entity.base.Post;

public interface PostRepository extends PagingAndSortingRepository<Post, Long>, JpaSpecificationExecutor<Post>{

}
