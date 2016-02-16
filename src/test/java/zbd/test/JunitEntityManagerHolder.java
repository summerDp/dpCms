package zbd.test;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;
/**
 * 使JUNIT里的HIBERNATE SESSION 保持连接直到程序跑完为止。
 * @author 赵宝东
 *
 */
public class JunitEntityManagerHolder {
	EntityManager em = null;

	@Resource
	EntityManagerFactory emf;

	@Before
	public void doBefore(){
		this.em = emf.createEntityManager();
		EntityManagerHolder emHolder = new EntityManagerHolder(em);
		TransactionSynchronizationManager.bindResource(emf,
				emHolder);
	}

	@After
	public void doAfter(){
		EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager
				.unbindResource(emf);
	}
}
