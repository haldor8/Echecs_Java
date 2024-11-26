import javax.swing.*;
import java.util.*;

public class Pion extends Pieces {

    public Pion(int num_ligne, int num_colonne, int proprietaire) {
        super(num_ligne, num_colonne, proprietaire);
        String couleur = proprietaire == 0 ? "blanc" : "noir";
        chargerIcon("Pieces/pion_" + couleur + ".svg");
    }
    public boolean deplacement_valide(int x_initial, int y_initial, int x_final, int y_final, Pieces[][] echiquier) {
        int delta_x = Math.abs(x_final - x_initial);
        int delta_y = y_final - y_initial;

        int direction = this.get_proprietaire() == 0 ? -1 : 1; // 0 = blanc (à voir pour changer en enum)

        if (echiquier[x_final][y_final] != null && echiquier[x_final][y_final].get_proprietaire() == (this.get_proprietaire())) {
            return false;
        }

        if (delta_x == 0 && delta_y == direction && echiquier[x_final][y_final] == null) {
            return true;
        }

        if (!this.a_bouge() && delta_x == 0 && delta_y == 2 * direction && echiquier[x_final][y_final] == null && echiquier[x_initial][y_initial + direction] == null) {
            return true;
        }

        if (delta_x == 1 && delta_y == direction && echiquier[x_final][y_final] != null && !(echiquier[x_final][y_final].get_proprietaire() == (this.get_proprietaire()))) {
            return true;
        }

        return false;
    }

}

