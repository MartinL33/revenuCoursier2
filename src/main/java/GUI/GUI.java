package GUI;



import static importFacture.Text.textDossierChoisi;
import static importFacture.Value.tabRegroupage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import importFacture.Action;
import importFacture.Value;
import importFacture.ValueParametresGUI;

public class GUI extends JFrame{

	ValueParametresGUI valuesGUI=new ValueParametresGUI();
	static boolean detectFileMap=false;
	static Locale langue=Locale.getDefault();

	private static final long serialVersionUID = 1L;

	static int widthFenetre=450;
	static int heightFenetre=200;

	private static ResourceBundle guiLangBundle = ResourceBundle.getBundle("importFacture.GUILang",langue ); 

	private final JPanel panGUI = new JPanel();

	private final JPanel panSelectPath = new JPanel();    
	private final JLabel labelSelectPath=new JLabel((String)guiLangBundle.getString("textSelectPath"));

	private final JPanel panPath = new JPanel();
	private final JLabel labelPath = new JLabel("");
	private final JButton boutonPath = new JButton((String)guiLangBundle.getString("textBoutonPath"));

	private final JPanel panRegroupage = new JPanel();
	private final JLabel labelRegroupage = new JLabel((String)guiLangBundle.getString("textlabelRegroupage"));
	private final JComboBox<String> comboRegroupage = new JComboBox<String>(getListComboTextRecoupage());


	private final JPanel panTypeRegroupage = new JPanel();
	private final JLabel labelTypeRegroupage = new JLabel("type regroupage");
	private final JComboBox<String> comboTypeRegroupage = new JComboBox<String>(Value.typeRegroupage);


	private JCheckBox isSavingCheck = new JCheckBox((String)guiLangBundle.getString("textCheck"));
	private   JPanel panelCheck = new JPanel();


	private final JPanel panBoutonLancer = new JPanel();
	private final static JButton boutonLancer = new JButton((String)guiLangBundle.getString("textboutonLancer"));    

	private final JPanel panGPS = new JPanel();	
	private final JPanel panVide1 = new JPanel();


	private final JPanel panExport = new JPanel();	
	private JCheckBox isExporterCarte = new JCheckBox("Exporter la carte des shifts");

	private final JPanel panSelectOptionCarte=new JPanel();

	private final JPanel panLimiteTaille = new JPanel();	
	private JCheckBox isLimiteTailleCarte = new JCheckBox("Limiter la taille de la map à 5 Mo (pour google map)");

	private final JPanel panCouleur = new JPanel();	
	private final JLabel labelSelectCouleur=new JLabel("Couleur en fonction ");
	private final JComboBox<?> comboCouleurCarte = new JComboBox<Object>(Map.EcrireMap.couleurMap);



	private final JPanel panisSupprimerShiftCarte = new JPanel();
	private JCheckBox isSupprimerShiftCarte = new JCheckBox("Reset shift sans positions GPS fiable");



	private final JPanel panVide2 = new JPanel();
	private final JPanel panlabelMessage = new JPanel();
	static private final JLabel labelMessage=new JLabel("");

	private final JPanel panMail1 = new JPanel();	
	private final JLabel labelMail1 = new JLabel("Pour établir un classement des villes,");
	private final JPanel panMail2 = new JPanel();
	private final JLabel labelMail2 = new JLabel("envoyez vos factures sur statcoursier@outlook.fr");


	public static void main(String[] args) {
		for(String s:args) {
			if(s.equals("--debug")||s.equals("-d")) {
				Value.debug=true;
				break;
			}
		}
		System.out.println("Debug:"+String.valueOf(Value.debug) );
		if(Value.debug) Value.tabRegroupage=Value.tabRegroupageDebug;
		else Value.tabRegroupage=Value.tabRegroupageNormal;

		new GUI();		
	}

	public static void updateUI(String message) {
		labelMessage.setText(message);
		labelMessage.repaint();		    
		messageConsole(message); 
	}

