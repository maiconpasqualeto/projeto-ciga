/**
 * 
 */
package br.com.sixinf.ciga.listener;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import br.com.sixinf.ciga.TimerAtualizaDados;
import br.com.sixinf.ferramentas.persistencia.AdministradorPersistencia;
import br.com.sixinf.ferramentas.persistencia.PersistenciaException;

/**
 * @author maicon
 *
 */
public class CigaListener implements ServletContextListener {
	
	private Timer timer = new Timer();

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		AdministradorPersistencia.close();
		
		timer.cancel();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		System.getProperties().put("org.apache.el.parser.COERCE_TO_ZERO", "false");
		
		try {
			AdministradorPersistencia.createEntityManagerFactory("cigaunit");
		} catch (PersistenciaException e) {
			Logger.getLogger(CigaListener.class).error(e);
		}
		
		TimerTask tt = new TimerAtualizaDados();
		timer.schedule(tt, 3000, 86400000); // 24 horas
		
	}

}
