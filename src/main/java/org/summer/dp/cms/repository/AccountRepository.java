package org.summer.dp.cms.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.summer.dp.cms.entity.base.Account;

public interface AccountRepository extends PagingAndSortingRepository<Account, Long>, JpaSpecificationExecutor<Account>{
	public Account findByLoginName(String loginName);
}
