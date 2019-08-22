package br.com.desafio.controlador;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.desafio.model.Telefone;
import br.com.desafio.model.Usuario;

/**
 * Session Bean implementation class ControladorConsumoBean
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ControladorBean implements Controlador {

	@PersistenceContext
	private EntityManager em;

	public boolean salvarUsuario(Usuario pUsuario) throws Exception {

		try {
			// Grava o objeto Usuario
			em.persist(pUsuario);
			return true;

		} catch (Exception e) {
			// Retorna o erro para o método que o invocou
			throw(e);
		}
	}

	public boolean excluirUsuario(Integer pId) {
		Usuario user = null;
		try {
			
			user = em.find(Usuario.class, pId);
			em.remove(user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Usuario alterarUsuario(Usuario pUsuario) {
		Usuario user =  null;
		try {
			user = em.merge(pUsuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	public Usuario findUserById(Integer pId) {
		Usuario usuario = null;
		try {
			usuario = em.find(Usuario.class, pId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return usuario;
	}

	@Override
	public boolean excluirTelefone(Integer pId) {
		try {
			em.createQuery("delete from Telefone where id = :id").setParameter("id", pId).executeUpdate();
			flushAndClear();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean salvarTelefone(Telefone pTelefone) {
		try {
			em.persist(pTelefone);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Telefone alterarTelefone(Telefone pTelefone) throws Exception {
		Telefone tel = null;
		try {
			tel = em.merge(pTelefone);
			return tel;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tel;
	}
	
	void flushAndClear() {
	    em.flush();
	    em.clear();
	}
}