
import java.io.*;
import java.util.regex.*;
import java.util.*;

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
        
        parts[0] = "Coeur";
        parts[1] = "Vie active";
    }

    public String getSigns(int index) {
        return signs[index];
    }

    public String getParts(int index) {
        return parts[index];
    }   
    
    public ArrayList<String> getContent(InputStream info)  {
        BufferedReader in = new BufferedReader(new InputStreamReader(info));
        String inputLine;
        ArrayList<String> texts = new ArrayList<String>();
        Pattern pExtract = Pattern.compile("<p class=\"texte_paragraphe\">([^<]+)</p>");
        Matcher matchExtract;
        
        try{           
            while ((inputLine = in.readLine()) != null) {
                matchExtract = pExtract.matcher(inputLine);
                if (matchExtract.find())   {
                    texts.add(matchExtract.group(1));
                    //System.out.println("Ligne1 : " + matchExtract.group(1));
                }
            }
            in.close();
            if (texts.isEmpty())  {
                throw new Exception("Can not find available text");
            }
        } 
        catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }
        return texts;
    }
    
}
