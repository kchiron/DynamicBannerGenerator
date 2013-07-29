package dynamicbannergenerator;


import java.io.*;
import java.net.*;
import java.util.regex.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Katia
 */
public class URLConnectionReader {
    URL link;
    private URLConnection connect;
    

    public URLConnectionReader(String sign) throws Exception {       
        this.link = new URL("http://www.marieclaire.fr/astro/horoscope-du-jour/" + sign + "/");
    }

    public URL getLink() {
        return link;
    }
    
    public InputStream connect() throws IOException   {
        connect = link.openConnection();
        return connect.getInputStream();
    }
}
