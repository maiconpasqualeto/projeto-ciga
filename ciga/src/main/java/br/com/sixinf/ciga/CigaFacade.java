/**
 * 
 */
package br.com.sixinf.ciga;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

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
		
		Noticia n = new Noticia();
		n.setTitulo("Recorde nas exportações brasileiras de soja em junho");
		n.setDescricao("As exportações brasileiras de soja totalizaram 9,81 milhões de toneladas em junho, segundo o Ministério do Desenvolvimento, Indústria e Comércio Exterior (MDIC). Foi o maior volume mensal embarcado na história.\r\nAs exportações aumentaram 5,0% em relação a maio deste ano, quando foram exportadas 9,34 milhões de toneladas, recorde até então.");
		n.setDataHora(new Date());
		n.setFonte("Sistema Brasileiro do Agronegócio");
		n.setLink("www.sba1.com/noticias/economia/56250/recorde-nas-exportacoes-brasileiras-de-soja-em-junho");
		noticias.add(n);
		
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
