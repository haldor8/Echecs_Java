import javax.swing.*;
import java.lang.reflect.Array;
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

    @Override
    public ArrayList<int[]> deplacements_possibles(int x, int y, Pieces[][] echiquier) {
        ArrayList<int[]> les_deplacements = new ArrayList<>();

        // Déplacements possibles pour un roi : toutes les cases adjacentes
        int[][] directions = {
                {1, 0},   // Bas
                {-1, 0},  // Haut
                {0, 1},   // Droite
                {0, -1},  // Gauche
                {1, 1},   // Bas-droite
                {1, -1},  // Bas-gauche
                {-1, 1},  // Haut-droite
                {-1, -1}  // Haut-gauche
        };

        for (int[] direction : directions) {
            int nx = x + direction[0], ny = y + direction[1];

            if (nx >= 0 && nx < 8 && ny >= 0 && ny < 8) { // Vérifie que la case est valide
                if (echiquier[nx][ny] == null || echiquier[nx][ny].get_proprietaire() != this.get_proprietaire()) {
                    les_deplacements.add(new int[]{nx, ny}); // Case vide ou capture possible
                }
            }
        }

        return les_deplacements;
    }

}

