/**
 * 
 */
package br.com.sixinf.ciga;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.com.sixinf.ciga.dao.CigaDAO;
import br.com.sixinf.ciga.entidades.Cotacao;
import br.com.sixinf.ciga.entidades.Noticia;
import br.com.sixinf.ciga.entidades.TipoCotacao;
import br.com.sixinf.ferramentas.log.LoggerException;

/**
 * @author maicon
 *
 */
public class CigaFacade {
	
	private static CigaFacade facade;
	
	public static CigaFacade getInstance() {
		if (facade == null)
			facade = new CigaFacade();
		return facade;
	}
	
	/**
	 * 
	 * @return
	 */
	public void atualizarNoticias() {
		
		int timeout = 15; // 15 segundos
		RequestConfig config = RequestConfig.custom()
				  .setConnectTimeout(timeout * 1000)
				  .setConnectionRequestTimeout(timeout * 1000)
				  .setSocketTimeout(timeout * 1000).build();
		CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
				
		HttpGet httpGet = new HttpGet("http://www.sba1.com/categoria/destaque-comum");
		try {
			
			CloseableHttpResponse response = httpclient.execute(httpGet);
			try {
				
			    if (response.getStatusLine().getStatusCode() == 200) {
			    
				    HttpEntity entity = response.getEntity();
				    
				    String pagina = EntityUtils.toString(entity);
				    Document doc = Jsoup.parse(pagina);
				    Elements els = doc.getElementsByTag("article");
				    SimpleDateFormat sdf = new SimpleDateFormat("dd' de 'MMM' de 'yyyy' - 'HH'h'", new Locale("pt", "BR"));
				    
				    for (Element el: els) {
				    	Elements titulos = el.getElementsByClass("title");
				    	Elements titulosELinks = titulos.get(0).getElementsByTag("a");
				    	String titulo = titulosELinks.get(0).text();
				    	String link = titulosELinks.get(0).attr("href");
				    	Elements datas = el.getElementsByClass("date");
				    	String data = datas.get(0).getElementsByTag("strong").get(0).text();
				    	Elements excerpts = el.getElementsByClass("excerpt");
				    	String excerpt = excerpts.get(0).getElementsByTag("p").get(0).text();
				    				    	
				    	Noticia n = new Noticia();			    	
				    	n.setDataHora(sdf.parse(data));
				    	n.setTitulo(titulo);
				    	n.setResumo(excerpt);
				    	n.setFonte("Sistema Brasileiro do Agronegócio");
				    	n.setLink(link);
				    	
				    	boolean jaExiteNoticia = CigaDAO.getInstance().jaExiteNoticia(n.getTitulo(), n.getDataHora());
				    	if (!jaExiteNoticia) {
				    		// http://www.sba1.com/noticias/56645/queda-na-oferta-e-maior-demanda-elevam-precos-dos-ovos
				    		String subs = link.substring(link.indexOf("/noticias/") + 10);
				    		String idNoticia = subs.substring(0, link.indexOf('/'));
				    		String texto = buscaTextoCompletoNoticia(idNoticia);
				    		if (!texto.isEmpty()) {
					    		n.setDescricao(texto);
					    		CigaDAO.getInstance().adicionar(n);
				    		}
				    	}
				    	
				    }
				    
			    }
			} catch (ParseException | LoggerException e) {
				Logger.getLogger(getClass()).error("Erro ao atualizar notícias", e);
			} finally {
			    response.close();
			}
		} catch (IOException e) {
			Logger.getLogger(getClass()).error("Erro de IO ao buscar notícias", e);
		}
		
	}
	
	
	/**
	 * 
	 * @param idNoticia
	 */
	private String buscaTextoCompletoNoticia (String idNoticia) {
		StringBuilder str = new StringBuilder();
		
		try {
			
			int timeout = 15; // 15 segundos
			
			RequestConfig config = RequestConfig.custom()
					  .setConnectTimeout(timeout * 1000)
					  .setConnectionRequestTimeout(timeout * 1000)
					  .setSocketTimeout(timeout * 1000).build();
			CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
			
			HttpGet httpGet = new HttpGet("http://www.sba1.com/noticias/" + idNoticia);
		
			
			CloseableHttpResponse response = httpclient.execute(httpGet);
			try {
				
			    if (response.getStatusLine().getStatusCode() == 200) {
			    
				    HttpEntity entity = response.getEntity();
				    
				    String pagina = EntityUtils.toString(entity);
				    Document doc = Jsoup.parse(pagina);
				    Element el = doc.getElementsByClass("single").get(0);
				    Elements els = el.getElementsByTag("p");
				    
				    for (Element e : els) {
				    	String text = e.text();
				    	if (text.startsWith("Fonte"))
				    		break;
				    	str.append(e.text() + "\r\n");
				    }
			    }
			
			} finally {
			    response.close();
			}
		} catch (IOException e) {
			Logger.getLogger(getClass()).error("Erro de IO ao buscar notícias", e);
		}
		return str.toString();
	}
	
	
	/**
	 * 
	 */
	public void atualizarCotacoes() {
		// problema para acessar sites https
		System.setProperty("jsse.enableSNIExtension", "false");
		
		atualizarCotacao(TipoCotacao.BOI_GORDO);
		atualizarCotacao(TipoCotacao.VACA_GORDA);
		atualizarCotacao(TipoCotacao.SOJA_SACA);
		
		atualizarCotacaoScotEmb(TipoCotacao.MILHO_SACA);		
		atualizarCotacaoScot(TipoCotacao.BEZERRO_MACHO);		
		atualizarCotacaoScot(TipoCotacao.NOVILHA);
				
	}
	
