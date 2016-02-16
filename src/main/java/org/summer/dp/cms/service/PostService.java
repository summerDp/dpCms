package org.summer.dp.cms.service;

import java.util.Date;
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
import org.summer.dp.cms.entity.base.Post;
import org.summer.dp.cms.helper.string.ArrayToString;
import org.summer.dp.cms.repository.PostRepository;
import org.summer.dp.cms.support.PageRequest;
import org.summer.dp.cms.support.persistence.DynamicSpecifications;
import org.summer.dp.cms.support.persistence.SearchFilter;
import org.summer.dp.cms.vo.CurrentInfo;


@Component
@Transactional
@Service("postService")
public class PostService {
	@Resource
	PostRepository postRepository;

	@PersistenceContext 
	private EntityManager em;
	/**
	 * 批量删除
	 * @param ids
	 */
	public void deletes(Long[] ids) {
		String inString = ArrayToString.toString(ids, "in");
		Query query = em.createQuery("delete from Post b where b.id "+inString);
		query.executeUpdate();
	}

	/**
	 * 批量持久化
	 * @param postList
	 */
	public void saveBatch(List<Post> postList){
		int i=0 ;
		for(;i<postList.size();i++){
			em.persist(postList.get(i));
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
	 * @param postList
	 */
	public void updateBatch(List<Post> postList){
		int i=0 ;
		for(;i<postList.size();i++){
			em.merge(postList.get(i));
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
	
	public Page<Post> findPage(Pageable pageRequest,Map<String, Object> searchParams){
		Map<String, SearchFilter> filters = SearchFilter.parse2Filter(searchParams);
		Specification<Post> spec = DynamicSpecifications.bySearchFilter(filters.values(), Post.class);
		return postRepository.findAll(spec,pageRequest);
	}
	
	public void delete(long id){
		postRepository.delete(id);
	}

	public Post getOne(Long id){
		return postRepository.findOne(id);
	}
	
	
	public Post save(Post post, CurrentInfo currentInfo){
		if(post.getId()==null){
			post.setAddDate(new Date());
			post.setAddUserId(currentInfo.getAccount().getId());
		}else{
			post.setModifyDate(new Date());
			post.setModifyUserId(currentInfo.getAccount().getId());
		}

		post  =  postRepository.save(post);
		return post;
	}
	
	
	public Page<Post> findPage(Map<String, Object> searchParams, PageRequest pageRequest){
		Map<String, SearchFilter> filters = SearchFilter.parse2Filter(searchParams);
		Specification<Post> spec = DynamicSpecifications.bySearchFilter(filters.values(), Post.class);
		return postRepository.findAll(spec,pageRequest);
	}
}