	public static void setLancer() {
		boutonLancer.setText("Lancer");
	}
	public static void setStop() {
		boutonLancer.setText("Stop");
	}


	private String[] getListComboTextRecoupage(){
		String[] result=new String[tabRegroupage.length];    	
		for (int i=0;i<tabRegroupage.length;i++) {
			result[i]=tabRegroupage[i].getTextCombo();
		}		
		return result;
	}

	private GUI(){	

		//get local graphics environment
		GraphicsEnvironment graphicsEnvironment=GraphicsEnvironment.getLocalGraphicsEnvironment();

		//get maximum window bounds
		Rectangle maximumWindowBounds=graphicsEnvironment.getMaximumWindowBounds();

		int widthLocaFenetre=(int) ((maximumWindowBounds.getWidth()-widthFenetre)/2);
		int heightLocaFenetre=(int) ((maximumWindowBounds.getHeight()-heightFenetre)/2);


		this.setSize(widthFenetre, heightFenetre);
		this.setLocation(widthLocaFenetre, heightLocaFenetre);

		this.setTitle((String)guiLangBundle.getObject("nameApp"));		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 



		boutonPath.addActionListener(new BoutonCheminListener());

		comboRegroupage.setSize(new Dimension(200,20));
		boutonLancer.addActionListener(new BoutonLancerListener());
		boutonLancer.setVisible(false);


		panGUI.setLayout(new BoxLayout(panGUI, BoxLayout.PAGE_AXIS));


		panSelectPath.setLayout(new BorderLayout());
		panSelectPath.add(labelSelectPath,BorderLayout.WEST);
		panGUI.add(panSelectPath);

		panPath.setLayout(new BorderLayout());	
		panPath.add(labelPath,BorderLayout.WEST);
		panPath.add(boutonPath,BorderLayout.EAST);
		panGUI.add(panPath);

		panRegroupage.setLayout(new BorderLayout());
		panRegroupage.add(labelRegroupage,BorderLayout.CENTER);	
		panRegroupage.add(comboRegroupage,BorderLayout.EAST);			
		panGUI.add(panVide1);
		panGUI.add(panRegroupage);

		panTypeRegroupage.setLayout(new BorderLayout());
		panTypeRegroupage.add(labelTypeRegroupage,BorderLayout.CENTER);
		panTypeRegroupage.add(comboTypeRegroupage,BorderLayout.EAST);

		if(Value.debug) panGUI.add(panTypeRegroupage);


		isSavingCheck.setSelected(Value.isSaving);
		isSavingCheck.addActionListener(new CheckisSavingListener());
		panelCheck.setLayout(new BorderLayout());
		panelCheck.add(isSavingCheck,BorderLayout.WEST);		
		if(Value.debug) panGUI.add(panelCheck);


		isExporterCarte.setSelected(Value.isExportCarte);
		isExporterCarte.addActionListener(new CheckisExporterCarteListener());

		panGPS.setLayout(new BoxLayout(panGPS, BoxLayout.PAGE_AXIS));
		panGPS.add(panVide1);



		panExport.setLayout(new BorderLayout());
		panExport.add(isExporterCarte,BorderLayout.WEST);	
		panGPS.add(panExport);

		panSelectOptionCarte.setLayout(new BoxLayout(panSelectOptionCarte, BoxLayout.PAGE_AXIS));

		comboCouleurCarte.setSize(new Dimension(100,20));

		panCouleur.add(labelSelectCouleur);
		panCouleur.add(comboCouleurCarte);

		panSelectOptionCarte.add(panCouleur);	
		isLimiteTailleCarte.setSelected(Value.isLimiteTailleCarte);		
		panLimiteTaille.add(isLimiteTailleCarte);

		panSelectOptionCarte.add(panLimiteTaille);

		panGPS.add(panSelectOptionCarte);


		isSupprimerShiftCarte.setSelected(Value.isSupprimerShiftSansGPSfiable);
		panisSupprimerShiftCarte.setLayout(new BorderLayout());
		panisSupprimerShiftCarte.add(isSupprimerShiftCarte,BorderLayout.WEST);
		if(Value.debug) panGPS.add(panisSupprimerShiftCarte);

		panGUI.add(panGPS);
		panGPS.setVisible(detectFileMap);

		panBoutonLancer.setLayout(new BorderLayout());	
		panBoutonLancer.add(boutonLancer,BorderLayout.EAST);
		panGUI.add(panVide2);
		panGUI.add(panBoutonLancer);	

		panlabelMessage.setLayout(new BorderLayout());	
		panlabelMessage.add(labelMessage,BorderLayout.EAST);		
		panGUI.add(panlabelMessage);

		panMail1.setLayout(new BorderLayout());	
		panMail1.add(labelMail1);		
		panGUI.add(panMail1);
		panMail2.setLayout(new BorderLayout());	
		panMail2.add(labelMail2);		
		panGUI.add(panMail2);

		this.setContentPane(panGUI);
		this.setVisible(true);


	}

