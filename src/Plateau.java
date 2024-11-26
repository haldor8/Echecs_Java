import java.io.ObjectOutputStream;

import javax.swing.*;
import java.awt.*;
import java.util.*;

// Se situe du côté client et gère l'envoi d'informations au serveur
public class Plateau extends JPanel {

    private Pieces[][] matrice;
    private int id_joueur;
    private ObjectOutputStream sortie;
    // private int style; // Si on souhaite faire plusieurs styles de plateau
    public static int nb_lignes = 8;
    public static int nb_colonnes = 8; // Plateau d'échecs = 8x8

    public Plateau(int le_client) {
        // Instancier tous les pions et leurs positions de base dans le constructeur
        this.matrice = new Pieces[nb_lignes][nb_colonnes];
        this.id_joueur = le_client;
        // Configuration du layout en grille 8x8
        this.setLayout(new GridLayout(nb_lignes, nb_colonnes));
        // Ajouter les cases au plateau
        // A1 blanc ( bas gauche), H8 (noir bas gauche), même plateau
        for (int i = 0; i < nb_lignes; i++) {
            for (int j = 0; j < nb_colonnes; j++) {
                JPanel casePanel = new JPanel();
                if ((i + j) % 2 == 0) {
                    casePanel.setBackground(Color.WHITE); // Case blanche

                    //casePanel.add(;
                } else {
                    casePanel.setBackground(Color.BLACK); // Case noire
                }
                this.add(casePanel); // Ajouter la case au plateau
            }
        }

        for (int i = 0; i < nb_colonnes; i++) {
            this.matrice[1][i] = new Pion(1, i, 1); // Placer les pions blancs
            JLabel label = new JLabel(matrice[1][i].get_icon());
            this.matrice[6][i] = new Pion(6, i, 2); // Placer les pions noirs
        }

        for (int i = 0; i < nb_colonnes; i += 7) {
            this.matrice[0][i] = new Tour(0, i, 1); // Placer les tours blanches
            this.matrice[7][i] = new Tour(7, i, 2); // Placer les tours noires
        }

        for (int i = 1; i < nb_colonnes; i += 5) {
            this.matrice[0][i] = new Cavalier(0, i, 1); // Placer les cavaliers blancs
            this.matrice[7][i] = new Cavalier(7, i, 2); // Placer les cavaliers noirs
        }

        for (int i = 2; i < nb_colonnes; i += 3) {
            this.matrice[0][i] = new Fou(0, i, 1); // Placer les fous blancs
            this.matrice[7][i] = new Fou(7, i, 2); // Placer les fous noirs
        }

        this.matrice[0][3] = new Reine(0, 4, 1); // Placer la reine blanche
        this.matrice[7][3] = new Reine(7, 4, 2); // Placer la reine noire

        this.matrice[0][4] = new Roi(0, 4, 1); // Placer la reine blanche
        this.matrice[7][4] = new Roi(7, 4, 2); // Placer la reine noire

        // Combler le plateau avec des cases vides ou des pions vides jsp
    }

    // Ne pas oublier d'inverser les coordonnées reçues par l'écouteur lorsque les
    // pions noirs jouent

    // Implémenter le 1 click highlight et le drag&drop

    public void set_out(ObjectOutputStream la_sortie){
        this.sortie = la_sortie;
    }
    public static int getNb_lignes() {
        return nb_lignes;
    }

    public static int getNb_colonnes() {
        return nb_colonnes;
    }
}
