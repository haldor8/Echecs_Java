import java.io.Serializable;
import javax.swing.*;
import java.util.*;


// Décris les informations des pièces
public class Pieces implements Serializable {
    private int num_ligne;
    private int num_colonne;
    private int proprietaire;
    private boolean a_bouge = false;
    private List<String> liste_deplacement;
    private ImageIcon icon;

    public Pieces(int num_ligne, int num_colonne, int proprietaire/*, List<String> liste_deplacement, ImageIcon icon */) {
        this.num_ligne = num_ligne;
        this.num_colonne = num_colonne;
        this.proprietaire = proprietaire;
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

    public boolean a_bouge() {
        return a_bouge;
    }

    public List<String> get_liste_deplacement() {
        return liste_deplacement;
    }

    public ImageIcon get_icon() {
        return icon;
    }
}
