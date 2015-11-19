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
	private Cotacao cotacaoBoiCgr;
	private Cotacao cotacaoBoiDd;
	private Cotacao cotacaoBoiTresL;
	private Cotacao cotacaoVacaCgr;
	private Cotacao cotacaoVacaDd;
	private Cotacao cotacaoVacaTresL;
	private Cotacao cotacaoSoja;
	private Cotacao cotacaoBezerro;
	private Cotacao cotacaoBezerroNelore;
	private Cotacao cotacaoNovilha;
	private Cotacao cotacaoNovilhaNelore;
	private Cotacao cotacaoMilho;
	private String contatoNome;
	private String contatoEmail;
	private String contatoAssunto;
	private String contatoMensagem;
	
	@PostConstruct
	public void init() {
		noticias = CigaDAO.getInstance().buscarUltimasNoticias();
		cotacaoBoiCgr = CigaDAO.getInstance().buscarCotacao("MS", "C. Grande", TipoCotacao.BOI_GORDO);
		cotacaoBoiDd = CigaDAO.getInstance().buscarCotacao("MS", "Dourados", TipoCotacao.BOI_GORDO);
		cotacaoBoiTresL = CigaDAO.getInstance().buscarCotacao("MS", "Três Lagoas", TipoCotacao.BOI_GORDO);
		cotacaoVacaCgr = CigaDAO.getInstance().buscarCotacao("MS", "C. Grande", TipoCotacao.VACA_GORDA);
		cotacaoVacaDd = CigaDAO.getInstance().buscarCotacao("MS", "Dourados", TipoCotacao.VACA_GORDA);
		cotacaoVacaTresL = CigaDAO.getInstance().buscarCotacao("MS", "Três Lagoas", TipoCotacao.VACA_GORDA);
		cotacaoSoja = CigaDAO.getInstance().buscarCotacao("MS", "Dourados", TipoCotacao.SOJA_SACA);
		cotacaoBezerro = CigaDAO.getInstance().buscarCotacao("MS", "MS", TipoCotacao.BEZERRO_MESTICO);
		cotacaoBezerroNelore = CigaDAO.getInstance().buscarCotacao("MS", "MS", TipoCotacao.BEZERRO_NELORE);
		cotacaoMilho = CigaDAO.getInstance().buscarCotacao("MS", "Campo Grande", TipoCotacao.MILHO_SACA);
		cotacaoNovilha = CigaDAO.getInstance().buscarCotacao("MS", "MS", TipoCotacao.NOVILHA_MESTICA);
		cotacaoNovilhaNelore = CigaDAO.getInstance().buscarCotacao("MS", "MS", TipoCotacao.NOVILHA_NELORE);
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

	public Cotacao getCotacaoBoiCgr() {
		return cotacaoBoiCgr;
	}

	public void setCotacaoBoiCgr(Cotacao cotacaoBoiCgr) {
		this.cotacaoBoiCgr = cotacaoBoiCgr;
	}

	public Cotacao getCotacaoBoiDd() {
		return cotacaoBoiDd;
	}

	public void setCotacaoBoiDd(Cotacao cotacaoBoiDd) {
		this.cotacaoBoiDd = cotacaoBoiDd;
	}

	public Cotacao getCotacaoBoiTresL() {
		return cotacaoBoiTresL;
	}

	public void setCotacaoBoiTresL(Cotacao cotacaoBoiTresL) {
		this.cotacaoBoiTresL = cotacaoBoiTresL;
	}

	public Cotacao getCotacaoVacaCgr() {
		return cotacaoVacaCgr;
	}

	public void setCotacaoVacaCgr(Cotacao cotacaoVacaCgr) {
		this.cotacaoVacaCgr = cotacaoVacaCgr;
	}

	public Cotacao getCotacaoVacaDd() {
		return cotacaoVacaDd;
	}

	public void setCotacaoVacaDd(Cotacao cotacaoVacaDd) {
		this.cotacaoVacaDd = cotacaoVacaDd;
	}

	public Cotacao getCotacaoVacaTresL() {
		return cotacaoVacaTresL;
	}

	public void setCotacaoVacaTresL(Cotacao cotacaoVacaTresL) {
		this.cotacaoVacaTresL = cotacaoVacaTresL;
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

	public Cotacao getCotacaoBezerroNelore() {
		return cotacaoBezerroNelore;
	}

	public void setCotacaoBezerroNelore(Cotacao cotacaoBezerroNelore) {
		this.cotacaoBezerroNelore = cotacaoBezerroNelore;
	}

	public Cotacao getCotacaoNovilhaNelore() {
		return cotacaoNovilhaNelore;
	}

	public void setCotacaoNovilhaNelore(Cotacao cotacaoNovilhaNelore) {
		this.cotacaoNovilhaNelore = cotacaoNovilhaNelore;
	}
	
}
