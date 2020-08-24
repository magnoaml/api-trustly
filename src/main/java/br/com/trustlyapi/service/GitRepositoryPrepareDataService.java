package br.com.trustlyapi.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import br.com.trustlyapi.model.GitRepository;


public class GitRepositoryPrepareDataService{
	
	private static final String ROLE = "role=\"rowheader\"";
	private static final String CLASS = "flex-auto min-width-0 col-md-2 mr-3";
	private static final String CLASS_FILE_NAME = "\"final-path\">";
	private static final String LINK = "href";
	private static final String HTTPS = "https://";
	private static final String CLOSE_TAG = "\">";
	private static final String LINES = " lines (";
	private static final String BYTES = " Bytes";
	private static final String KB = " KB";
	private static final String MB = " MB";
	private static final String LOG_LINES = "Lines: ";	
	private static final String LOG_SIZE = "Size: ";	
	private static final String LOG_FILE_NAME = "File Name: ";
	private static final String LOG_FILE_EXT = "File Extension: ";
	private static final String TOTAL_BYTES_FILES = "TOTAL NUMBER OF BYTES OF ALL THE FILES GROUPED BY EXTENSIONS";
	private static final double KBYTE = 1024;
	private static final double MBYTE = 1048576;
	private static final String URL_GIT = "https://github.com/";
	private static final String DOT_GIT = ".git";
	private static final String EXTENSION = "Extension:";
	private static final String SIZE = "; Size";
	private static final String FILE = "; File";
		
	private List<String> listFiles = new ArrayList<>();

	
    public List<GitRepository> prepareRepository(String urlGitRepository)  {
    
    	String urlGit ="";
		try {
		
			System.out.println("Read data repository:");
			urlGit =URL_GIT.concat(urlGitRepository).concat(DOT_GIT);
            URL urlRepository = new URL(URL_GIT.concat(urlGitRepository).concat(DOT_GIT));
            String urlString = urlRepository.getAuthority(); 		
		            
            BufferedReader file = new BufferedReader(new InputStreamReader(urlRepository.openStream()));
            String line;
          
            boolean hasFile = false; 

            while ((line = file.readLine()) != null) {
            	
            	if (hasFile && line.contains(LINK)) {
            		System.out.println(HTTPS.concat(urlString.concat(getLinkRepository(line))));
            		getDirectoriesFiles(HTTPS.concat(urlString.concat(getLinkRepository(line))), URL_GIT.concat(urlGitRepository));            		
            	}
            	
            	hasFile = line.contains(ROLE) && line.contains(CLASS);
            }
            file.close();
		    		
        } catch (MalformedURLException excecao) {
            excecao.printStackTrace();
        } catch (IOException excecao) {
            excecao.printStackTrace();
        }				

        return printListTotal(urlGit);
		
    	
    }

    
    private static String getLinkRepository(String link) {
    	
    	int posIni = link.indexOf(LINK);
    	int posEnd = link.length();
    	link = link.substring(posIni, posEnd);
    	String newLink = link.substring(6, link.indexOf(CLOSE_TAG));
    	
    	return newLink;
    }
   
