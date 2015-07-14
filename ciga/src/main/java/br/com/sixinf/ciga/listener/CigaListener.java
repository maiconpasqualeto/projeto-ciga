/**
 * 
 */
package br.com.sixinf.ciga.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import br.com.sixinf.ferramentas.persistencia.AdministradorPersistencia;
import br.com.sixinf.ferramentas.persistencia.PersistenciaException;

/**
 * @author maicon
 *
 */
public class CigaListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		AdministradorPersistencia.close();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		System.getProperties().put("org.apache.el.parser.COERCE_TO_ZERO", "false");
		
		try {
			AdministradorPersistencia.createEntityManagerFactory("cigaunit");
		} catch (PersistenciaException e) {
			Logger.getLogger(CigaListener.class).error(e);
		}
		
	}

}
