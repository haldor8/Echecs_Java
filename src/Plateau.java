import java.io.ObjectOutputStream;

import javax.swing.*;
import java.awt.*;
import java.util.*;

// Se situe du côté client et gère l'envoi d'informations au serveur
public class Plateau extends JPanel {

    private Pieces[][] matrice;
    private Color client;
    private ObjectOutputStream sortie;
    // private int style; // Si on souhaite faire plusieurs styles de plateau
    private int nb_lignes, nb_colonnes = 8; // Plateau d'échecs = 8x8

    public Plateau(Color le_client) {
        // Instancier tous les pions et leurs positions de base dans le constructeur
        this.matrice = new Pieces[nb_lignes][nb_colonnes];
        this.client = le_client;

        for (int i = 0; i < nb_colonnes; i++) {
            this.matrice[1][i] = new Pion(); // Placer les pions blancs
            this.matrice[6][i] = new Pion(); // Placer les pions noirs
        }

        for (int i = 0; i < nb_colonnes; i += 7) {
            this.matrice[0][i] = new Tour(); // Placer les tours blanches
            this.matrice[7][i] = new Tour(); // Placer les tours noires
        }

        for (int i = 1; i < nb_colonnes; i += 5) {
            this.matrice[0][i] = new Cavalier(); // Placer les cavaliers blancs
            this.matrice[7][i] = new Cavalier(); // Placer les cavaliers noirs
        }

        for (int i = 2; i < nb_colonnes; i += 3) {
            this.matrice[0][i] = new Fou(); // Placer les fous blancs
            this.matrice[7][i] = new Fou(); // Placer les fous noirs
        }

        this.matrice[0][3] = new Reine(); // Placer la reine blanche
        this.matrice[7][3] = new Reine(); // Placer la reine noire

        this.matrice[0][4] = new Roi(); // Placer la reine blanche
        this.matrice[7][4] = new Roi(); // Placer la reine noire

        // Combler le plateau avec des cases vides ou des pions vides jsp
    }

    // Ne pas oublier d'inverser les coordonnées reçues par l'écouteur lorsque les
    // pions noirs jouent

    // Implémenter le 1 click highlight et le drag&drop

    public void set_out(ObjectOutputStream la_sortie){
        this.sortie = la_sortie;
    }
}
