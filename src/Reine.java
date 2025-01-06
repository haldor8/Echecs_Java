import java.lang.reflect.Array;
import java.util.ArrayList;

public class Reine extends Pieces {

    public Reine( int proprietaire) {
        super( proprietaire);
        chargerIcon("Pieces/reine_" + this.get_Couleur_String() + ".svg");
    }


    @Override
    public boolean deplacement_valide(int x_initial, int y_initial, int x_final, int y_final, Pieces[][] echiquier) {
        int delta_x = Math.abs(x_final - x_initial);
        System.out.println("COPUCOU REINE");
        int delta_y = Math.abs(y_final - y_initial);

        Pieces piece_source = echiquier[x_initial][y_initial]; // = this;
        Pieces piece_dest = echiquier[x_final][y_final];

        if (piece_dest != null && piece_dest.get_proprietaire()== piece_source.get_proprietaire()) {
            return false;
        }

        if (delta_x == 0 || delta_y == 0 || delta_x == delta_y) {
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

    @Override
    public ArrayList<int[]> deplacements_possibles(int x, int y, Pieces[][] echiquier) {
        ArrayList<int[]> les_deplacements = new ArrayList<>();

        // Tableau des directions : [dx, dy]
        int[][] directions = {
                {1, 0},  // Droite
                {-1, 0}, // Gauche
                {0, 1},  // Bas
                {0, -1}, // Haut
                {1, 1},  // Diagonale bas-droite
                {-1, -1},// Diagonale haut-gauche
                {1, -1}, // Diagonale haut-droite
                {-1, 1}  // Diagonale bas-gauche
        };

        for (int[] direction : directions) {
            int dx = direction[0], dy = direction[1];
            int nx = x + dx, ny = y + dy;

            while (nx >= 0 && nx < 8 && ny >= 0 && ny < 8) {
                if (echiquier[nx][ny] == null) {
                    les_deplacements.add(new int[]{nx, ny});
                } else {
                    if (echiquier[nx][ny].get_proprietaire() != this.get_proprietaire()) {
                        les_deplacements.add(new int[]{nx, ny});
                    }
                    break;
                }
                nx += dx;
                ny += dy;
            }
        }

        return les_deplacements;
    }
}

