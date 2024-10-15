// Décris les pions
public class Pion extends Pieces {
    public Pion(int num_ligne, int num_colonne, Client proprietaire, boolean a_bouge, List<String> liste_deplacement, Image icon, boolean estBlanc) {
        super(num_ligne, num_colonne, proprietaire, a_bouge, liste_deplacement, icon, estBlanc);
    }

    public boolean peut_se_deplacer(int nouveauX, int nouveauY) {
        if (est_blanc()) {
            return (nouveauX == get_num_colonne() && (nouveauY == get_num_ligne() + 1 || (get_num_ligne() == 1 && nouveauY == get_num_ligne() + 2)));
        } else {
            return (nouveauX == get_num_colonne() && (nouveauY == get_num_ligne() - 1 || (get_num_ligne() == 6 && nouveauY == get_num_ligne() - 2)));
        }
    }
}
