/**
 * 
 */
package br.com.sixinf.ciga;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
				    SimpleDateFormat sdf = new SimpleDateFormat("dd' de 'MMM' de 'yyyy' - 'HH'h'");
				    
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
				    	n.setDescricao(excerpt);
				    	n.setFonte("SBA1");
				    	n.setLink(link);
				    	
				    	boolean jaExiteNoticia = CigaDAO.getInstance().jaExiteNoticia(n.getTitulo(), n.getDataHora());
				    	if (!jaExiteNoticia)
				    		CigaDAO.getInstance().adicionar(n);
				    	
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
	 */
	public void atualizarCotacoes() {
		CigaFacade.getInstance().atualizarCotacao(TipoCotacao.BOI_GORDO);
		CigaFacade.getInstance().atualizarCotacao(TipoCotacao.VACA_GORDA);
		CigaFacade.getInstance().atualizarCotacao(TipoCotacao.SOJA_SACA);
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
			        email.addTo("webmaster@cigaconsultoria.com.br", "Site Ciga Consultoria"); //destinatário  
			        email.setFrom("webmaster@cigaconsultoria.com.br", "Ciga Consultoria"); // destinatário
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
