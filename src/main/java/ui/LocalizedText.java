package ui;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

import javax.swing.Icon;

public class LocalizedText {
	public static String settings					= "Réglages";
	public static String sequence_settings			= "Réglage séquence";
	public static String video_output_settings		= "Réglage sortie vidéo";
	public static String weather_settings			= "Réglage météo";
	public static String horoscope_settings			= "Réglage horoscope";
	public static String type						= "Type";
	public static String name						= "Nom";
	public static String cancel						= "Annuler";
	public static String ok							= "Ok";
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
	public static String city_zip_code_state		= "Ville, Code postal, état";
	public static String displayed_days				= "Affichage des jours";
	public static String other						= "Autre";
	public static String image						= "Image";
	public static String video						= "Vidéo";
	public static String save						= "Enregistrer";
	public static String open						= "Ouvrir";
	public static String reload						= "Actualiser";
	public static String help						= "Aide";
	public static String save_the_file				= "Enregistrer le fichier";
	public static String playlist					= "Liste de lecture";
	public static String video_output_folder 		= "Dossier sortie vidéo";
	public static String choose_video_or_image_file	= "Sélectionner un fichier image ou vidéo";
	public static String choose_a_folder			= "Sélectionner un dossier";
	public static String no_file_selected			= "Aucun fichier sélectionner";
	public static String no_folder_selected			= "Aucun dossier sélectionner";
	public static String file_format				= "Format de fichier";
	public static String title						= "Titre";
	public static String video_or_image_file		= "Vidéo ou Image";
	public static String new_media					= "Nouveau média";
	public static String modify_media				= "Modifier un média";
	
	public static void loadLanguage(String langFile) {
		try {
			final BufferedReader br = new BufferedReader(new InputStreamReader(LocalizedText.class.getResourceAsStream(langFile)));
			
			String line;
			while ((line = br.readLine()) != null)   {
				final String[] part = line.trim().split("=", 2);
	
				if(part.length == 2) {
					final Field f = LocalizedText.class.getField(part[0].trim());
					f.set(null, part[1].trim());
				}
			}
		} catch(final Exception e) {
			System.out.println("Couln't load the language file!");
			e.printStackTrace();
		}
	}
}
