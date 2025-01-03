import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Plateau extends JPanel {
    private Pieces[][] matrice;
    private JPanel[][] cases_graphiques;
    private ObjectOutputStream sortie;
    private int id_joueur;
    public static int nb_lignes = 8;
    public static int nb_colonnes = 8;

    private static int DISTANCE = 50;

    private int ligne_buffer = -1;
    private int colonne_buffer = -1;

    public Plateau(int le_client) {
        this.matrice = new Pieces[nb_lignes][nb_colonnes];
        this.cases_graphiques = new JPanel[nb_lignes][nb_colonnes];
        this.id_joueur = le_client;

        this.setLayout(new GridLayout(nb_lignes, nb_colonnes));

        // Initialiser le plateau graphique
        for (int i = 0; i < nb_lignes; i++) {
            for (int j = 0; j < nb_colonnes; j++) {
                JPanel case_panel = new JPanel(new BorderLayout());
                cases_graphiques[i][j] = case_panel;

                if ((i + j) % 2 == 0) {
                    case_panel.setBackground(new Color(238, 238, 210));
                } else {
                    case_panel.setBackground(new Color(118, 150, 86));
                }

                this.add(case_panel);
            }
        }

        // Initialiser les pièces
        initialiser_pieces();

        // Mettre à jour l'affichage graphique
        mettre_a_jour_affichage();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Récupérer les coordonnées de la case cliquée
                int ligne = (int) (e.getY() / (getSize().getHeight() / 8)); // Ligne
                int colonne = (int) (e.getX() / (getSize().getWidth() / 8)); // Colonne
                System.out.println("Appuyé en : Ligne = " + ligne + ", Colonne = " + colonne);

                // Si aucune pièce n'est encore sélectionnée
                if (ligne_buffer == -1) {
                    if (matrice[ligne][colonne] != null) {
                        // Vérifie si la pièce appartient au joueur actif
                        if (matrice[ligne][colonne].get_proprietaire() == id_joueur) {
                            maj_buffer(ligne, colonne); // Stocke la sélection courante
                            System.out.println("Pièce sélectionnée : " + matrice[ligne][colonne].getClass().getSimpleName());
                        }
                    }
                } else {
                    System.out.println("Une pièce est déjà sélectionnée !");
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Récupérer les coordonnées de la case cible
                int ligne = (int) (e.getY() / (getSize().getHeight() / 8)); // Ligne
                int colonne = (int) (e.getX() / (getSize().getWidth() / 8)); // Colonne
                System.out.println("Relâché en : Ligne = " + ligne + ", Colonne = " + colonne);

                // Si une pièce a été sélectionnée
                if (ligne_buffer != -1 && colonne_buffer != -1) {
                    Pieces piece = matrice[ligne_buffer][colonne_buffer]; // La pièce sélectionnée

                    // Vérifier que la pièce n'est pas null
                    if (piece != null) {
                        System.out.println("Tentative de déplacement de " + piece.getClass().getSimpleName() +
                                " de [" + ligne_buffer + ", " + colonne_buffer + "] vers [" + ligne + ", " + colonne + "]");

                        // Appeler `deplacement_valide` propre à la pièce
                        if (piece.deplacement_valide(ligne_buffer, colonne_buffer, ligne, colonne, matrice)) {
                            System.out.println("Déplacement valide ! Mise à jour de la matrice.");

                            // Actualiser la matrice pour bouger la pièce
                            matrice[ligne][colonne] = piece; // Positionner la pièce à la nouvelle case
                            matrice[ligne_buffer][colonne_buffer] = null; // Vider l'ancienne case

                            // Mettre à jour l'affichage du plateau
                            mettre_a_jour_affichage();

                            // Exemple : Envoyer le mouvement au serveur
                            try {
                                int[] mouvement = {ligne_buffer, colonne_buffer, ligne, colonne}; // Tableau du mouvement
                                sortie.writeObject(mouvement); // Envoi au serveur
                                sortie.flush();
                                System.out.println("Déplacement synchronisé avec le serveur.");
                            } catch (IOException ex) {
                                System.out.println("Erreur d'envoi au serveur : " + ex.getMessage());
                            }
                        } else {
                            System.out.println("Déplacement invalide !");
                        }
                    } else {
                        System.out.println("Aucune pièce sélectionnée.");
                    }

                    // Réinitialiser le buffer
                    clear_buffer();
                }
            }
        });
    }

    private void initialiser_pieces() {
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
            matrice[0][6] = new Cavalier(1);
            matrice[7][1] = new Cavalier(2);
            matrice[7][6] = new Cavalier(2);

            matrice[0][2] = new Fou(1);
            matrice[0][5] = new Fou(1);
            matrice[7][2] = new Fou(2);
            matrice[7][5] = new Fou(2);

            matrice[0][3] = new Reine(1);
            matrice[0][4] = new Roi(1);
            matrice[7][3] = new Reine(2);
            matrice[7][4] = new Roi(2);
        } else {
            for (int i = 0; i < nb_colonnes; i++) {
                matrice[1][i] = new Pion(2);
                matrice[6][i] = new Pion(1);
            }

            // Ajouter les autres pièces
            matrice[0][0] = new Tour(2);
            matrice[0][7] = new Tour(2);
            matrice[7][0] = new Tour(1);
            matrice[7][7] = new Tour(1);

            matrice[0][1] = new Cavalier(2);
            matrice[0][6] = new Cavalier(2);
            matrice[7][1] = new Cavalier(1);
            matrice[7][6] = new Cavalier(1);

            matrice[0][2] = new Fou(2);
            matrice[0][5] = new Fou(2);
            matrice[7][2] = new Fou(1);
            matrice[7][5] = new Fou(1);

            matrice[0][3] = new Reine(2);
            matrice[0][4] = new Roi(2);
            matrice[7][3] = new Reine(1);
            matrice[7][4] = new Roi(1);
        }
    }

    private void mettre_a_jour_affichage() {
        for (int i = 0; i < nb_lignes; i++) {
            for (int j = 0; j < nb_colonnes; j++) {
                JPanel case_panel = cases_graphiques[i][j];
                case_panel.removeAll();

                Pieces piece = matrice[i][j];
                if (piece != null && piece.get_icon() != null) {
                    JLabel label = new JLabel(piece.get_icon());
                    case_panel.add(label, BorderLayout.CENTER);
                }

                case_panel.revalidate();
                case_panel.repaint();
            }
        }
    }

    public void set_out(ObjectOutputStream la_sortie) {
        this.sortie = la_sortie;
    }

    public static int getNb_lignes() {
        return nb_lignes;
    }

    public static int getNb_colonnes() {
        return nb_colonnes;
    }

    public void afficher_deplacements(int x, int y, Pieces la_piece){

    }

    private void maj_buffer(int x, int y){
        this.ligne_buffer = x;
        this.colonne_buffer = y;
    }

    private void clear_buffer(){
        this.ligne_buffer = -1;
        this.colonne_buffer = -1;
    }
}