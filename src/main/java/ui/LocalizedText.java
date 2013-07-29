package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

import javax.swing.Icon;

public class LocalizedText {
	public static String sequence_settings			= "Réglage séquence";
	public static String video_settings				= "Réglage vidéo";
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
	public static String select_an_image			= "Sélectionner une image";
	public static String national_weather			= "Météo nationale";
	public static String regional_weather			= "Météo régionale";
	public static String city_weather				= "Météo ville";
	public static String current_day				= "Le jour même";
	public static String the_next_day				= "Le jour d'après";
	public static String location					= "Localisation";
	public static String city_zip_code_state		= "Ville, Code postal, État";
	public static String displayed_days				= "Affichage des jours";
	public static String other						= "Autre";
	public static String image						= "Image";
	public static Object save						= "Enregistrer";
	public static Object open						= "Ouvrir";
	public static Object reload						= "Actualiser";
	public static Object help						= "Aide";
	public static Object save_the_file				= "Enregistre le fichier";
	
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
