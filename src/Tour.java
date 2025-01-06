import javax.swing.*;
import java.util.*;

public class Tour extends Pieces {

    public Tour( int proprietaire) {
        super( proprietaire);
        chargerIcon("Pieces/tour_" +this.get_Couleur_String() + ".svg");
    }


    @Override
    public boolean deplacement_valide(int x_initial, int y_initial, int x_final, int y_final, Pieces[][] echiquier) {
        int delta_x = Math.abs(x_final - x_initial);
        int delta_y = Math.abs(y_final - y_initial);

        if (echiquier[x_final][y_final] != null && echiquier[x_final][y_final].get_proprietaire()==(this.get_proprietaire())) {
            return false;
        }

        if (delta_x == 0 || delta_y == 0) {
            int step_x = Integer.compare(x_final, x_initial);
            int step_y = Integer.compare(y_final, y_initial);

            int x = x_initial + step_x;
            int y = y_initial + step_y;

            while (x != x_final || y != y_final) {
                if (echiquier[x][y] != null) {
                    return false;
                }
                x += step_x;
                y += step_y;
            }
            return true;
        }

        return false;
    }

    public ArrayList<int[]> deplacements_possibles(int x, int y, Pieces[][] echiquier) {
        ArrayList<int[]> les_deplacements = new ArrayList<>();

        // Directions possibles pour une tour : haut, bas, gauche, droite
        int[][] directions = {
                {1, 0},   // Bas
                {-1, 0},  // Haut
                {0, 1},   // Droite
                {0, -1}   // Gauche
        };

        for (int[] direction : directions) {
            int dx = direction[0], dy = direction[1];
            int nx = x + dx, ny = y + dy;

            // Parcours dans une direction jusqu'à rencontrer un obstacle ou sortir du plateau
            while (nx >= 0 && nx < 8 && ny >= 0 && ny < 8) {
                if (echiquier[nx][ny] == null) {
                    les_deplacements.add(new int[]{nx, ny}); // Case vide
                } else {
                    if (echiquier[nx][ny].get_proprietaire() != this.get_proprietaire()) {
                        les_deplacements.add(new int[]{nx, ny}); // Capture possible
                    }
                    break; // On s'arrête après avoir rencontré une pièce
                }
                nx += dx;
                ny += dy;
            }
        }

        return les_deplacements;
    }

}

