package dynamicbannergenerator;

import java.awt.*;
import java.io.*;
import nl.jamiecraane.imagegenerator.*;
import nl.jamiecraane.imagegenerator.imageexporter.*;
import nl.jamiecraane.imagegenerator.impl.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Katia
 */

public class ImageCreator {

    public void runExample() throws Exception {
        TextImage textImage = new TextImageImpl(300, 550, new Margin(25, 5, 50, 0));

        InputStream is = this.getClass().getResourceAsStream("/English.ttf");
        Font usedFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(11.0f);

        textImage.useTextWrapper(new GreedyTextWrapper());
        textImage.setTextAligment(Alignment.LEFT);
        textImage.useFont(usedFont);

        textImage.wrap(true).write("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever sinze the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.").newLine();
        textImage.setTextAligment(Alignment.RIGHT).write("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever sinze the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.").newLine();
        textImage.setTextAligment(Alignment.CENTER).write( "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever sinze the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.").newLine();
        textImage.setTextAligment(Alignment.JUSTIFY).write("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever sinze the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.").newLine();
        textImage.wrap(false).useFontStyle(Style.UNDERLINED).setTextAligment(Alignment.LEFT).newLine().write("This text falls of the line because wrapping is disabled again.");

        ImageWriter imageWriter = ImageWriterFactory.getImageWriter(ImageType.PNG);
        imageWriter.writeImageToFile(textImage, new File("textwrap.png"));
    }
}