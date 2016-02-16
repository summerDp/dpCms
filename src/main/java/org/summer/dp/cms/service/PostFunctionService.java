package org.summer.dp.cms.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.summer.dp.cms.entity.base.Function;
import org.summer.dp.cms.entity.base.PostFunction;
import org.summer.dp.cms.helper.string.ArrayToString;
import org.summer.dp.cms.repository.FunctionRepository;
import org.summer.dp.cms.repository.PostFunctionRepository;
import org.summer.dp.cms.repository.PostRepository;
import org.summer.dp.cms.support.PageRequest;
import org.summer.dp.cms.support.persistence.DynamicSpecifications;
import org.summer.dp.cms.support.persistence.SearchFilter;


@Component
@Transactional
@Service("postFunctionService")
public class PostFunctionService {
	@Resource
	PostFunctionRepository postFunctionRepository;
	
	@Resource
	PostRepository postRepository;
	
	@Resource
	FunctionRepository functionRepository;

	@PersistenceContext 
	private EntityManager em;
	/**
	 * 批量删除
	 * @param ids
	 */
	public void deletes(Long[] ids) {
		String inString = ArrayToString.toString(ids, "in");
		Query query = em.createQuery("delete from PostFunction b where b.id "+inString);
		query.executeUpdate();
	}

	/**
	 * 批量持久化
	 * @param postFunctionList
	 */
	public void saveBatch(List<PostFunction> postFunctionList){
		int i=0 ;
		for(;i<postFunctionList.size();i++){
			em.persist(postFunctionList.get(i));
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
	 * @param postFunctionList
	 */
	public void updateBatch(List<PostFunction> postFunctionList){
		int i=0 ;
		for(;i<postFunctionList.size();i++){
			em.merge(postFunctionList.get(i));
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
	
	public Page<PostFunction> findPage(Pageable pageRequest,Map<String, Object> searchParams){
		Map<String, SearchFilter> filters = SearchFilter.parse2Filter(searchParams);
		Specification<PostFunction> spec = DynamicSpecifications.bySearchFilter(filters.values(), PostFunction.class);
		return postFunctionRepository.findAll(spec,pageRequest);
	}
	
	public void delete(long id){
		postFunctionRepository.delete(id);
	}

	public PostFunction getOne(Long id){
		return postFunctionRepository.findOne(id);
	}
	
	public PostFunction save(PostFunction postFunction){
		postFunction  =  postFunctionRepository.save(postFunction);
		return postFunction;
	}
	
	public Page<PostFunction> findPage(Map<String, Object> searchParams, PageRequest pageRequest){
		Map<String, SearchFilter> filters = SearchFilter.parse2Filter(searchParams);
		Specification<PostFunction> spec = DynamicSpecifications.bySearchFilter(filters.values(), PostFunction.class);
		return postFunctionRepository.findAll(spec,pageRequest);
	}
	
	/**
	 * 登录页面使用，获取登录用户的菜单列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Function> findDefaultPostFuntions(long postId){
		return SearchFilter.query("postFunctions.post.id="+postId + "&postFunctions.isMenu=1", Function.class, functionRepository);
	}
}
