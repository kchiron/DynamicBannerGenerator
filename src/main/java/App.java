package dynamicbannergenerator;


import java.io.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Katia
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        URLConnectionReader link;
        Horoscope h = new Horoscope();
        InputStream input;
        ImageCreator test = new ImageCreator();
        
        test.runExample();
        
//        for (int i = 0; i < 12; i++) {
//            System.out.println("sign : " + h.getSigns(i));
//            link = new URLConnectionReader(h.getSigns(i));
//            input = link.connect();
//            h.getContent(input);
//        }
        
    }
}
