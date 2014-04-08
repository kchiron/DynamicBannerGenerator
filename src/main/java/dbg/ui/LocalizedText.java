package dbg.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.ParseException;

public class LocalizedText {

	//TODO: use Java ResourceBundle instead of this list of value

	public static String cancel						= "Annuler";
	public static String ok							= "Ok";
	public static String no							= "Non";
	public static String yes						= "Oui";
	public static String quit						= "Quitter";
	public static String save						= "Enregistrer";
	public static String save_settings				= "Enregistrer les réglages";
	public static String save_n_quit				= "Enregistrer et Quitter";
	public static String open						= "Ouvrir";
	public static String reload						= "Actualiser";
	public static String help						= "Aide";
	public static String error						= "Erreur";
	
	public static String settings					= "Réglages";
	public static String sequence_settings			= "Réglage séquence";
	public static String video_output_settings		= "Réglage sortie vidéo";
	public static String weather_settings			= "Réglage météo";
	public static String horoscope_settings			= "Réglage horoscope";
	public static String type						= "Type";
	public static String name						= "Nom";
	public static String video_size					= "Taille de la vidéo";
	public static String display_time				= "Temps d'affichage";
	public static String choose_background_image	= "Choix de l'image de fond";
	public static String background_image			= "Image de fond";
	public static String choose_an_image			= "Sélectionner une image";
	public static String national_weather			= "Météo nationale";
	public static String regional_weather			= "Météo régionale";
	public static String city_weather				= "Météo ville";
	public static String current_day				= "Le jour même";
	public static String the_next_day				= "Le jour d'après";
	public static String location					= "Localisation";
	public static String city_zip_code_state		= "Ville, Code postal, Pays";
	public static String nb_days_displayed			= "Jours affichés";
	public static String displayed_days				= "Affichage des jours";
	public static String others						= "Autres";
	public static String image						= "Image";
	public static String video						= "Vidéo";
	public static String save_the_file				= "Enregistrer le fichier";
	public static String playlist					= "Liste de lecture";
	public static String video_output_folder 		= "Dossier sortie vidéo";
	public static String choose_video_or_image_file	= "Sélectionner un fichier image ou vidéo";
	public static String choose_a_folder			= "Sélectionner un dossier";
	public static String no_file_selected			= "Aucun fichier sélectionné";
	public static String no_folder_selected			= "Aucun dossier sélectionné";
	public static String file_format				= "Format de fichier";
	public static String title						= "Titre";
	public static String video_or_image_file		= "Vidéo ou Image";
	public static String new_media					= "Nouveau média";
	public static String modify_media				= "Modifier un média";
	public static String error_file_not_found		= "Une erreur est survenue lors de la recherche du fichier.\nCelui-ci n'a pas pu être localisé.";
	public static String file_not_found				= "Fichier introuvable";
	public static String error_empty_title			= "titre manquant";
	public static String error_no_file_selected		= "fichier manquant";
	public static String error_display_time_zero	= "le temps d'affichage doit être supérieur à zero";
	public static String error_please_correct		= "Veuillez corriger les problèmes suivant: ";
	public static String error_cant_display			= "Aucun programme n'est associé à ce fichier pour permettre son affichage.";
	public static String missing_information		= "Information manquante";
	public static String remove						= "Supprimer";
	public static String edit						= "Modifier";
	public static String sign_per_page				= "Nombre de signes par page";
	
	//Horoscope signs
	public static String aries						= "Bélier";
	public static String taurus						= "Taureau";
	public static String gemini						= "Gémeaux";
	public static String cancer						= "Cancer";
	public static String leo						= "Lion";
	public static String virgo						= "Vierge";
	public static String libra						= "Balance";
	public static String scorpio					= "Scorpion";
	public static String sagittarius				= "Sagittaire";
	public static String capricorn					= "Capricorne";
	public static String aquarius					= "Verseau";
	public static String pisces						= "Poisson";
	public static String horoscope					= "Horoscope";
	
	public static String confirm_close				= "Voulez-vous sauvegarder les paramètres avant de quitter?";
	
	public static String enter_googleapikey			= "Veuillez entrer une clé API Google Places :";
	public static String missing_data				= "Information manquante";
	public static String googleapikey_required		= "Clé google API indispensable";
	
	public static String days						= "jour(s)";
	public static String elements 					= "élément(s)";
	public static String video_assembler			= "Assembleur Vidéo";
	public static String assemble_video 			= "Assembler la vidéo";
	public static String show						= "Afficher";
	public static String converting_elements		= "Convertion des éléments (images, videos, horoscope, ...)";
	public static String concatenating_videos		= "Assemblage final des vidéos";
	public static String adding_video				= "Ajout du fichier vidéo dans la liste";
	public static String video_assembly_canceled	= "Assemblage vidéo annulé!";
	public static String video_assembly_error 		= "Une erreur est survenue lors de l'assamblage vidéo!";
	
	
	/**
	 * Loads a language file replacing the default text
	 * @param langFile path to a language file with a specific syntax.<br/> syntax: <code>element=traduction</code> <br/> example: <code>title=Titre</code>
	 * @throws ParseException if the language file has a wrong syntax
	 * @throws IOException if an I/O error occurs during the file reading
	 */
	public static void loadLanguage(String langFile) throws ParseException, IOException {
		final BufferedReader br = new BufferedReader(new InputStreamReader(LocalizedText.class.getResourceAsStream(langFile)));

		String line;
		int numLine = 0;
		while ((line = br.readLine()) != null)   {
			numLine++;

			final String[] part = line.trim().split("=", 2);
			
			if(part.length == 2) {
				try {
					final Field f = LocalizedText.class.getField(part[0].trim());
					f.set(null, part[1].trim());
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {e.printStackTrace();}
			} 
			else 
				throw new ParseException("Wrong language file syntax", numLine);
		}
	}
}
