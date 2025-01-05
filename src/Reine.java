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

        if (echiquier[x_final][y_final] != null && echiquier[x_final][y_final].get_Couleur()==(this.get_Couleur())) {
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
}

