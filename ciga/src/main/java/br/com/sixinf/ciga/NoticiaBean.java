/**
 * 
 */
package br.com.sixinf.ciga;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import br.com.sixinf.ciga.dao.CigaDAO;
import br.com.sixinf.ciga.entidades.Noticia;
import br.com.sixinf.ferramentas.dao.DAOException;

/**
 * @author maicon
 *
 */
@ManagedBean(name="noticiasBean")
@ViewScoped
public class NoticiaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Noticia noticia;
	private List<Noticia> ultimasNoticias = new ArrayList<Noticia>();
	
	@PostConstruct
	public void init() {
		
		try {
		
			FacesContext facesContext = FacesContext.getCurrentInstance();
			String strIdNoticia = facesContext.getExternalContext().getRequestParameterMap().get("id");
			
			if (strIdNoticia == null) {
	        	FacesContext.getCurrentInstance().getExternalContext().redirect("principal.xhtml");
	        	facesContext.responseComplete();
	        	return;
	        }
			
			noticia = CigaDAO.getInstance().buscar(Long.valueOf(strIdNoticia), Noticia.class);
			
			ultimasNoticias = CigaDAO.getInstance().buscarUltimasNoticias();
			
		} catch (IOException | NumberFormatException | DAOException e) {
			Logger.getLogger(getClass()).error("Erro ao carregar noticia", e);
		}
		
	}

	public Noticia getNoticia() {
		return noticia;
	}

	public void setNoticia(Noticia noticia) {
		this.noticia = noticia;
	}

	public List<Noticia> getUltimasNoticias() {
		return ultimasNoticias;
	}

	public void setUltimasNoticias(List<Noticia> ultimasNoticias) {
		this.ultimasNoticias = ultimasNoticias;
	}
	
}
