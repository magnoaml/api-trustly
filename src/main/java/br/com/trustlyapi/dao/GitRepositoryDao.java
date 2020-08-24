package br.com.trustlyapi.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.PathParam;
import org.eclipse.persistence.internal.descriptors.PersistenceObjectAttributeAccessor;
import br.com.trustlyapi.dao.entity.GitRepositoryEntity;
import br.com.trustlyapi.model.GitRepository;
 
 
 
public class GitRepositoryDao {
 
	private final EntityManagerFactory entityManagerFactory;
 
	private final EntityManager entityManager;
 
	public GitRepositoryDao(){
 
		this.entityManagerFactory = Persistence.createEntityManagerFactory("persistence");
 
		this.entityManager = this.entityManagerFactory.createEntityManager();
	}
 
	/**
	 * Save a new register repository on database
	 * */
	public void saveUpdate(GitRepositoryEntity entity){
		try {
			this.entityManager.getTransaction().begin();
			this.entityManager.persist(entity);
			this.entityManager.getTransaction().commit();
			
	    } catch (Exception ex) {
	    	this.entityManager.getTransaction().rollback();	        
	    } 
	}
	
	/**
	 * Delete all registers of a repository on databases
	 * */

	public void deleteByUrl(String urlGit, String repo) {
	    try {
	    	String urlRepo = urlGit.concat("/").concat(repo);
	    	this.entityManager.getTransaction().begin();
	    	Query q = this.entityManager.createQuery("delete GitRepositoryEntity rep "
	    			+ "where rep.urlRepository like'%".concat(urlRepo).concat("%'"));		
	        q.executeUpdate();	        
			this.entityManager.getTransaction().commit();
	    } catch (Exception ex) {
	    	this.entityManager.getTransaction().rollback();
	        
	    } 
    }
    
	
	/**
	 * Lists all repositories of database
	 * */
	@SuppressWarnings("unchecked")
	public List<GitRepositoryEntity> allRepositories(){
 
		return this.entityManager.createQuery("SELECT rep FROM GitRepositoryEntity rep "
				+ "ORDER BY rep.urlRepository, rep.extension").getResultList();
	}
 
	/**
	 * List all data of one repository of databases
	 * */
	public List<GitRepositoryEntity> getRepository(String user, String repo){
 
		String repository = user.concat("/").concat(repo);
		return this.entityManager.createQuery("SELECT rep FROM GitRepositoryEntity rep "
				+ "WHERE rep.urlRepository like '%".concat(repository).
				concat("%' ORDER BY rep.urlRepository, rep.extension")).getResultList();
	}
	
	
 
}