package org.summer.dp.cms.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.summer.dp.cms.entity.base.Account;
import org.summer.dp.cms.entity.base.Employee;
import org.summer.dp.cms.helper.hash.MD5;
import org.summer.dp.cms.helper.language.Pinyin4jUtil;
import org.summer.dp.cms.helper.string.ArrayToString;
import org.summer.dp.cms.repository.AccountRepository;
import org.summer.dp.cms.repository.EmployeeRepository;
import org.summer.dp.cms.support.PageRequest;
import org.summer.dp.cms.support.persistence.DynamicSpecifications;
import org.summer.dp.cms.support.persistence.SearchFilter;
import org.summer.dp.cms.vo.CurrentInfo;


@Component
@Transactional
@Service("accountService")
public class AccountService {
	private static final Logger logger = Logger.getLogger(AccountService.class);
	@Resource AccountRepository accountRepository;
	@Resource EmployeeRepository employeeRepository;
	@Autowired private DefaultPasswordService passwordService;

	@PersistenceContext 
	private EntityManager em;
	/**
	 * 批量删除
	 * @param ids
	 */
	public void deletes(Long[] ids) {
		String inString = ArrayToString.toString(ids, "in");
		Query query = em.createQuery("delete from Account b where b.id "+inString);
		query.executeUpdate();
	}

	/**
	 * 批量持久化
	 * @param accountList
	 */
	public void saveBatch(List<Account> accountList){
		int i=0 ;
		for(;i<accountList.size();i++){
			em.persist(accountList.get(i));
			if(i % 50 ==0){
				em.flush();
				em.clear();
			}
		}
		if(i%50!=0){
			em.flush();
			em.clear();
		}

	}

	/**
	 * 批量更新
	 * @param accountList
	 */
	public void updateBatch(List<Account> accountList){
		int i=0 ;
		for(;i<accountList.size();i++){
			em.merge(accountList.get(i));
			if(i % 50 ==0){
				em.flush();
				em.clear();
			}
		}
		if(i%50!=0){
			em.flush();
			em.clear();
		}

	}

	public Page<Account> findPage(Pageable pageRequest,Map<String, Object> searchParams){
		Map<String, SearchFilter> filters = SearchFilter.parse2Filter(searchParams);
		Specification<Account> spec = DynamicSpecifications.bySearchFilter(filters.values(), Account.class);
		return accountRepository.findAll(spec,pageRequest);
	}

	public void delete(long id){
		accountRepository.delete(id);
	}

	public Account getOne(Long id){
		return accountRepository.findOne(id);
	}


	public Account save(Account account, CurrentInfo currentInfo){
		if(account.getId()==null){
			account.setAddDate(new Date());
			account.setEmployeeByAddUserId(currentInfo.getEmployee());
		}else{
			account.setModifyDate(new Date());
			account.setEmployeeByModifyUserId(currentInfo.getEmployee());
		}

		account  =  accountRepository.save(account);
		return account;
	}


	public Page<Account> findPage(Map<String, Object> searchParams, PageRequest pageRequest){
		Map<String, SearchFilter> filters = SearchFilter.parse2Filter(searchParams);
		Specification<Account> spec = DynamicSpecifications.bySearchFilter(filters.values(), Account.class);
		return accountRepository.findAll(spec,pageRequest);
	}

	public Account findByLoginName(String username) {

		return accountRepository.findByLoginName(username);
	}

	/**
	 * 根据员工名称创建一个账号
	 * @param employeeId
	 * @return
	 *         
	 */
	public Account saveEmployeeAccout(long employeeId, CurrentInfo currentInfo, Map<String, Object> params){
		Employee employee = employeeRepository.findOne(employeeId);
		Account account = null;
		String pwd = Pinyin4jUtil.genRandomPassword();
		String pinyinName = null;
		
		if((account = employee.getAccount()) != null){//已经存在帐号，那么就激活它
			if(account.getStatus()==0){ // 未激活
				account.setStatus(1);
				account.setPassword(passwordService.encryptPassword(pwd));
				employee.setAccount(account);
				employeeRepository.save(employee);
				pinyinName = Pinyin4jUtil.toHanyuPinyin(employee.getName());
				
			}
			else{
              
				logger.warn("尝试激活一个已经激活的帐号,是低能的行为");
				return null;
			}
			
		}else{//不存在帐号就进行创建帐号流程
			String name = employee.getName();
			pinyinName = Pinyin4jUtil.toHanyuPinyin(name);
			Account accountByName = accountRepository.findByLoginName(pinyinName);

			if(accountByName != null){//帐号名已经存在
				pinyinName += employee.getId();//加个ID就不会重复了
			}
			account = new Account();
			account.setLoginName(pinyinName);
			account.setPassword(MD5.MD5(pwd));
			account.setEmployeeByAddUserId(currentInfo.getEmployee());
			account.setStatus(1);
			account.setAddDate(new Date());
			account = accountRepository.save(account);
			employee.setAccount(account);//该员工关联到该帐号
			employeeRepository.save(employee);
		}
		params.put("account", pinyinName);
		params.put("password", pwd);
		return account;
	}
}
