package br.com.desafio.controlador;

import javax.ejb.Local;

import br.com.desafio.model.Telefone;
import br.com.desafio.model.Usuario;

@Local
public interface Controlador {
	
	/*
	 * Métodos do Usuario
	 */
	public boolean salvarUsuario(Usuario pUsuario) throws Exception;
	
	public boolean excluirUsuario(Integer pId) throws Exception;
	
	public Usuario alterarUsuario(Usuario pUsuario) throws Exception;
	
	public Usuario findUserById(Integer pId) throws Exception;
	
	/*
	 * Métodos do Telefone
	 */
	public boolean salvarTelefone(Telefone pTelefone) throws Exception;
	
	public boolean excluirTelefone(Integer pId) throws Exception;

	public Telefone alterarTelefone(Telefone pTelefone) throws Exception;
	
}
