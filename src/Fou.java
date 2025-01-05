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


    public String get_type_piece(){
        return "Fou";
    }
}

