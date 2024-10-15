public class Cavalier extends Pieces {

    public Cavalier(int num_ligne, int num_colonne, Client proprietaire, boolean a_bouge, List<String> liste_deplacement, Image icon, boolean est_blanc) {
        super(num_ligne, num_colonne, proprietaire, a_bouge, liste_deplacement, icon, est_blanc);
    }

    @Override
    public boolean deplacement_valide(int x_initial, int y_initial, int x_final, int y_final, Pieces[][] echiquier) {
        int delta_x = Math.abs(x_final - x_initial);
        int delta_y = Math.abs(y_final - y_initial);

        if (echiquier[x_final][y_final] != null && echiquier[x_final][y_final].get_proprietaire().equals(this.get_proprietaire())) {
            return false;
        }

        return (delta_x == 2 && delta_y == 1) || (delta_x == 1 && delta_y == 2);
    }
}

