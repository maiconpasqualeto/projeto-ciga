/**
 * 
 */
package br.com.sixinf.ciga;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * @author maicon
 *
 */
@ManagedBean(name="principalBean")
@ViewScoped
public class PrincipalBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Noticia> noticias = new ArrayList<Noticia>();
	private String contatoNome;
	private String contatoEmail;
	private String contatoAssunto;
	private String contatoMensagem;
	
	@PostConstruct
	public void init() {
		noticias = CigaFacade.getInstance().buscarNoticias();
	}
	
	public void enviarEmailContato() {
		
	}

	public List<Noticia> getNoticias() {
		return noticias;
	}

	public void setNoticias(List<Noticia> noticias) {
		this.noticias = noticias;
	}

	public String getContatoNome() {
		return contatoNome;
	}

	public void setContatoNome(String contatoNome) {
		this.contatoNome = contatoNome;
	}

	public String getContatoEmail() {
		return contatoEmail;
	}

	public void setContatoEmail(String contatoEmail) {
		this.contatoEmail = contatoEmail;
	}

	public String getContatoAssunto() {
		return contatoAssunto;
	}

	public void setContatoAssunto(String contatoAssunto) {
		this.contatoAssunto = contatoAssunto;
	}

	public String getContatoMensagem() {
		return contatoMensagem;
	}

	public void setContatoMensagem(String contatoMensagem) {
		this.contatoMensagem = contatoMensagem;
	}

}
