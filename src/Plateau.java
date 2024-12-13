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
    public static int nb_lignes = 8, nb_colonnes = 8; // Plateau d'échecs = 8x8

    public Plateau(int le_client) {
        // Instancier tous les pions et leurs positions de base dans le constructeur
        this.matrice = new Pieces[nb_lignes][nb_colonnes];
        this.id_joueur = le_client;

        for (int i = 0; i < nb_colonnes; i++) {
            this.matrice[1][i] = new Pion(1, i, 0); // Placer les pions blancs
            this.matrice[6][i] = new Pion(6, i, 1); // Placer les pions noirs
        }

        for (int i = 0; i < nb_colonnes; i += 7) {
            this.matrice[0][i] = new Tour(0, i, 0); // Placer les tours blanches
            this.matrice[7][i] = new Tour(7, i, 1); // Placer les tours noires
        }

        for (int i = 1; i < nb_colonnes; i += 5) {
            this.matrice[0][i] = new Cavalier(0, i, 0); // Placer les cavaliers blancs
            this.matrice[7][i] = new Cavalier(7, i, 1); // Placer les cavaliers noirs
        }

        for (int i = 2; i < nb_colonnes; i += 3) {
            this.matrice[0][i] = new Fou(0, i, 0); // Placer les fous blancs
            this.matrice[7][i] = new Fou(7, i, 1); // Placer les fous noirs
        }

        this.matrice[0][3] = new Reine(0, 4, 0); // Placer la reine blanche
        this.matrice[7][3] = new Reine(7, 4, 1); // Placer la reine noire

        this.matrice[0][4] = new Roi(0, 4, 0); // Placer la reine blanche
        this.matrice[7][4] = new Roi(7, 4, 1); // Placer la reine noire

        // Combler le plateau avec des cases vides ou des pions vides jsp

        this.addMouseListener(new MouseAdapter()) {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getY() / 60;
                int y = e.getX() / 60;
    
                if (x >= 0 && x < nb_lignes && y >= 0 && y < nb_colonnes) {
                    if (matrice[x][y] != null && matrice[x][y].get_proprietaire() == id_joueur) {
                        pieceSelectionnee = matrice[x][y]; // Sélectionner la pièce
                    } else {
                        pieceSelectionnee = null; // Désélectionner
                    }
                    repaint(); // Redessiner le plateau
                }
            }
    }



    // Ne pas oublier d'inverser les coordonnées reçues par l'écouteur lorsque les
    // pions noirs jouent

    // Implémenter le 1 click highlight et le drag&drop

    public void set_out(ObjectOutputStream la_sortie){
        this.sortie = la_sortie;
    }

    @Override 
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if (pieceSelectionnee != null) {
            List<Point> mouvements = pieceSelectionnee.mouvements_possibles(matrice);
            g.setColor(new Color(0, 255, 0, 128)); // Couleur semi-transparente pour surbrillance
            for (Point p : mouvements) {
                g.fillRect(p.y * 60, p.x * 60, 60, 60);
            }
        }
    }


}
