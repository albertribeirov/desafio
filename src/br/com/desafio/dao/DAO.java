package br.com.desafio.dao;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@SuppressWarnings("serial")
@RequestScoped
public abstract class DAO implements Serializable {
	
	@PersistenceContext
	private EntityManager em;

	public <T> T carregar(Class<T> classe, Object id) {
		return em.find(classe, id);
	}

	public <T> void salvar(T entity) {
		em.persist(entity);
	}

	public <T> void alterar(T entity) {
		em.merge(entity);
	}

	public <T> void excluir(T entity) {
		em.remove(entity);
	}

	protected Query criarQuery(String query) {
		return em.createQuery(query);
	}
}