	class BoutonCheminListener implements ActionListener{


		@Override
		public void actionPerformed(ActionEvent arg0) {

			// création de la boîte de dialogue
			JFileChooser dialogue = new JFileChooser();
			dialogue.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			dialogue.setMultiSelectionEnabled(false);
			dialogue.setControlButtonsAreShown(true);
			dialogue.setDialogTitle((String)guiLangBundle.getString("textDialogTitle"));
			// affichage
			dialogue.showOpenDialog(null);

			// récupération du fichier sélectionné
			String pathTest=dialogue.getSelectedFile()+""; 

			if(pathTest!=null&&!pathTest.isEmpty()&&!pathTest.equals("")){
				File fileTest=new File(pathTest);
				if(fileTest.exists()&&fileTest.isDirectory()) {

					labelPath.setText(pathTest);
					labelPath.repaint();	   
					messageConsole(textDossierChoisi+ pathTest);
					labelMessage.setText("");
					labelMessage.repaint();
					boutonLancer.setVisible(true);
					Action.setFileImport(fileTest);

					detectFileMap=Action.detectFilesMaps(fileTest);
					panGPS.setVisible(detectFileMap);				

					if(panGPS.isVisible()) {
						isSupprimerShiftCarte.setSelected(true);
						isLimiteTailleCarte.setSelected(true);
						isExporterCarte.setSelected(true);		
						panSelectOptionCarte.setVisible(isExporterCarte.isSelected());
						setSize(450, 300);					
					}
					else {
						setSize(450, 200);	
					}
					repaint();
				}
			}

		}
	}

	class BoutonLancerListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {

			Value.isExportCarte=isExporterCarte.isSelected();
			Value.isSupprimerShiftSansGPSfiable=isSupprimerShiftCarte.isSelected();
			Value.isLimiteTailleCarte=isLimiteTailleCarte.isSelected();
			Value.couleurSelected=Map.EcrireMap.couleurMap[comboCouleurCarte.getSelectedIndex()];
			Value.regroupageSelected=Value.tabRegroupage[comboRegroupage.getSelectedIndex()];
			Value.typeRegroupageSelected=Value.typeRegroupage[comboTypeRegroupage.getSelectedIndex()];
			Action.execute();

		}
	}  



	class CheckisSavingListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			Value.isSaving=(boolean) ((JCheckBox)e.getSource()).isSelected() ;
			if(Value.isSaving) 	{
				//TODO
				// DataFromDirectory.send();		
			}
		}
	}


	class CheckisExporterCarteListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			panSelectOptionCarte.setVisible(isExporterCarte.isSelected());
		}
	}

	public static void messageConsole(String mess) {
		if(Value.debug) System.out.println(mess);
	}



}





