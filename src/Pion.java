import java.util.ArrayList;

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

        Pieces piece_source = matrice[x][y];
        Pieces piece_dest = matrice[x_final][y_final];
        int direction; // Pion vers le haut (1) ou vers le bas (2)
        if(this.get_proprietaire() == 1){
            direction = -1;
        }else{
            direction = 1;
        }
        if (x_final == x + direction && y_final == y) {
            // Déplacement simple d'une case
            return matrice[x_final][y_final] == null; // Case vide
        } else if (x_final == x + (2 * direction) && y_final == y && x == (piece_source.get_proprietaire() == 1 ? 6 : 1)) {
            // Déplacement de deux cases (si c'est le premier coup)
            return matrice[x_final][y_final] == null; // Case vide
        }
        // Cas pour les captures (diagonales)
        if (Math.abs(x_final - x) == 1 && Math.abs(y_final - y) == 1 && matrice[x_final][y_final] != null && matrice[x_final][y_final].get_proprietaire() != this.get_proprietaire()) {
            return true; // Capture valide
        }
        return false;
    }

    public ArrayList<int[]> deplacements_possibles(int x, int y, Pieces[][] echiquier) {
        ArrayList<int[]> les_deplacements = new ArrayList<>();
        int direction = (this.get_proprietaire() == 1) ? -1 : 1; // -1 pour blanc, 1 pour noir

        // Déplacement simple vers l'avant
        if (isInBounds(x + direction, y) && echiquier[x + direction][y] == null) {
            les_deplacements.add(new int[]{x + direction, y});

            // Déplacement double si le pion est sur sa position de départ
            if ((this.get_proprietaire() == 1 && x == 6) || (this.get_proprietaire() == 2 && x == 1)) {
                if (echiquier[x + 2 * direction][y] == null) {
                    les_deplacements.add(new int[]{x + 2 * direction, y});
                }
            }
        }

        // Capture à droite
        if (isInBounds(x + direction, y + 1)) {
            Pieces la_case = echiquier[x + direction][y + 1];
            if (la_case != null && la_case.get_proprietaire() != this.get_proprietaire()) {
                les_deplacements.add(new int[]{x + direction, y + 1});
            }
        }

        // Capture à gauche
        if (isInBounds(x + direction, y - 1)) {
            Pieces la_case = echiquier[x + direction][y - 1];
            if (la_case != null && la_case.get_proprietaire() != this.get_proprietaire()) {
                les_deplacements.add(new int[]{x + direction, y - 1});
            }
        }

        return les_deplacements;
    }

    // Méthode utilitaire pour vérifier si une position est dans les limites de l'échiquier
    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }
}