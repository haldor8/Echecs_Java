public class Pion extends Pieces {

    public Pion(int proprietaire) {
        super(proprietaire); // 1 pour blanc, 2 pour noir
        chargerIcon("Pieces/pion_" + this.get_Couleur_String() + ".svg"); // Charger l'icon SVG correcte
    }

    @Override
    public boolean deplacement_valide(int x_initial, int y_initial, int x_final, int y_final, Pieces[][] echiquier) {
        // Calcul des différences sur les axes X (vertical) et Y (horizontal)
        int delta_x = x_final - x_initial; // Déplacement vertical
        int delta_y = y_final - y_initial; // Déplacement horizontal

        // Déterminer la direction selon la couleur du pion
        // Blancs avancent de -1 (vers le haut) et noirs avancent de +1 (vers le bas)
        int direction = this.get_proprietaire() == 1 ? -1 : 1;

        // Bloc 1 — Vérification du mouvement simple (une case tout droit)
        if (delta_x == direction && delta_y == 0 && echiquier[x_final][y_final] == null) {
            return true;
        }

        // Bloc 2 — Vérification du mouvement de capture (en diagonale, une case)
        if (delta_x == direction && Math.abs(delta_y) == 1 && echiquier[x_final][y_final] != null &&
                echiquier[x_final][y_final].get_proprietaire() != this.get_proprietaire()) {
            return true;
        }

        // Bloc 3 — Vérification du premier mouvement spécial (deux cases en avant)
        if ((x_initial == 6 && this.get_proprietaire() == 1 || x_initial == 1 && this.get_proprietaire() == 2) && // Ligne initiale
                delta_x == 2 * direction && delta_y == 0 && // Deux cases en avant
                echiquier[x_initial + direction][y_initial] == null && // Case intermédiaire vide
                echiquier[x_final][y_final] == null) { // Case finale vide
            return true;
        }

        // Si aucune des conditions n'est respectée, le mouvement est invalide
        return false;
    }
}