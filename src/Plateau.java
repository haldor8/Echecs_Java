import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Plateau extends JPanel {
    private Pieces[][] matrice;
    private JPanel[][] cases_graphiques;
    private ObjectOutputStream sortie; // Utilisé pour envoyer les données au serveur
    private ObjectInputStream entree; // Utilisé pour recevoir les réponses du serveur
    private int id_joueur;
    public static int nb_lignes = 8;
    public static int nb_colonnes = 8;

    private int ligne_buffer = -1;
    private int colonne_buffer = -1;

    public Plateau(int le_client) {
        this.matrice = new Pieces[nb_lignes][nb_colonnes];
        this.cases_graphiques = new JPanel[nb_lignes][nb_colonnes];
        this.id_joueur = le_client;

        this.setLayout(new GridLayout(nb_lignes, nb_colonnes));

        for (int i = 0; i < nb_lignes; i++) {
            for (int j = 0; j < nb_colonnes; j++) {
                JPanel case_panel = new JPanel(new BorderLayout());
                cases_graphiques[i][j] = case_panel;

                // Définir la couleur des cases
                if ((i + j) % 2 == 0) {
                    case_panel.setBackground(new Color(238, 238, 210)); // clair
                } else {
                    case_panel.setBackground(new Color(118, 150, 86)); // foncé
                }

                // Associer un MouseListener pour capturer les clics
                int finalI = i;
                int finalJ = j;
                case_panel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        int adjustedI = ajusterCoordonneesPourJoueur(finalI, false);
                        int adjustedJ = ajusterCoordonneesPourJoueur(finalJ, true);

                        System.out.println("Case cliquée : i=" + adjustedI + ", j=" + adjustedJ);

                        if (ligne_buffer == -1 && colonne_buffer == -1) {
                            // Première sélection
                            Pieces piece_cliquee = matrice[adjustedI][adjustedJ];
                            if (piece_cliquee != null && piece_cliquee.get_proprietaire() == id_joueur) {
                                afficher_deplacements(adjustedI, adjustedJ, piece_cliquee);
                                ligne_buffer = adjustedI;
                                colonne_buffer = adjustedJ;
                            } else {
                                System.out.println("Aucune pièce valide sélectionnée ou pièce adverse.");
                            }
                        } else {
                            // Deuxième sélection (déplacement)
                            if (matrice[ligne_buffer][colonne_buffer] != null &&
                                    matrice[ligne_buffer][colonne_buffer].deplacement_valide(
                                            ligne_buffer, colonne_buffer, adjustedI, adjustedJ, matrice)) {
                                effectuer_deplacement(ligne_buffer, colonne_buffer, adjustedI, adjustedJ);
                            } else {
                                System.out.println("Déplacement invalide.");
                            }
                            clear_buffer();
                        }
                    }
                });
                


                // Ajouter la case au GridLayout
                this.add(case_panel);
            }
        }

        // Initialiser les pièces
        initialiser_pieces();
        // Mettre à jour l'affichage graphique
        mettre_a_jour_affichage();
    }

    // Méthode pour ajuster les coordonnées selon le joueur
    private int ajusterCoordonneesPourJoueur(int coord, boolean estColonne) {
        if (id_joueur == 2) {
            return estColonne ? nb_colonnes - 1 - coord : nb_lignes - 1 - coord;
        }
        return coord;
    }

    private void initialiser_pieces() {
        for (int i = 0; i < nb_colonnes; i++) {
            matrice[1][i] = new Pion(2);
            matrice[6][i] = new Pion(1);
        }

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

    public void afficher_deplacements(int x, int y, Pieces la_piece) {
        if (la_piece == null || la_piece.get_proprietaire() != id_joueur) {
            System.out.println("Aucune pièce valide sélectionnée ou pièce adverse.");
            return;
        }

        // Réinitialiser les couleurs des cases
        for (int i = 0; i < nb_lignes; i++) {
            for (int j = 0; j < nb_colonnes; j++) {
                JPanel case_panel = cases_graphiques[i][j];
                if ((i + j) % 2 == 0) {
                    case_panel.setBackground(new Color(238, 238, 210)); // clair
                } else {
                    case_panel.setBackground(new Color(118, 150, 86)); // foncé
                }
            }
        }

        ArrayList<int[]> deplacements_possibles = new ArrayList<>();
        for (int i = 0; i < nb_lignes; i++) {
            for (int j = 0; j < nb_colonnes; j++) {
                int adjustedI = ajusterCoordonneesPourJoueur(i, false);
                int adjustedJ = ajusterCoordonneesPourJoueur(j, true);

                if (la_piece.deplacement_valide(x, y, adjustedI, adjustedJ, matrice)) {
                    if (matrice[adjustedI][adjustedJ] != null &&
                            matrice[adjustedI][adjustedJ].get_proprietaire() == la_piece.get_proprietaire()) {
                        continue;
                    }
                    deplacements_possibles.add(new int[]{adjustedI, adjustedJ});
                }
            }
        }

        // Afficher les cases où la pièce peut se déplacer
        for (int[] deplacement : deplacements_possibles) {
            int displayI = ajusterCoordonneesPourJoueur(deplacement[0], false);
            int displayJ = ajusterCoordonneesPourJoueur(deplacement[1], true);
            if(matrice[displayI][displayJ]!=null && matrice[displayI][displayJ].get_proprietaire() != la_piece.get_proprietaire()){
                JPanel case_panel = cases_graphiques[displayI][displayJ];
                case_panel.setBackground(new Color(233, 86, 20)); // rouge
            }
            else {
                JPanel case_panel = cases_graphiques[displayI][displayJ];
                case_panel.setBackground(new Color(64, 184, 232)); // bleu
            }
        }

        mettre_a_jour_affichage(); // Mettre à jour l'affichage du plateau
    }

    private void effectuer_deplacement(int x1, int y1, int x2, int y2) {

        set_in(entree);
        set_out(sortie);

        Pieces piece = matrice[x1][y1];
        matrice[x1][y1] = null;
        matrice[x2][y2] = piece;

        // Envoi du mouvement au serveur
        // Effectuer les déplacements en ajustant les indices pour le joueur 2
        if (id_joueur == 2) {
            x1 = nb_lignes - 1 - x1;
            y1 = nb_colonnes - 1 - y1;
            x2 = nb_lignes - 1 - x2;
            y2 = nb_colonnes - 1 - y2;
        }
        envoyer_deplacement_au_serveur(x1, y1, x2, y2);
        mettre_a_jour_affichage(); // Mettre à jour le plateau après le déplacement
        recevoirDeplacementDuServeur();

    }

    private void envoyer_deplacement_au_serveur(int x1, int y1, int x2, int y2) {
        if (sortie != null) {
            try {
                // Envoyer le mouvement sous forme de tableau d'entiers
                int[] deplacement = {x1, y1, x2, y2};
                sortie.writeObject(deplacement);
                sortie.flush();
                System.out.println("Déplacement envoyé au serveur : " + Arrays.toString(deplacement));
            } catch (Exception e) {
                System.err.println("Erreur lors de l'envoi du déplacement au serveur : " + e.getMessage());
            }
        }
    }

    // Le client 2 reçoit un déplacement du serveur
    public void recevoirDeplacementDuServeur() {
        try {
            // Réception du déplacement envoyé par le serveur
            int[] deplacement = (int[]) entree.readObject();  // Utilise le ObjectInputStream
            int x1 = deplacement[0];
            int y1 = deplacement[1];
            int x2 = deplacement[2];
            int y2 = deplacement[3];
            System.out.println("OUIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII recu"+x1);

            // Appliquer ce déplacement sur le plateau du client
            Pieces piece = matrice[x1][y1];  // Récupère la pièce à déplacer
            matrice[x1][y1] = null;          // Supprime la pièce de la case d'origine
            matrice[x2][y2] = piece;         // Place la pièce sur la nouvelle case
            mettre_a_jour_affichage();        // Met à jour l'affichage du plateau
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }


    private void mettre_a_jour_affichage() {
        for (int i = 0; i < nb_lignes; i++) {
            for (int j = 0; j < nb_colonnes; j++) {
                JPanel case_panel = cases_graphiques[i][j];
                case_panel.removeAll();

                int adjustedI = ajusterCoordonneesPourJoueur(i, false);
                int adjustedJ = ajusterCoordonneesPourJoueur(j, true);

                Pieces piece = matrice[adjustedI][adjustedJ];
                if (piece != null && piece.get_icon() != null) {
                    JLabel label = new JLabel(piece.get_icon());
                    case_panel.add(label, BorderLayout.CENTER);
                }

                case_panel.revalidate();
                case_panel.repaint();
            }
        }
    }

    private void clear_buffer() {
        this.ligne_buffer = -1;
        this.colonne_buffer = -1;
    }

    public void set_out(ObjectOutputStream la_sortie) {
        this.sortie = la_sortie;
    }

    public void set_in(ObjectInputStream l_entree) {
        this.entree = l_entree;
    }

    public static int getNb_lignes() {
        return nb_lignes;
    }

    public static int getNb_colonnes() {
        return nb_colonnes;
    }
}
