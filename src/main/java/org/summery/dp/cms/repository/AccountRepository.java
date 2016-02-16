package org.summery.dp.cms.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.summery.dp.cms.entity.base.Account;

public interface AccountRepository extends PagingAndSortingRepository<Account, Long>, JpaSpecificationExecutor<Account>{
	public Account findByLoginName(String loginName);
}
