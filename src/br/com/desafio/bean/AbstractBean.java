package br.com.desafio.bean;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("serial")
public class AbstractBean implements Serializable {
	
	public static String ERRO = "Erro";
	public static String MESSAGE = "message";
	public static String SUCESSO = "Sucesso";

	/**
	 * Adiciona uma mensagem ao request para ser exibida na tela.
	 */
	protected void addMessageToRequest(String mensagem) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		request.setAttribute(MESSAGE, mensagem);
	}

	/**
	 * Usa faces-redirect para atualizar a tela.
	 */
	protected String redirect(String outcome) {
		return outcome + "?faces-redirect=true";
	}
}