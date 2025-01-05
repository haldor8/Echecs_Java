import java.awt.*;
import java.io.Serializable;
import javax.swing.*;
import java.util.*;
import org.apache.batik.transcoder.*;
import org.apache.batik.transcoder.image.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;


// Décris les informations des pièces
public class Pieces implements Serializable {
    private int proprietaire;
    private ImageIcon icon;
    private int couleur; // 1 pour blanc, 2 pour noir
    private String la_couleur; //noir ou blanc

    public Pieces(int proprietaire) {
        this.proprietaire = proprietaire;
        la_couleur = proprietaire == 1 ? "blanc" : "noir";
    }

    public boolean deplacement_valide(int x_initial, int y_initial, int x_final, int y_final, Pieces[][] echiquier){
        return true;
    }

    public int get_proprietaire() {
        return proprietaire;
    }
    public int get_Couleur(){return couleur;}
    //retourne la couleur (noir ou blanc, plutôt que le  int qui est utile pour certaines manips)
    public String get_Couleur_String(){return la_couleur;}


    public ImageIcon get_icon() {
        return icon;
    }

    public void chargerIcon(String cheminSVG) {
        try {
            // Transcodeur pour convertir SVG en BufferedImage
            TranscoderInput input = new TranscoderInput(new File(cheminSVG).toURI().toString());
            BufferedImageTranscoder transcoder = new BufferedImageTranscoder();

            transcoder.transcode(input, null);
            BufferedImage image = transcoder.getBufferedImage();
            icon = new ImageIcon(image); // Convertir en ImageIcon
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class BufferedImageTranscoder extends ImageTranscoder {
        private BufferedImage image;

        @Override
        public BufferedImage createImage(int width, int height) {
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bufferedImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.dispose();
            return bufferedImage;
        }


        @Override
        public void writeImage(BufferedImage image, TranscoderOutput output) {
            this.image = image;
        }

        public BufferedImage getBufferedImage() {
            return image;
        }
    }
}