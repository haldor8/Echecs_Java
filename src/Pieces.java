import java.io.Serializable;

// Décris les informations des pièces
public class Pieces implements Serializable {
    private int num_ligne;
    private int num_colonne;
    private Client proprietaire;
    private boolean a_bouge;
    private List<String> liste_deplacement;
    private Image icon;
    private boolean estBlanc;

    public Pieces(int num_ligne, int num_colonne, Client proprietaire, boolean a_bouge, List<String> liste_deplacement, Image icon, boolean estBlanc) {
        this.num_ligne = num_ligne;
        this.num_colonne = num_colonne;
        this.proprietaire = proprietaire;
        this.a_bouge = a_bouge;
        this.liste_deplacement = liste_deplacement;
        this.icon = icon;
        this.estBlanc = estBlanc;
    }

    public int get_num_ligne() {
        return num_ligne;
    }

    public int get_num_colonne() {
        return num_colonne;
    }

    public Client get_proprietaire() {
        return proprietaire;
    }

    public boolean a_bouge() {
        return a_bouge;
    }

    public List<String> get_liste_deplacement() {
        return liste_deplacement;
    }

    public Image get_icon() {
        return icon;
    }

    public boolean est_blanc() {
        return estBlanc;
    }
}
