import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
                        int adjustedI = id_joueur == 2 ? nb_lignes - 1 - finalI : finalI;
                        int adjustedJ = id_joueur == 2 ? nb_colonnes - 1 - finalJ : finalJ;

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
                    //@Override
                    /*public void mouseReleased(MouseEvent e) {
                        // Réinitialiser les couleurs des cases après le clic
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
                        clear_buffer();
                        System.out.println("Relâchement terminé.");
                    }*/
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
        System.out.println("JEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE REINITIALIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIISEEEEEEE");
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
                int adjustedI = id_joueur == 2 ? nb_lignes - 1 - i : i;
                int adjustedJ = id_joueur == 2 ? nb_colonnes - 1 - j : j;

                if (la_piece.deplacement_valide(x, y, adjustedI, adjustedJ, matrice)) {
                    if (matrice[adjustedI][adjustedJ] != null &&
                            matrice[adjustedI][adjustedJ].get_proprietaire() == la_piece.get_proprietaire()) {
                        continue;
                    }
                    deplacements_possibles.add(new int[]{adjustedI, adjustedJ});
                }
            }
        }

        for (int[] deplacement : deplacements_possibles) {
            int displayI = id_joueur == 2 ? nb_lignes - 1 - deplacement[0] : deplacement[0];
            int displayJ = id_joueur == 2 ? nb_colonnes - 1 - deplacement[1] : deplacement[1];
            JPanel case_panel = cases_graphiques[displayI][displayJ];
            case_panel.setBackground(new Color(64, 184, 232)); // bleu
        }

        mettre_a_jour_affichage();
    }

    private void effectuer_deplacement(int x1, int y1, int x2, int y2) {
        if (id_joueur == 2) {
            x1 = nb_lignes - 1 - x1;
            y1 = nb_colonnes - 1 - y1;
            x2 = nb_lignes - 1 - x2;
            y2 = nb_colonnes - 1 - y2;
        }

        Pieces piece = matrice[x1][y1];
        matrice[x1][y1] = null;
        matrice[x2][y2] = piece;

        envoyer_deplacement_au_serveur(x1, y1, x2, y2);
        mettre_a_jour_affichage();
    }

    private void envoyer_deplacement_au_serveur(int x1, int y1, int x2, int y2) {
        if (sortie != null) {
            try {
                int[] deplacement = {x1, y1, x2, y2};
                sortie.writeObject(deplacement);
                sortie.flush();
                System.out.println("Déplacement envoyé au serveur : " + Arrays.toString(deplacement));
            } catch (Exception e) {
                System.err.println("Erreur lors de l'envoi du déplacement au serveur : " + e.getMessage());
            }
        }
    }

    public void recevoir_deplacement_du_serveur(int x1, int y1, int x2, int y2) {
        Pieces piece = matrice[x1][y1];
        matrice[x1][y1] = null;
        matrice[x2][y2] = piece;
        mettre_a_jour_affichage();
    }


    private void mettre_a_jour_affichage() {
        for (int i = 0; i < nb_lignes; i++) {
            for (int j = 0; j < nb_colonnes; j++) {
                JPanel case_panel = cases_graphiques[i][j];
                case_panel.removeAll();

                int adjustedI = id_joueur == 2 ? nb_lignes - 1 - i : i;
                int adjustedJ = id_joueur == 2 ? nb_colonnes - 1 - j : j;

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