    private void getDirectoriesFiles (String linkRepositoryGit, String repositoryDefault) {
    	try { 
	        
	        URL url = new URL(linkRepositoryGit);
	        BufferedReader diretory = new BufferedReader(new InputStreamReader(url.openStream()));
	        Integer totalNumberLines = 0;
	        String line="", repositorioRaiz="", unity="", linesTotal, lineTypeSizeFile="", fileName="", fileExt = "";
            boolean hasDirectory=true, hasFile = true, addItensList=false; 
            int posStart=0, posEnd = 0;
 
	        while ((line = diretory.readLine()) != null) {
	        	
	        	hasFile = line.contains(LINES) || line.contains(BYTES) ||  line.contains(KB) ||  line.contains(MB);
	        	
	        	if (hasDirectory && line.contains(LINK)) {
            		repositorioRaiz = getLinkRepository(line);
            		
            		if (!repositorioRaiz.equals(repositoryDefault)) {
            			System.out.println(HTTPS.concat(url.getAuthority()).concat(repositorioRaiz));	  
            			getDirectoriesFiles(HTTPS.concat(url.getAuthority()).concat(getLinkRepository(line)), repositoryDefault);  
            		}
            	}else if (!hasDirectory && line.contains(CLASS_FILE_NAME)) {
            		
            		posStart = line.indexOf(CLASS_FILE_NAME) + 13;
            		posEnd = line.indexOf("</strong>");
            		fileName = line.substring(posStart, posEnd);
            		fileExt = fileName.substring(fileName.lastIndexOf(".") +1 , fileName.length()) ;
            		
            	}else if (hasFile) {
            		
            		if (line.contains(LINES)) {
	            		linesTotal = line.substring(0, line.indexOf(LINES)).trim();
	            		System.out.println(LOG_LINES.concat(linesTotal).concat(" "));
	            		totalNumberLines += Integer.valueOf(linesTotal);
	            		lineTypeSizeFile += LOG_LINES.concat(linesTotal).concat("; ");
            			
	            	}else if (line.contains(BYTES) ||  line.contains(KB) ||  line.contains(MB)) {
	                	
	            		if (line.contains(BYTES)) {
	            			posEnd = line.indexOf(BYTES);
	            			unity = BYTES;
	            		}else if (line.contains(KB)) {
	            			posEnd = line.indexOf(KB);
	            			unity = KB;
	            		}else if (line.contains(MB)) {
	            			posEnd = line.indexOf(MB);
	            			unity = MB;
	            		}
	            		
	            		lineTypeSizeFile += LOG_FILE_NAME.concat(fileName).trim().
	            				concat("; ").concat(LOG_FILE_EXT).concat(fileExt).concat("; ");
	            		System.out.println(LOG_SIZE.concat(line.substring(0, posEnd).trim().concat(unity)));
	            		
	            		lineTypeSizeFile += LOG_SIZE.concat(line.substring(0, posEnd).trim().concat(unity)).concat(" ");
	            		System.out.println(LOG_FILE_NAME.concat(fileName.trim()));
	            			            		
	            		addItensList = true;
	            	}
	            	
	            	if (addItensList) {
	            		listFiles.add(lineTypeSizeFile);
	            		addItensList = false;
	            		lineTypeSizeFile = "";
	            		fileName = "";
	            		fileExt = "";
	            	}
            	}


	        	hasFile = false;            	
	            hasDirectory = line.contains(ROLE) && line.contains(CLASS);
            	
	        }

			
        } catch (MalformedURLException excecao) {
            excecao.printStackTrace();
        } catch (IOException excecao) {
            excecao.printStackTrace();
        }
    	
    }
    
