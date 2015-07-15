/**
 * 
 */
package br.com.sixinf.ciga.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Entity;

import br.com.sixinf.ferramentas.persistencia.Entidade;

/**
 * @author maicon
 *
 */
@Entity
@Table(name="cotacao")
public class Cotacao implements Entidade, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="seqCotacao", sequenceName="cotacao_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seqCotacao")
	@Column(name="id")
	private Long id;
	
	@Column(name="uf")
	private String uf;
	
	@Column(name="praca")
	private String praca;
	
	@Column(name="valor_avista")
	private BigDecimal valorAVista;
	
	@Column(name="valor_aprazo")
	private BigDecimal valorAPrazo;
	
	@Column(name="tipo_cotacao")
	@Enumerated(EnumType.STRING)
	private TipoCotacao tipoCotacao;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getPraca() {
		return praca;
	}

	public void setPraca(String praca) {
		this.praca = praca;
	}

	public BigDecimal getValorAVista() {
		return valorAVista;
	}

	public void setValorAVista(BigDecimal valorAVista) {
		this.valorAVista = valorAVista;
	}

	public BigDecimal getValorAPrazo() {
		return valorAPrazo;
	}

	public void setValorAPrazo(BigDecimal valorAPrazo) {
		this.valorAPrazo = valorAPrazo;
	}

	public TipoCotacao getTipoCotacao() {
		return tipoCotacao;
	}

	public void setTipoCotacao(TipoCotacao tipoCotacao) {
		this.tipoCotacao = tipoCotacao;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getValorAVistaFormatado() {
		DecimalFormat df = new DecimalFormat("##");
		return df.format(getValorAVista().floatValue());
	}

	@Override
	public Long getIdentificacao() {
		return id;
	}

}
