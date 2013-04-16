package br.com.caelum.carangos.repositories;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.caelum.carangos.view.Pagina;

public abstract class Repository<T, I extends Serializable> {
	
	protected final EntityManager entityManager;
	protected final Class<T> clazz;

	protected Repository(EntityManager entityManager) {
		this.entityManager = entityManager;
		
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

		this.clazz = clazz;
	}
	
	public T create(T entity) {
		entityManager.persist(entity);
		
		return entity;
	}
	
	public T update(T entity) {
		return entityManager.merge(entity);
	}
	
	public void destroy(T entity) {
		entityManager.remove(entity);
	}
	
	public T find(I id) {
		return entityManager.find(clazz, id);
	}
	
	public List<T> findAll() {
		Query query = entityManager.createQuery("from " + clazz.getName());

		@SuppressWarnings("unchecked")
		List<T> resultList = query.getResultList();

		return resultList;
	}
	public List<T> find(Pagina pagina) {
		Query query = entityManager.createQuery("from " + clazz.getName());
		
		@SuppressWarnings("unchecked")
		List<T> resultList = query.setFirstResult(pagina.getInicio()).setMaxResults(pagina.getTamanho()).getResultList();
		
		return resultList;
	}
}