# revenuCoursier2

Utilitaire revenu-coursier

But de l'utilitaire: 
	Transformer un ensemble de factures Deliveroo en des statistiques utiles aux coursiers

Résultat: 
  Voir l'étude sur la rémunération des coursiers Deliveroo qui présente en détail les résultats. 


Mode d'emploi de l'utilitaire : 

Etape préliminaire: 
	
	1.télécharger l'utilitaire
  	2.si Java n'est pas installé ou la version est inférieur à 1.8 
  -> installer ou mettre à jour java sur https://www.java.com/fr/download/

Lancement: 
	
	clic droit sur le fichier statCoursier.jar puis ouvrir avec java ou openJDK 
	Dans certains cas, sous linux/mac, il faut autoriser l'exécution comme programme 																		
				
Utilisation:
			
	1. Sélectionner le dossier contenant les factures en cliquant sur ouvrir
		
		Le nom des factures n'a pas d'importance 
		Les factures peuvent être mises dans des sous-dossiers 
		Les factures peuvent être en double 
		Il peut y avoir des fichiers qui ne sont pas des factures		
		Il peut y avoir des factures de plusieurs coursiers différents
		
	2. Sélectionner le regroupage des shifts (par défaut, mensuel) 
		le mode ne pas regrouper permet de contrôler le bon fonctionnement de l'utilitaire
		
	3. Appuyer sur Lancer
		
Résultats:

	Les résultats sont dans un fichier .csv dans le dossier contenant les factures

	Votre tableur préféré (LibreOffice ou Excel) peut ouvrir les fichiers .csv en précisant comme unique séparateur le point virgule ; 

	La date correspond à la date du premier shift de la période
	La durée est en heure décimale: 1,5 est égale à 1heure 30minute	
	Le CA horaire est égale au CA divisé par la durée 
	Les pourboires sont obtenus dans le paragraphe résumé en bas des factures et réparti entre les shifts au prorata du nombre de commande
	Le regroupement par heure s'effectue en divisant les shifts par heure et en attribuant le CA au prorata de la durée puis en les regroupant.

	Avec le tableur, vous pouvez ensuite faire des courbes de votre activité et comparer vos stats avec d'autres coursiers.
	Pour comparer vos stats, vous trouverez dans le dossier mes factures (je shifte à Bordeaux et je ne suis pas rapide bien qu'en électrique depuis mars 2019).

Maps:

	L'utilitaire vous permet également de générer une carte de vos shifts dont voici un exemple: https://drive.google.com/open?id=16wHEOFWTQQI91HIjIny5zlM64aRlQXpg&usp=sharing	

	Pour utiliser cette fonctionnalité, il faut :

	1. Obtenir l'historique de position:
	  1.1   il faut aller sur la page https://takeout.google.com/settings/takeout
	  1.2   appuyer sur ne rien sélectionner
	  1.3.  appuyer sur Historique des positions en format JSON
	  1.4.  appuyer sur suivant
	  1.5.  créer une archive
	  1.6.  aller dans le dossier téléchargement
	  1.7.  extraire le ficher zip qui ressemble à takeout-20181015T114101Z-001	
	  1.8   ouvrir le dossier takeout et mettre le fichier "Historique des positions.json" dans le dossier contenant les factures (d'un seul coursier)
	  1.9   lancer l'utilitaire

	2. Affichage résultats:
	  2.1.   rdv sur google mymaps https://www.google.com/maps/d/ 
	  2.2    créer une carte
	  2.3.   cliquer sur importer
	  2.4.   faites glisser ou sélectionner le fichier carte1.kml situé dans le dossier facture. Il peut y avoir plusieurs cartes en cas de gros livreur.

Planification développement: 
	
	Test de l'utilitaire notamment avec des factures antérieures à avril 2016
	Améliorer l'interface graphique
	Clarifier le  code
	Ajout de l'importation des récapitulatifs uber
	Detecter les shifts dont l'intervale est trop court pour rentrer au domicile

Retours: 

	Il est possible que des bugs (sans gravité) soit présent. 
	Si vous avez des questions, des remarques, des souhaits ou que vous voulez m'aider: 
	Je suis preneur: statcoursier@outlook.fr
