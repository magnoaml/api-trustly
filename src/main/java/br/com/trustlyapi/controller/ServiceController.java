package br.com.trustlyapi.controller;


import java.util.ArrayList;
import java.util.List;
 
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import br.com.trustlyapi.dao.GitRepositoryDao;
import br.com.trustlyapi.dao.entity.GitRepositoryEntity;
import br.com.trustlyapi.model.GitRepository;
import br.com.trustlyapi.service.GitRepositoryPrepareDataService;

 
 

@Path("/gitrepo")
public class ServiceController extends Thread{
  
	private  GitRepositoryDao repository = new GitRepositoryDao();
	List<GitRepository> gitRepositoryReturn =  new ArrayList<GitRepository>();

	/**
	 * @Consumes - format data post
	 * @Produces - format data return
	 * 
	 * This method register save/update new repository
	 * */
	@POST	
	@Consumes("application/json")
	@Produces("application/json")

	public boolean saveEdit(List<GitRepository> listGitRepository){
 
		GitRepositoryEntity entity;
		repository = new GitRepositoryDao();
		try { 
        	for (GitRepository line : listGitRepository) {
        		entity = new GitRepositoryEntity();
        		entity.setExtension(line.getExtension());
        		entity.setTotalLines(line.getTotalLines());
        		entity.setTotlBytes(line.getTotalBytes());
        		entity.setUrlRepository(line.getUrlRepository());
				
				repository.saveUpdate(entity);
			}
		
 
			return true;
 
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
 
	}

	
    public boolean deleteRepo(String urlGit, String repo) {
    
    	try {
    		repository = new GitRepositoryDao();
			repository.deleteByUrl(urlGit, repo); 
			return true;
 
		} catch (Exception e) {
 
			System.out.println(e.getMessage());
			return false;
		}
 
	}
 
	
	/**
	 * This method list all repositories of dababases
	 * */
	@GET
	@Produces("application/json")
	@Path("/listAll")
	public List<GitRepository> listAllRepositories(){
 
		List<GitRepository> repo =  new ArrayList<GitRepository>();
		repository = new GitRepositoryDao();
		try {
			List<GitRepositoryEntity> listRepositories = repository.allRepositories();
	 
			for (GitRepositoryEntity entity : listRepositories) {
				
				repo.add(new GitRepository(entity.getExtension(), entity.getTotalLines(), entity.getTotalBytes(), entity.getUrlRepository()));
			}
			return repo;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return repo;
		}
	}	
	
	/**
	 * This method lists a searched repository
	 * */
	@GET
	@Produces("application/json")
	@Path("/listRepo/{usergit}/{repositoryGit}")
	public List<GitRepository> listRepository(@PathParam("usergit") String userGit, @PathParam("repositoryGit") String repositoryGit){
 
		List<GitRepository> repo =  new ArrayList<GitRepository>();
		try {
			repository = new GitRepositoryDao();
			List<GitRepositoryEntity> listRepositories = repository.getRepository(userGit, repositoryGit);
	 
			for (GitRepositoryEntity entity : listRepositories) {
				
				repo.add(new GitRepository(entity.getExtension(), entity.getTotalLines(), entity.getTotalBytes(), entity.getUrlRepository()));
			}
			
			return repo;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return repo;
		}
		
	}


 
	/**
	 * This method lists a repository and does not register on database
	 * */	
	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/{usergit}/{repository}")
	public List<GitRepository> getGitRepository(@PathParam("usergit") String urlGit, @PathParam("repository") String repo){
		
    	Thread thread = new Thread(){
    		public void run(){
    			System.out.println("Thread Running");		
				try {
					String urlRepo = urlGit.concat("/").concat(repo);	
					List<GitRepository> gitRepository =  new ArrayList<GitRepository>();
					GitRepositoryPrepareDataService gitPrepareRepository = new GitRepositoryPrepareDataService();
					gitRepository = gitPrepareRepository.prepareRepository(urlRepo);
					gitRepositoryReturn = new ArrayList<GitRepository>();
					gitRepositoryReturn = gitRepository;
				}catch (Exception e) {
					System.out.println(e.getMessage());
				}
    		}
    	};
			  
		thread.start();
		while (thread.isAlive()) {
			System.out.println(thread.getName());
		}

		
		return gitRepositoryReturn;
	}
	
	
	/**
	 * This method registers or updates the data of a repository in the database.
	 * If the repository already exists, the data in this repository is deleted and registered again.
	 * */	
	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/saveUpdate/{usergit}/{repository}")
	public List<GitRepository> saveGitRepository(@PathParam("usergit") String urlGit, @PathParam("repository") String repo){
		
		List<GitRepository> gitRepository =  new ArrayList<GitRepository>();
		try {
	
			gitRepository = getGitRepository(urlGit, repo);
	 
			boolean delete = deleteRepo(urlGit, repo);
			boolean insert = saveEdit(gitRepository);
			
			if (delete && insert)
				return gitRepository;
			else 
				return new ArrayList<GitRepository>();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return gitRepository;
		}
	}
	

 
	 
}
