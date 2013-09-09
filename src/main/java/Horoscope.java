
import java.io.*;
import java.util.regex.*;
import java.util.*;
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
       
    public ArrayList<String> getContent(String [] signs) throws IOException  {
        for (int i = 0; i < signs.length; i++) {
            Document dec = Jsoup.connect("http://www.marieclaire.fr/astro/horoscope-du-jour/"+signs[i]+"/").get();
            System.out.println("url : http://www.marieclaire.fr/astro/horoscope-du-jour/"+signs[i]+"/");
            Elements content = dec.select("p.texte_paragraphe");

            for (Element e : content) {
                e.text();
                System.out.println(e.text());
            }
        }
        return null;
    }
    
}
