
import java.io.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Katia
 */
public class Horoscope {
    private String signs[] = new String[12];
    private String parts[] = new String[2];

    public Horoscope() {
        signs[0] = "belier";
        signs[1] = "taureau";
        signs[2] = "gemeaux";
        signs[3] = "cancer";
        signs[4] = "lion";
        signs[5] = "vierge";
        signs[6] = "balance";
        signs[7] = "scorpion";
        signs[8] = "sagittaire";
        signs[9] = "capricorne";
        signs[10] = "verseau";
        signs[11] = "poissons";
    }

    public String getSigns(int index) {
        return signs[index];
    }

    public String getParts(int index) {
        return parts[index];
    }
    
    /*
     * Retourne tout le contenu d'un signe
     */
    public String getContent(String signs) throws IOException  {
        String text;
        Document dec = Jsoup.connect("http://www.marieclaire.fr/astro/horoscope-du-jour/"+signs+"/").get();
        Elements content = dec.select("p.texte_paragraphe");
        text = "";

        for (Element e : content) {
            text += e.text() + " ";
        }
        
        return text;    
    }
}