	/**
	 * 
	 */
	public void atualizarCotacao(TipoCotacao tipoCotacao) {
		int timeout = 15; // 15 segundos
		RequestConfig config = RequestConfig.custom()
				  .setConnectTimeout(timeout * 1000)
				  .setConnectionRequestTimeout(timeout * 1000)
				  .setSocketTimeout(timeout * 1000).build();
		CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		
		String url = "http://www.canalrural.com.br/cotacao/";
		switch (tipoCotacao) {
			case BOI_GORDO:
				url += "boi-gordo/";
				break;
			case VACA_GORDA:
				url += "vaca-gorda/";
				break;
			case SOJA_SACA:
				url += "soja/";
				break;
			default:
				throw new UnsupportedOperationException("Tipo de cotação não existe no site ");
		}
		
		HttpGet httpGet = new HttpGet(url);
		try {
			
			CloseableHttpResponse response = httpclient.execute(httpGet);
			try {
				
			    if (response.getStatusLine().getStatusCode() == 200) {
			    
				    HttpEntity entity = response.getEntity();
				    
				    String pagina = EntityUtils.toString(entity);
				    Document doc = Jsoup.parse(pagina);
				    Elements els = doc.getElementsByClass("cotacao-table").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
				    				    
				    boolean primeiro = true;
				    for (Element el: els) {
				    	Elements children = el.children();
				    	
			    		int idx = 0;
			    		
			    		// pula essa linha:
				    	// <td class="cotacao-table__data-type" rowspan="31"><p>MERCADO FÍSICO<span>@ (exceto se indicado)</span></p></td>
				    	if (primeiro) {
				    		idx++;
				    		primeiro = false;
				    	}
				    	
				    	String uf = children.get(idx++).text();
				    	String praca = children.get(idx++).text();
				    	String valorAVista = children.get(idx++).text();
				    	String valorAPrazo = children.get(idx++).text();
				    	
				    	Cotacao c = CigaDAO.getInstance().buscarCotacao(uf, praca, tipoCotacao);
				    	
				    	if (c == null) {				    	
				    		c = new Cotacao();
				    		c.setTipoCotacao(tipoCotacao);
					    	c.setUf(uf);
					    	c.setPraca(praca);
					    	c.setValorAVista(new BigDecimal(valorAVista.replace(',', '.')));
					    	c.setValorAPrazo(new BigDecimal(valorAPrazo.replace(',', '.')));
					    	
					    	CigaDAO.getInstance().adicionar(c);
					    	
				    	} else {
					    	c.setValorAVista(new BigDecimal(valorAVista.replace(',', '.')));
					    	c.setValorAPrazo(new BigDecimal(valorAPrazo.replace(',', '.')));
					    	
					    	CigaDAO.getInstance().atualizaCotacao(c);
				    	}
				    	
				    }
				    
			    }
			} catch (LoggerException e) {
				Logger.getLogger(getClass()).error("Erro ao atualizar notícias", e);
			} finally {
			    response.close();
			}
		} catch (IOException e) {
			Logger.getLogger(getClass()).error("Erro de IO ao buscar cotações", e);
		}
	}
	
