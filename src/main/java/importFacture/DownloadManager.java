package importFacture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;

import GUI.GUI;

public class DownloadManager {

	private static String adresseVersion="https://www.dropbox.com/s/bkaa5riuz41id3r/version.txt?dl=0";
	private static String adresseDownload="https://www.dropbox.com/sh/2y1k11acicxf4hf/AABqbR-ZNmdjd1nbi-879QOUa?dl=0";
	private static DownloadManager version=new DownloadManager();

	DownloadManager() {}

	static void controleVersion() {
		double versionDisponible=1.0;

		URL urlAdresseVersion=null;
		try {
			urlAdresseVersion = new URL(adresseVersion);
		} catch (MalformedURLException e) {			
			e.printStackTrace();
		}
		BufferedReader in=null;
		try {
			in = new BufferedReader(
					new InputStreamReader(urlAdresseVersion.openStream()));
		} catch (IOException e) {			
			e.printStackTrace();
		}



		String inputLine;
		try {
			while ((inputLine = in.readLine()) != null) {
			    GUI.messageConsole(inputLine);
			}
			in.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}    


		if( versionDisponible>Value.version) {
			GUIVersion guiVersion=version.new GUIVersion();
		}
	}


	static void importFactureWeb() {


	}


	private class GUIVersion extends JFrame{

		/**
		 * 
		 */
		private static final long serialVersionUID = -7375249253758118427L;

		public GUIVersion() {
			this.setTitle("Nouvelle version disponible");
			this.setSize(450, 200);
			this.setLocationRelativeTo(null);
			
		}



	}

}
