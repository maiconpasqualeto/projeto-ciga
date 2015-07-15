/**
 * 
 */
package br.com.sixinf.ciga.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.sixinf.ciga.entidades.Cotacao;
import br.com.sixinf.ciga.entidades.Noticia;
import br.com.sixinf.ciga.entidades.TipoCotacao;
import br.com.sixinf.ferramentas.dao.BridgeBaseDAO;
import br.com.sixinf.ferramentas.dao.HibernateBaseDAOImp;
import br.com.sixinf.ferramentas.log.LoggerException;
import br.com.sixinf.ferramentas.persistencia.AdministradorPersistencia;

/**
 * @author maicon
 *
 */
public class CigaDAO extends BridgeBaseDAO {
	
	private static CigaDAO dao;
	
	public static CigaDAO getInstance(){
		if (dao == null)
			dao = new CigaDAO();
		return dao;
	}

	public CigaDAO() {
		super(new HibernateBaseDAOImp());
	}

	/**
	 * 
	 * @return
	 */
	public List<Noticia> buscarUltimasNoticias() {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		List<Noticia> lista = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select n from Noticia n ");
			hql.append("order by n.dataHora desc");
			TypedQuery<Noticia> q = em.createQuery(hql.toString(), Noticia.class);
			q.setMaxResults(10);
			lista = q.getResultList();
			
		} catch (Exception e) {
			Logger.getLogger(getClass()).error("Erro ao buscar enderecos do cliente", e);
		} finally {
            em.close();
        }
		return lista;
	}
	
	/**
	 * 
	 * @return
	 * @throws LoggerException 
	 */
	public boolean jaExiteNoticia(String titulo, Date dataHora) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		boolean retorno = false; 
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select count(n) from Noticia n ");
			hql.append("where n.titulo = :titulo ");
			hql.append("and n.dataHora = :dataHora ");
						
			Query q = em.createQuery(hql.toString());
			
			q.setParameter("titulo", titulo);
			q.setParameter("dataHora", dataHora);
						
			Long count = (Long) q.getSingleResult();
			if (count > 0)
				retorno = true;
			
		} catch (NoResultException e) {
			
		} catch (Exception e) {
			throw new LoggerException("Erro ao verificar se já existe notícia", e, Logger.getLogger(getClass()));
		} finally {
            em.close();
        }
		return retorno;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Cotacao buscarCotacao(String uf, String praca, TipoCotacao tipoCotacao) {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		Cotacao o = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select c from Cotacao c ");
			hql.append("where c.uf = :uf ");
			hql.append("and c.praca = :praca ");
			hql.append("and c.tipoCotacao = :tipoCotacao ");
			TypedQuery<Cotacao> q = em.createQuery(hql.toString(), Cotacao.class);
			q.setParameter("uf", uf);
			q.setParameter("praca", praca);
			q.setParameter("tipoCotacao", tipoCotacao);
			
			o = q.getSingleResult();
			
		} catch (NoResultException e) {
			
		} catch (Exception e) {
			Logger.getLogger(getClass()).error("Erro ao buscar cotacao", e);
		} finally {
            em.close();
        }
		return o;
	}
	
	/**
	 * 
	 * @param cotacao
	 * @throws LoggerException 
	 */
	public void atualizaCotacao(Cotacao cotacao) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		EntityTransaction t = em.getTransaction();
		
		try {
			t.begin();
			
			StringBuilder hql = new StringBuilder();
			hql.append("update Cotacao c ");
			hql.append("set c.valorAVista = :valorAVista, ");
			hql.append("c.valorAPrazo = :valorAPrazo ");
			hql.append("where c.uf = :uf ");
			hql.append("and c.praca = :praca ");
			hql.append("and c.tipoCotacao = :tipoCotacao ");
			
			Query q = em.createQuery(hql.toString());
			q.setParameter("uf", cotacao.getUf());
			q.setParameter("praca", cotacao.getPraca());
			q.setParameter("tipoCotacao", cotacao.getTipoCotacao());
			q.setParameter("valorAVista", cotacao.getValorAVista());
			q.setParameter("valorAPrazo", cotacao.getValorAPrazo());
			
			q.executeUpdate();
			
			t.commit();
		
		} catch (Exception e) {
			t.rollback();
			throw new LoggerException("Erro ao gravar produto", e, Logger.getLogger(getClass()));
		} finally {
            em.close();
        }
		
	}

}
