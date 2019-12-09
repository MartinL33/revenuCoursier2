package fr.algorithmes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScannerFolder {
	
	private String extention = null;
	private File directory = null;	
	private List<File> result = new ArrayList<File>();
		
	public void setExtention(String extention) {
		this.extention = extention;
		result = new ArrayList<File>();
	}

	public void setDirectory(File directory) {
		this.directory = directory;
	}
	
	
	public List<File> find(){
		assertValid();
		findFile(directory);	
		return result;
	}

	public boolean hasResult() {
		assertValid();
		findFile(directory);
		if(result.isEmpty()) return false;
		return result.size() > 0;
	}

	private void assertValid() {
		assert extention != null;
		assert directory != null;
		assert directory.exists();
		assert directory.isDirectory();
		assert directory.canRead();
	}

	private void findFile(final File file) {
		if (file.isFile()
				&& file.getName().endsWith(extention)) {
			result.add(file);
		}	    

		else if (file.isDirectory()) {
			File[] listeFichier = file.listFiles();
			for (File fichier : listeFichier) {
				findFile(fichier);
			}
		}
	}	
}
