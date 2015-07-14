/**
 * 
 */
package br.com.sixinf.ciga;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
	
	public List<Noticia> buscarNoticias() {
		
		List<Noticia> noticias = new ArrayList<Noticia>();
		
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
				    	
				    	noticias.add(n);
				    	
				    }
				    
			    }
			} catch (ParseException e) {
				Logger.getLogger(getClass()).error("Erro de parse da data ao buscar notícias", e);
			} finally {
			    response.close();
			}
		} catch (IOException e) {
			Logger.getLogger(getClass()).error("Erro de IO ao buscar notícias", e);
		}
		
		/*Noticia n = new Noticia();
		n.setTitulo("Recorde nas exportações brasileiras de soja em junho");
		n.setDescricao("As exportações brasileiras de soja totalizaram 9,81 milhões de toneladas em junho, segundo o Ministério do Desenvolvimento, Indústria e Comércio Exterior (MDIC). Foi o maior volume mensal embarcado na história.\r\nAs exportações aumentaram 5,0% em relação a maio deste ano, quando foram exportadas 9,34 milhões de toneladas, recorde até então.");
		n.setDataHora(new Date());
		n.setFonte("Sistema Brasileiro do Agronegócio");
		n.setLink("www.sba1.com/noticias/economia/56250/recorde-nas-exportacoes-brasileiras-de-soja-em-junho");
		noticias.add(n);*/
		
		return noticias;
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
