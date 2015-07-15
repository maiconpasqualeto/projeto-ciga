/**
 * 
 */
package br.com.sixinf.ciga.entidades;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.sixinf.ferramentas.persistencia.Entidade;

/**
 * @author maicon
 *
 */
@Entity
@Table(name="noticia")
public class Noticia implements Entidade, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="seqNoticia", sequenceName="noticia_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqNoticia")
	@Column(name="id")
	private Long id;
	
	@Column(name="titulo")
	private String titulo;
	
	@Column(name="descricao")
	private String descricao;
	
	@Column(name="fonte")
	private String fonte;
	
	@Column(name="link")
	private String link;
	
	@Column(name="data_hora")
	@Temporal(TemporalType.TIMESTAMP)
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
		DateFormat df = new SimpleDateFormat("dd MMMMM YYYY - HH'hs'", new Locale("pt", "BR"));
		return df.format(dataHora);
	}

	@Override
	public Long getIdentificacao() {
		return id;
	}
}