	/**
	 * 
	 
	public void atualizarCotacaoBMF(TipoCotacao tipoCotacao) {
		int timeout = 15; // 15 segundos
		RequestConfig config = RequestConfig.custom()
				  .setConnectTimeout(timeout * 1000)
				  .setConnectionRequestTimeout(timeout * 1000)
				  .setSocketTimeout(timeout * 1000).build();
		CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		
		String url = "http://www2.bmf.com.br/pages/portal/bmfbovespa/boletim1/indicadoresAgropecuarios1.asp";
				
		HttpGet httpGet = new HttpGet(url);
		try {
			
			CloseableHttpResponse response = httpclient.execute(httpGet);
			try {
				
			    if (response.getStatusLine().getStatusCode() == 200) {
			    
				    HttpEntity entity = response.getEntity();
				    
				    String pagina = EntityUtils.toString(entity);
				    Document doc = Jsoup.parse(pagina);
				    
				    Elements els = null;
				    String uf = "MS";
			    	String praca = "Campo Grande";
				    
				    switch (tipoCotacao) {
					    case BEZERRO_MACHO:
					    	els = doc.getElementsByClass("tabConteudo").get(5).getElementsByTag("tbody").get(0).getElementsByTag("tr");
					    	uf = "SP";
					    	praca = "Campinas";
					    	break;
					    //case MILHO_SACA:
					   // 	els = doc.getElementsByClass("tabConteudo").get(1).getElementsByTag("tbody").get(0).getElementsByTag("tr");
					   //	break;
					    default:
					    	throw new UnsupportedOperationException("Tipo de cotação não existe no site ");
				    }
				    		    
				    Elements children = els.get(els.size() - 2).children();
			    				    	
			    	String valorAVista = children.get(1).text();
			    	String valorAPrazo = children.get(1).text();
			    	
			    	Cotacao c = CigaDAO.getInstance().buscarCotacao(uf, praca, tipoCotacao);
			    	
			    	if (c == null) {				    	
			    		c = new Cotacao();
			    		c.setTipoCotacao(tipoCotacao);
				    	c.setUf(uf);
				    	c.setPraca(praca);
				    	c.setValorAVista(new BigDecimal(valorAVista.replace(".", "").replace(',', '.')));
				    	c.setValorAPrazo(new BigDecimal(valorAPrazo.replace(".", "").replace(',', '.')));
				    	
				    	CigaDAO.getInstance().adicionar(c);
				    	
			    	} else {
				    	c.setValorAVista(new BigDecimal(valorAVista.replace(".", "").replace(',', '.')));
				    	c.setValorAPrazo(new BigDecimal(valorAPrazo.replace(".", "").replace(',', '.')));
				    	
				    	CigaDAO.getInstance().atualizaCotacao(c);
			    	}
			    	
			    }
				 
			} catch (LoggerException e) {
				Logger.getLogger(getClass()).error("Erro ao atualizar notícias", e);
			} finally {
			    response.close();
			}
		} catch (IOException e) {
			Logger.getLogger(getClass()).error("Erro de IO ao buscar cotações", e);
		}
	}*/
	
	
	/**
	 * 
	 */
	public void atualizarCotacaoScotEmb(TipoCotacao tipoCotacao) {
		int timeout = 15; // 15 segundos
		RequestConfig config = RequestConfig.custom()
				  .setConnectTimeout(timeout * 1000)
				  .setConnectionRequestTimeout(timeout * 1000)
				  .setSocketTimeout(timeout * 1000).build();
		CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		
		String url = "https://www.scotconsultoria.com.br/embed/?q=";
		
		switch (tipoCotacao) {
		    case MILHO_SACA:
		    	url += "milho";
		    	break;
		    default:
		    	throw new UnsupportedOperationException("Tipo de cotação não existe no site ");
		}
		
		HttpGet httpGet = new HttpGet(url);
		try {
			
			CloseableHttpResponse response = httpclient.execute(httpGet);
			try {
				
			    if (response.getStatusLine().getStatusCode() == 200) {
			    
				    HttpEntity entity = response.getEntity();
				    
				    String pagina = EntityUtils.toString(entity);
				    Document doc = Jsoup.parse(pagina);
				    
				    Elements els = doc.getElementsByClass("fonte-subtitulo-cinza");
				    
				    String ufAnt = "";
				    
				    for (Element el: els) {
				    	Elements children = el.getElementsByTag("tbody").get(0).getElementsByTag("tr").get(0).getElementsByTag("td");
				    	
				    	String uf = children.get(0).text();
				    	if (!uf.isEmpty())
				    		ufAnt = uf;
				    	else
				    		uf = ufAnt;
				    		
				    	String praca = children.get(1).text();
				    	String valorAVista = children.get(2).text();
				    	String valorAPrazo = children.get(2).text();
					    
				    	Cotacao c = CigaDAO.getInstance().buscarCotacao(uf, praca, tipoCotacao);
				    	
				    	if (c == null) {				    	
				    		c = new Cotacao();
				    		c.setTipoCotacao(tipoCotacao);
					    	c.setUf(uf);
					    	c.setPraca(praca);
					    	c.setValorAVista(new BigDecimal(valorAVista.replace(".", "").replace(',', '.')));
					    	c.setValorAPrazo(new BigDecimal(valorAPrazo.replace(".", "").replace(',', '.')));
					    	
					    	CigaDAO.getInstance().adicionar(c);
					    	
				    	} else {
					    	c.setValorAVista(new BigDecimal(valorAVista.replace(".", "").replace(',', '.')));
					    	c.setValorAPrazo(new BigDecimal(valorAPrazo.replace(".", "").replace(',', '.')));
					    	
					    	CigaDAO.getInstance().atualizaCotacao(c);
				    	}
				    	
				    }
			    }
				 
			} catch (LoggerException e) {
				Logger.getLogger(getClass()).error("Erro ao atualizar notícias", e);
			} finally {
			    response.close();
			}
		} catch (IOException e) {
			Logger.getLogger(getClass()).error("Erro de IO ao buscar cotações", e);
		}
	}
	
