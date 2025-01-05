import javax.swing.*;
import java.util.*;

public class Roi extends Pieces {

    public Roi(int proprietaire) {
        super(proprietaire);
        chargerIcon("Pieces/roi_" + this.get_Couleur_String()+ ".svg");
    }


    @Override
    public boolean deplacement_valide(int x_initial, int y_initial, int x_final, int y_final, Pieces[][] echiquier) {
        int delta_x = Math.abs(x_final - x_initial);
        int delta_y = Math.abs(y_final - y_initial);

        if (echiquier[x_final][y_final] != null && echiquier[x_final][y_final].get_proprietaire()==(this.get_proprietaire())) {
            return false;
        }

        return delta_x <= 1 && delta_y <= 1;
    }
}

