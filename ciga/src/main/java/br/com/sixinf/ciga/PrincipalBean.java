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

import br.com.sixinf.ciga.dao.CigaDAO;
import br.com.sixinf.ciga.entidades.Cotacao;
import br.com.sixinf.ciga.entidades.Noticia;
import br.com.sixinf.ciga.entidades.TipoCotacao;

/**
 * @author maicon
 *
 */
@ManagedBean(name="principalBean")
@ViewScoped
public class PrincipalBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Noticia> noticias = new ArrayList<Noticia>();
	private Cotacao cotacaoBoi;
	private Cotacao cotacaoVaca;
	private Cotacao cotacaoSoja;
	private Cotacao cotacaoBezerro;
	private Cotacao cotacaoNovilha;
	private Cotacao cotacaoMilho;
	private String contatoNome;
	private String contatoEmail;
	private String contatoAssunto;
	private String contatoMensagem;
	
	@PostConstruct
	public void init() {
		noticias = CigaDAO.getInstance().buscarUltimasNoticias();
		/*cotacaoBoi = CigaDAO.getInstance().buscarCotacao("MS", "C. Grande", TipoCotacao.BOI_GORDO);
		cotacaoVaca = CigaDAO.getInstance().buscarCotacao("MS", "C. Grande", TipoCotacao.VACA_GORDA);
		cotacaoSoja = CigaDAO.getInstance().buscarCotacao("MS", "Dourados", TipoCotacao.SOJA_SACA);*/
		cotacaoBezerro = CigaDAO.getInstance().buscarCotacao("MS", "MS", TipoCotacao.BEZERRO_MACHO);
		cotacaoMilho = CigaDAO.getInstance().buscarCotacao("MS", "Campo Grande", TipoCotacao.MILHO_SACA);
		cotacaoNovilha = CigaDAO.getInstance().buscarCotacao("MS", "MS", TipoCotacao.NOVILHA);
	}
	
	public void enviarEmailContato() {
		contatoMensagem += "\r\n\r\nEmail: " + contatoEmail;
		
		CigaFacade.getInstance().enviarEmail(contatoNome, contatoEmail, contatoAssunto, contatoMensagem);
		
		contatoNome = null;
		contatoEmail = null;
		contatoAssunto = null;
		contatoMensagem = null;
		
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

	public Cotacao getCotacaoBoi() {
		return cotacaoBoi;
	}

	public void setCotacaoBoi(Cotacao cotacaoBoi) {
		this.cotacaoBoi = cotacaoBoi;
	}

	public Cotacao getCotacaoVaca() {
		return cotacaoVaca;
	}

	public void setCotacaoVaca(Cotacao cotacaoVaca) {
		this.cotacaoVaca = cotacaoVaca;
	}

	public Cotacao getCotacaoSoja() {
		return cotacaoSoja;
	}

	public void setCotacaoSoja(Cotacao cotacaoSoja) {
		this.cotacaoSoja = cotacaoSoja;
	}

	public Cotacao getCotacaoBezerro() {
		return cotacaoBezerro;
	}

	public void setCotacaoBezerro(Cotacao cotacaoBezerro) {
		this.cotacaoBezerro = cotacaoBezerro;
	}

	public Cotacao getCotacaoNovilha() {
		return cotacaoNovilha;
	}

	public void setCotacaoNovilha(Cotacao cotacaoNovilha) {
		this.cotacaoNovilha = cotacaoNovilha;
	}

	public Cotacao getCotacaoMilho() {
		return cotacaoMilho;
	}

	public void setCotacaoMilho(Cotacao cotacaoMilho) {
		this.cotacaoMilho = cotacaoMilho;
	}
	
}