	/**
	 * 
	 */
	public void atualizarCotacaoScot(TipoCotacao tipoCotacao) {
		int timeout = 15; // 15 segundos
		RequestConfig config = RequestConfig.custom()
				  .setConnectTimeout(timeout * 1000)
				  .setConnectionRequestTimeout(timeout * 1000)
				  .setSocketTimeout(timeout * 1000).build();
		CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		
		String url = "https://www.scotconsultoria.com.br/cotacoes/reposicao";
		
		HttpGet httpGet = new HttpGet(url);
		try {
			
			CloseableHttpResponse response = httpclient.execute(httpGet);
			try {
				
			    if (response.getStatusLine().getStatusCode() == 200) {
			    
				    HttpEntity entity = response.getEntity();
				    
				    String pagina = EntityUtils.toString(entity);
				    Document doc = Jsoup.parse(pagina);
				    
				    int idxTab = 0;
				    
				    switch (tipoCotacao) {
					    case BEZERRO_MACHO:
					    	idxTab = 1; 
					    	break;
					    case NOVILHA:
					    	idxTab = 3; 
					    	break;
					    default:
					    	throw new UnsupportedOperationException("Tipo de cotação não existe no site ");
					}
				    
				    Elements els = doc.getElementsByClass("conteudo_centro").get(0).getElementsByTag("table").get(idxTab).getElementsByClass("conteudo");
				    				    				    
				    for (Element el: els) {
				    	if ("conteudo final".equals(el.className()))
				    		continue;
				    	
				    	Elements children = el.children();
				    	
				    	String uf = children.get(0).text().substring(0, 2);
				    	String praca = uf;
				    	String valorAVista = children.get(7).text() + "0";
				    	String valorAPrazo = children.get(7).text() + "0";
					    
				    	Cotacao c = CigaDAO.getInstance().buscarCotacao(uf, praca, tipoCotacao);
				    	
				    	if (c == null) {				    	
				    		c = new Cotacao();
				    		c.setTipoCotacao(tipoCotacao);
					    	c.setUf(uf);
					    	c.setPraca(praca);
					    	c.setValorAVista(new BigDecimal(valorAVista));
					    	c.setValorAPrazo(new BigDecimal(valorAPrazo));
					    	
					    	CigaDAO.getInstance().adicionar(c);
					    	
				    	} else {
					    	c.setValorAVista(new BigDecimal(valorAVista));
					    	c.setValorAPrazo(new BigDecimal(valorAPrazo));
					    	
					    	CigaDAO.getInstance().atualizaCotacao(c);
				    	}
				    	
				    }
			    }
				 
			} catch (LoggerException e) {
				Logger.getLogger(getClass()).error("Erro ao atualizar notícias", e);
			} finally {
			    response.close();
			}
		} catch (IOException e) {
			Logger.getLogger(getClass()).error("Erro de IO ao buscar cotações", e);
		}
	}
	
	/**
	 * 
	 * @param email
	 * @param Assunto
	 * @param mensagem
	 * @throws EmailException 
	 */
	@SuppressWarnings("deprecation")
	public void enviarEmail(final String nome, final String enderecoEmail, 
				final String assunto, final String mensagem) {
		
		new Thread(new Runnable() {			
			
			@Override
			public void run() {
				try {
					
					SimpleEmail email = new SimpleEmail();  
			        email.setHostName("email-ssl.com.br"); // o servidor SMTP para envio do e-mail  
			        email.addTo("contato@cigaconsultoria.com.br", "Site Ciga Consultoria"); //destinatário  
			        email.setFrom("webmaster@cigaconsultoria.com.br", "Ciga Consultoria"); 
			        email.setSubject(assunto); // assunto do e-mail  
			        email.setMsg(mensagem); //conteudo do e-mail  
			        email.setAuthentication("webmaster@cigaconsultoria.com.br", "mariana123!@");  
			        email.setSmtpPort(465);  
			        email.setSSL(true);  
			        email.setTLS(true);  
			        email.send();
		        
				} catch (EmailException e) {
					Logger.getLogger(CigaFacade.class).error("Erro ao enviar email", e);
				}
		        
			}
		}).start();
	}
	
	
}
