import javax.swing.*;
import java.util.*;


import java.util.*;
import javax.swing.*;

public class Pion extends Pieces {

    public Pion(int num_ligne, int num_colonne, Client proprietaire, boolean a_bouge, List<String> liste_deplacement, Image icon, boolean est_blanc) {
        super(num_ligne, num_colonne, proprietaire, a_bouge, liste_deplacement, icon, est_blanc);
    }

    @Override
    public boolean deplacement_valide(int x_initial, int y_initial, int x_final, int y_final, Pieces[][] echiquier) {
        int delta_x = Math.abs(x_final - x_initial);
        int delta_y = y_final - y_initial;

        int direction = this.est_blanc() ? -1 : 1;

        if (echiquier[x_final][y_final] != null && echiquier[x_final][y_final].get_proprietaire().equals(this.get_proprietaire())) {
            return false;
        }

        if (delta_x == 0 && delta_y == direction && echiquier[x_final][y_final] == null) {
            return true;
        }

        if (!this.a_bouge() && delta_x == 0 && delta_y == 2 * direction && echiquier[x_final][y_final] == null && echiquier[x_initial][y_initial + direction] == null) {
            return true;
        }

        if (delta_x == 1 && delta_y == direction && echiquier[x_final][y_final] != null && !echiquier[x_final][y_final].get_proprietaire().equals(this.get_proprietaire())) {
            return true;
        }

        return false;
    }
}

