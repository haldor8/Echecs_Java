import java.io.ObjectOutputStream;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Plateau extends JPanel {
    private Pieces[][] matrice;
    private JPanel[][] casesGraphiques;
    private ObjectOutputStream sortie;
    private int id_joueur;
    public static int nb_lignes = 8;
    public static int nb_colonnes = 8;

    public Plateau(int le_client) {
        this.matrice = new Pieces[nb_lignes][nb_colonnes];
        this.casesGraphiques = new JPanel[nb_lignes][nb_colonnes];
        this.id_joueur = le_client;

        this.setLayout(new GridLayout(nb_lignes, nb_colonnes));

        // Initialiser le plateau graphique
        for (int i = 0; i < nb_lignes; i++) {
            for (int j = 0; j < nb_colonnes; j++) {
                JPanel casePanel = new JPanel(new BorderLayout());
                casesGraphiques[i][j] = casePanel;

                if ((i + j) % 2 == 0) {
                    casePanel.setBackground(new Color(238,238,210));
                } else {
                    casePanel.setBackground(new Color(118,150,86));
                }

                this.add(casePanel);
            }
        }

        // Initialiser les pièces
        initialiserPieces();

        // Mettre à jour l'affichage graphique
        mettreAJourAffichage();

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
    }

    private void initialiserPieces() {
        // Ajouter les pions
        if (this.id_joueur == 2) {
            for (int i = 0; i < nb_colonnes; i++) {
                matrice[1][i] = new Pion(1);
                matrice[6][i] = new Pion(2);
            }

            // Ajouter les autres pièces
            matrice[0][0] = new Tour(1);
            matrice[0][7] = new Tour(1);
            matrice[7][0] = new Tour(2);
            matrice[7][7] = new Tour(2);

            matrice[0][1] = new Cavalier(1);
            matrice[0][6] = new Cavalier( 1);
            matrice[7][1] = new Cavalier( 2);
            matrice[7][6] = new Cavalier(2);

            matrice[0][2] = new Fou(1);
            matrice[0][5] = new Fou(1);
            matrice[7][2] = new Fou(2);
            matrice[7][5] = new Fou( 2);

            matrice[0][3] = new Reine( 1);
            matrice[0][4] = new Roi( 1);
            matrice[7][3] = new Reine( 2);
            matrice[7][4] = new Roi( 2);
        }
    else {
            for (int i = 0; i < nb_colonnes; i++) {
                matrice[1][i] = new Pion(2);
                matrice[6][i] = new Pion( 1);
            }

            // Ajouter les autres pièces
            matrice[0][0] = new Tour( 2);
            matrice[0][7] = new Tour( 2);
            matrice[7][0] = new Tour( 1);
            matrice[7][7] = new Tour( 1);

            matrice[0][1] = new Cavalier( 2);
            matrice[0][6] = new Cavalier( 2);
            matrice[7][1] = new Cavalier(1);
            matrice[7][6] = new Cavalier( 1);

            matrice[0][2] = new Fou( 2);
            matrice[0][5] = new Fou( 2);
            matrice[7][2] = new Fou( 1);
            matrice[7][5] = new Fou( 1);

            matrice[0][3] = new Reine( 2);
            matrice[0][4] = new Roi( 2);
            matrice[7][3] = new Reine( 1);
            matrice[7][4] = new Roi( 1);
    }
    }

    private void mettreAJourAffichage() {
        for (int i = 0; i < nb_lignes; i++) {
            for (int j = 0; j < nb_colonnes; j++) {
                JPanel casePanel = casesGraphiques[i][j];
                casePanel.removeAll();

                Pieces piece = matrice[i][j];
                if (piece != null && piece.get_icon() != null) {
                    JLabel label = new JLabel(piece.get_icon());
                    casePanel.add(label, BorderLayout.CENTER);
                }

                casePanel.revalidate();
                casePanel.repaint();
            }
        }
    }

    public void set_out(ObjectOutputStream la_sortie){
        this.sortie = la_sortie;
    }
    public static int getNb_lignes() {
        return nb_lignes;
    }

    public static int getNb_colonnes() {
        return nb_colonnes;
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