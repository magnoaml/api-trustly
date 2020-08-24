package br.com.trustlyapi.model;

public class GitRepository {
	
	private String urlRepository;
	private int totalLines;
	private double totalBytes;
	private String extension;
	
	public GitRepository(){
	}
 
	public GitRepository(String extension, int lines, double bytes, String urlRepository) {
		super();
		this.extension = extension;
		this.totalLines = lines;	
		this.totalBytes = bytes;
		this.urlRepository = urlRepository;
	}
	

	public String getUrlRepository() {
		return urlRepository;
	}
	public void setUrlRepository(String urlRepository) {
		this.urlRepository = urlRepository;
	}
	public int getTotalLines() {
		return totalLines;
	}
	public void setTotalLines(int lines) {
		this.totalLines = lines;
	}
	public double getTotalBytes() {
		return totalBytes;
	}
	public void setTotalBytes(double bytes) {
		this.totalBytes = bytes;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}

	
}
