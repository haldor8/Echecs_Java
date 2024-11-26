import java.io.Serializable;
import javax.swing.*;
import java.util.*;
import org.apache.batik.transcoder.*;
import org.apache.batik.transcoder.image.*;

import java.awt.image.BufferedImage;
import java.io.File;


// Décris les informations des pièces
public class Pieces implements Serializable {
    private int num_ligne;
    private int num_colonne;
    private int proprietaire;
    private boolean a_bouge = false;
    private List<String> liste_deplacement;
    private ImageIcon icon;
    private int couleur; // 1 pour blanc, 2 pour noir
    private String la_couleur; //noir ou blanc

    public Pieces(int num_ligne, int num_colonne, int proprietaire/*, List<String> liste_deplacement, ImageIcon icon */) {
        this.num_ligne = num_ligne;
        this.num_colonne = num_colonne;
        this.proprietaire = proprietaire;
        la_couleur = proprietaire == 1 ? "blanc" : "noir";
        /*
        this.liste_deplacement = liste_deplacement;
        this.icon = icon;
        */
    }

    public int get_num_ligne() {
        return num_ligne;
    }

    public int get_num_colonne() {
        return num_colonne;
    }

    public int get_proprietaire() {
        return proprietaire;
    }
    public int get_Couleur(){return couleur;}
    //retourne la couleur (noir ou blanc, plutôt que le  int qui est utile pour certaines manips)
    public String get_Couleur_String(){return la_couleur;}

    public boolean a_bouge() {
        return a_bouge;
    }

    public List<String> get_liste_deplacement() {
        return liste_deplacement;
    }

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
            return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
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