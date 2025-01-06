import javax.swing.*;
import java.util.*;

public class Cavalier extends Pieces {


    public Cavalier(int proprietaire) {
        super( proprietaire);
        chargerIcon("Pieces/cavalier_" + this.get_Couleur_String()+ ".svg");
    }


    @Override
    public boolean deplacement_valide(int x_initial, int y_initial, int x_final, int y_final, Pieces[][] echiquier) {
        int delta_x = Math.abs(x_final - x_initial);
        int delta_y = Math.abs(y_final - y_initial);

        if (echiquier[x_final][y_final] != null && echiquier[x_final][y_final].get_proprietaire()==(this.get_proprietaire())) {
            return false;
        }

        return (delta_x == 2 && delta_y == 1) || (delta_x == 1 && delta_y == 2);
    }

    public ArrayList<int[]> deplacements_possibles(int x, int y, Pieces[][] echiquier) {
        ArrayList<int[]> les_deplacements = new ArrayList<>();

        // Déplacements possibles pour un cavalier
        int[][] mouvements = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        for (int[] mouvement : mouvements) {
            int nx = x + mouvement[0], ny = y + mouvement[1];

            if (nx >= 0 && nx < 8 && ny >= 0 && ny < 8) { // Vérifie que la case est valide
                if (echiquier[nx][ny] == null || echiquier[nx][ny].get_proprietaire() != this.get_proprietaire()) {
                    les_deplacements.add(new int[]{nx, ny}); // Case vide ou capture possible
                }
            }
        }

        return les_deplacements;
    }

}

