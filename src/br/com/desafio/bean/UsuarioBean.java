package br.com.desafio.bean;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.desafio.controlador.Controlador;
import br.com.desafio.dao.UsuarioDAO;
import br.com.desafio.model.Telefone;
import br.com.desafio.model.Usuario;
import br.com.desafio.util.Constantes;

@Named("usuarioBean")
@RequestScoped
public class UsuarioBean extends AbstractBean {

	private static final long serialVersionUID = 1L;

	@Inject
	private Controlador controlador;

	private Usuario usuario;

	private Telefone telefone1;

	private Telefone telefone2;

	private String confirmaSenha;

	private String senhaTelaLogin;

	private List<Usuario> usuarios;

	@Inject
	private UsuarioDAO usuarioDAO;

	/**
	 * Realiza logon e abre a página de cadastro de usuários
	 * Em caso de primeiro acesso, "admin" deve ser utilizado nos campos usuário e senha
	 */
	public String login() {
		FacesContext context = FacesContext.getCurrentInstance();
		Usuario user = null;

		// Verifica se usuário/senha são admin/admin. Se não forem, verifica se os campos estão vazios. 
		if (!usuario.getNome().equals(Constantes.ADMIN) && !senhaTelaLogin.equals(Constantes.ADMIN) | usuario.getNome().equals("") && senhaTelaLogin.equals("")) {

			context.addMessage(MESSAGE, new FacesMessage(ERRO, Constantes.MENSAGEM_ERRO_USUARIO_SENHA));

		} else {
			if (usuario.getNome().equals(Constantes.ADMIN) && senhaTelaLogin.equals(Constantes.ADMIN)) {
				return redirect(Constantes.USUARIO);
			} else {
				user = usuarioDAO.findUserByName(usuario.getNome());
				if (user != null & user.getSenha().equals(senhaTelaLogin)) {
					return redirect(Constantes.USUARIO);

				} else {
					context.addMessage(MESSAGE, new FacesMessage(ERRO, Constantes.MENSAGEM_ERRO_USUARIO_SENHA));
				}
			}
		}
		return null;
	}

	/**
	 *  Limpa o bean
	 */
	public String cancelar() {
		this.usuario = null;
		this.telefone1 = null;
		this.telefone2 = null;
		return null;
	}

	/**
	 * Altera os dados de um usuário, incluindo seus telefones
	 */
	public String alterar(Integer pId) {
		FacesContext context = FacesContext.getCurrentInstance();

		try {
			this.usuario = controlador.findUserById(pId);

			if (usuario.getTelefones().size() == 1) {
				telefone1 = usuario.getTelefones().get(0);
				telefone2 = null;
			}

			if (usuario.getTelefones().size() == 2) {
				telefone1 = usuario.getTelefones().get(0);
				telefone2 = usuario.getTelefones().get(1);
			}
		} catch (Exception e) {
			context.addMessage(MESSAGE, new FacesMessage(ERRO, Constantes.MENSAGEM_ERRO_CARREGAR_USUARIO));
		}
		return null;
	}

	/**
	 * Exclui um usuário
	 */
	public String excluir(Integer pId) {
		FacesContext context = FacesContext.getCurrentInstance();

		try {
			controlador.excluirUsuario(pId);
		} catch (Exception e) {
			context.addMessage(MESSAGE, new FacesMessage(ERRO, Constantes.MENSAGEM_ERRO_EXCLUIR_USUARIO));
		}
		return redirect(Constantes.USUARIO);
	}

	public String excluirTelefone(Integer pId) {
		FacesContext context = FacesContext.getCurrentInstance();

		try {
			controlador.excluirTelefone(pId);
		} catch (Exception e) {
			context.addMessage(MESSAGE, new FacesMessage(ERRO, Constantes.MENSAGEM_ERRO_EXCLUIR_TELEFONE));
		}
		return redirect(Constantes.USUARIO);
	}

	/**
	 * Salva um usuário e seus respectivos telefones
	 */
	public String salvar() {
		FacesContext context = FacesContext.getCurrentInstance();

		try {
			if (!usuario.getSenha().equalsIgnoreCase(confirmaSenha)) {
				context.addMessage(MESSAGE, new FacesMessage(ERRO, Constantes.MENSAGEM_ERRO_SENHAS_DIFERENTES));
				return null;
			}

			// Inclui usuário se o ID dele for null, altera se for válido.
			if (usuario.getId() == null) {
				controlador.salvarUsuario(usuario);

			} else {
				controlador.alterarUsuario(usuario);
			}
			
			incluiAlteraTelefone(telefone1);
			incluiAlteraTelefone(telefone2);

			return redirect(Constantes.USUARIO);

		} catch (Exception e) {
			context.addMessage(MESSAGE, new FacesMessage(ERRO, Constantes.MENSAGEM_ERRO_ALTERAR_USUARIO));
			return null;
		}

	}

	/**
	 * Inclui telefone e vincula ao usuário se o ID do telefone for null.
	 * Altera o telefone se os dados forem válidos.
	 */
	public void incluiAlteraTelefone(Telefone pTelefone) {
		FacesContext context = FacesContext.getCurrentInstance();

		try {
			if (pTelefone.getId() == null && (pTelefone.getNumero().length() > 7)) {
				pTelefone.setUsuario(usuario);
				controlador.salvarTelefone(pTelefone);
			} else {
				if (validaTelefone(pTelefone)) {
					pTelefone.setUsuario(usuario);
					controlador.alterarTelefone(pTelefone);
				}
			}
		} catch (Exception e) {
			context.addMessage(MESSAGE, new FacesMessage(ERRO, Constantes.MENSAGEM_ERRO_OPERACAO));
		}
	}

	public boolean validaTelefone(Telefone pTelefone) {
		if (pTelefone.getDdd() == null && pTelefone.getNumero().equals("") && pTelefone.getTipo().equals("")) {
			return false;

		} else if (pTelefone.getDdd() != null && (pTelefone.getNumero().length() > 7)) {
			return true;

		} else {
			return false;
		}
	}

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

	public Usuario getUsuario() {

		if (usuario == null) {
			usuario = new Usuario();
		}

		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Telefone getTelefone1() {
		if (telefone1 == null) {
			telefone1 = new Telefone();
		}
		return telefone1;
	}

	public void setTelefone1(Telefone telefone1) {
		this.telefone1 = telefone1;
	}

	public Telefone getTelefone2() {
		if (telefone2 == null) {
			telefone2 = new Telefone();
		}
		return telefone2;
	}

	public void setTelefone2(Telefone telefone2) {
		this.telefone2 = telefone2;
	}

	public List<Usuario> getUsuarios() {
		if (usuarios == null) {
			usuarios = usuarioDAO.listarUsuarios();
		}

		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public String getSenhaTelaLogin() {
		return senhaTelaLogin;
	}

	public void setSenhaTelaLogin(String senhaTelaLogin) {
		this.senhaTelaLogin = senhaTelaLogin;
	}
}