    private List<GitRepository> printListTotal(String urlGit) {
    	System.out.println(TOTAL_BYTES_FILES); 
    	String ext="", lines="",  bytes="", size ="";
		int posStart = 0, posEnd = 0;
		Double sizeFile;
        Integer totalLinesLista = 0;
		List<String> groupFilesExt = new ArrayList<>();	
		boolean hasExt = false;
	
	
		try {
	    	for(String list : listFiles) {
	    		  posStart = list.indexOf(EXTENSION) + 10;
	    		  posEnd = list.indexOf(SIZE);    		  
	    		  if (posEnd >= posStart)
	    			  ext = list.substring(posStart, posEnd).trim();
	    		  
	    		  if (list.contains(LOG_LINES)){
	    			  posStart = list.indexOf(LOG_LINES) + 7;
		    		  posEnd = list.indexOf(FILE);
		    		  if (posEnd >= posStart)
		    			  lines = list.substring(posStart, posEnd);
	    		  } else
	    			  lines = "0";
	
	    		  totalLinesLista += Integer.valueOf(lines);
	    		  posStart = list.indexOf(LOG_SIZE) + 6;
	    		  posEnd = list.length();
	    		  
	    		  
	    		  if (posEnd >= posStart) {
	    			  size = list.substring(posStart, posEnd);
	    			  posEnd = size.indexOf(" ");
	    			  size = size.substring(0, posEnd);
	    		  }
	    		  
	    		  posEnd = list.length();
	    		  if (posEnd >= posStart) {
	    			  bytes = list.substring(posStart, posEnd);
	    			  posStart = bytes.indexOf(" ")+1;
	    			  bytes = bytes.substring(posStart, bytes.length());
	    		  }
	    		  
	    		  
				  if (bytes.trim().equals(MB.trim())) {
					  sizeFile = convertMbToBytes(Double.valueOf(size));
					  size = String.valueOf(sizeFile);
					  
				  }else if (bytes.trim().equals(KB.trim())) {
					  sizeFile = convertKbToBytes(Double.valueOf(size));
					  size = String.valueOf(sizeFile);
				  }
				  
				  			  
				  for(String listGroup : groupFilesExt) {
					  hasExt = listGroup.equals(ext);				  
					  
					  if (hasExt)
						  break;
					  else
						  hasExt = false;
	
				  }
				  
				  if (!hasExt)
					  groupFilesExt.add(ext);  
				  
	    		  System.out.println(ext.concat(", ").concat(lines).
	    				  concat(", ").concat(size).concat(",").concat(BYTES).trim());
	    	}
        } catch (Exception excecao) {
            excecao.getMessage();
        }
		
    	System.out.println("TOTAL LINES: ".concat(String.valueOf(totalLinesLista)));
    	return groupFiles(groupFilesExt, urlGit);

    }
    
    private static double convertMbToBytes(double mb) {
    	
    	return mb * MBYTE;
    }
    
    private static double convertKbToBytes(double kb) {
    	
    	return kb * KBYTE;
    }
    
    private List<GitRepository> groupFiles(List <String> filesGroup, String urlGit) {

    	System.out.println("RESUME:");
		int posStart = 0, posEnd = 0;
		String lines = "", extFile = "", ext="",size = "";
		Integer totalLinesGroup = 0;
		Double totalBytesGroup = 0.0;
		List<GitRepository> gitRepository =  new ArrayList<GitRepository>();
		
	    try {
	    	
	    	for (int i=0; i < filesGroup.size(); i++) {
	    		totalLinesGroup = 0;
	    		totalBytesGroup = 0.0;
	    		extFile = filesGroup.get(i);
	    		
	        	for (String line : listFiles) {
	
	        		posStart = line.indexOf(EXTENSION) + 10;
	        		posEnd = line.indexOf(SIZE);    		  
	        		if (posEnd >= posStart)
	        			ext = line.substring(posStart, posEnd).trim();
	  
					if (extFile.equals(ext)) {
					
						if (line.contains(LOG_LINES)){
							posStart = line.indexOf(LOG_LINES) + 7;
							posEnd = line.indexOf(FILE);
								  
						if (posEnd >= posStart)
							lines = line.substring(posStart, posEnd);
						} else
							lines = "0";	  
						
						totalLinesGroup += Integer.valueOf(lines); 
						
						posStart = line.indexOf(LOG_SIZE) + 6;
						posEnd = line.length() -6;
						
						if (posEnd >= posStart) {
							size = line.substring(posStart, posEnd);
							
							if(size.equals(null) || size.equals(""))
								size = "0";
							
							totalBytesGroup += Double.valueOf(size);
						} 
					}	
	        	}	
	    		gitRepository.add(new GitRepository(extFile, totalLinesGroup, totalBytesGroup, urlGit));
	  		  	System.out.println("Ext: ".concat(extFile).
	  				  concat(", Lines:").concat(String.valueOf(totalLinesGroup)).
	  				  concat(", Bytes:").concat(String.valueOf(totalBytesGroup).
	  				  concat(BYTES)));
					
			}
			
        } catch (Exception excecao) {
            excecao.getMessage();
        }
	    
		return gitRepository;
    }


}
