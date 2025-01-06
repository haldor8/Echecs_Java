import javax.swing.*;
import java.util.*;

public class Fou extends Pieces {

    public Fou( int proprietaire) {
        super(proprietaire);
        chargerIcon("Pieces/fou_" + this.get_Couleur_String() + ".svg");
    }


    @Override
    public boolean deplacement_valide(int x_initial, int y_initial, int x_final, int y_final, Pieces[][] echiquier) {
        int delta_x = Math.abs(x_final - x_initial);
        int delta_y = Math.abs(y_final - y_initial);

        // Le fou se déplace en diagonale, donc delta_x doit être égal à delta_y
        if (delta_x != delta_y) {
            return false;
        }

        // Calcul des étapes à prendre pour avancer en diagonale
        int step_x = Integer.compare(x_final, x_initial);
        int step_y = Integer.compare(y_final, y_initial);

        // Vérifier qu'il n'y a pas de pièce sur le chemin
        int x = x_initial + step_x;
        int y = y_initial + step_y;
        while (x != x_final && y != y_final) {
            // Si la case est occupée, il y a un blocage
            if (echiquier[x][y] != null) {
                return false; // Bloqué par une pièce
            }
            x += step_x;
            y += step_y;
        }

        return true; // Déplacement valide en diagonale
    }
    public ArrayList<int[]> deplacements_possibles(int x, int y, Pieces[][] echiquier) {
        ArrayList<int[]> les_deplacements = new ArrayList<>();

        // Directions possibles pour un fou : diagonales
        int[][] directions = {
                {1, 1},    // Bas-droite
                {1, -1},   // Bas-gauche
                {-1, 1},   // Haut-droite
                {-1, -1}   // Haut-gauche
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




    public String get_type_piece(){
        return "Fou";
    }
}

