/**
 * 
 */
package br.com.sixinf.ciga;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author maicon
 *
 */
public class Noticia implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String titulo;
	
	private String descricao;
	
	private String fonte;
	
	private String destaqueTitulo;
	
	private String link;
	
	private Date dataHora;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getFonte() {
		return fonte;
	}

	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDestaqueTitulo() {
		return destaqueTitulo;
	}

	public void setDestaqueTitulo(String destaqueTitulo) {
		this.destaqueTitulo = destaqueTitulo;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	public String getDataHoraCompleta() {
		DateFormat df = new SimpleDateFormat("dd MMMMM YYYY - HH:MM", new Locale("pt", "BR"));
		return df.format(dataHora);
	}
}
