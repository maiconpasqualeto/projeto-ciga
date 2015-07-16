/**
 * 
 */
package br.com.sixinf.ciga;

import java.util.TimerTask;

import org.apache.log4j.Logger;

/**
 * @author maicon
 *
 */
public class TimerAtualizaDados extends TimerTask {
	
	
	@Override
	public void run() {
		Logger.getLogger(getClass()).debug("Inicia atualização de notícias.");
		
		CigaFacade.getInstance().atualizarNoticias();
		
		CigaFacade.getInstance().atualizarCotacoes();
		
	}

}
