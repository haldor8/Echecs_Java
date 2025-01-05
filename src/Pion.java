public class Pion extends Pieces {

    public Pion(int proprietaire) {
        super( proprietaire);
        chargerIcon("Pieces/pion_" + this.get_Couleur_String() + ".svg");
    }

    public boolean deplacement_valide(int x, int y, int x_final, int y_final, Pieces[][] matrice) {
        System.out.println("deplacement piooooooooooooooooooooooooooooooooooooooon");
        // si l'on est hors du plateau
        if (x_final < 0 || x_final >= 8 || y_final < 0 || y_final >= 8) {
            return false;
        }

            // Exemple pour un pion : se déplacer de 1 case en avant ou 2 cases au premier coup
        if (this instanceof Pion) {
            int direction = (this.get_proprietaire() == 1) ? -1 : 1; // Pion vers le haut (1) ou vers le bas (2)
            if (x_final == x + direction && y_final == y) {
                // Déplacement simple d'une case
                return matrice[x_final][y_final] == null; // Case vide
            } else if (x_final == x + (2 * direction) && y_final == y && x == (this.get_proprietaire() == 1 ? 6 : 1)) {
                // Déplacement de deux cases (si c'est le premier coup)
                return matrice[x_final][y_final] == null; // Case vide
            }
            // Cas pour les captures (diagonales)
            if (Math.abs(x_final - x) == 1 && Math.abs(y_final - y) == 1 && matrice[x_final][y_final] != null && matrice[x_final][y_final].get_proprietaire() != this.get_proprietaire()) {
                return true; // Capture valide
            }
        }
        return false;
    }
}