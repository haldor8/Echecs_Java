import org.apache.batik.apps.svgbrowser.NodePickerPanel;

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
                            if(tester_deplacement(adjustedI, adjustedJ)){
                                envoyer_deplacement_au_serveur(ligne_buffer, colonne_buffer, adjustedI, adjustedJ);
                            }else{
                                System.out.println("Deplacement non envoye au serveur");
                            }

                            clear_buffer();
                            reinitialiserCouleursCases();
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

        reinitialiserCouleursCases();

        ArrayList<int[]> deplacements_possibles = la_piece.deplacements_possibles(x, y, matrice);

        if(deplacements_possibles != null){ // Si il y a des déplacements possibles
            // Afficher les cases où la pièce peut se déplacer
            for (int[] deplacement : deplacements_possibles) {
                int displayI = ajusterCoordonneesPourJoueur(deplacement[0], false);
                int displayJ = ajusterCoordonneesPourJoueur(deplacement[1], true);
                if(matrice[displayI][displayJ] != null && matrice[displayI][displayJ].get_proprietaire() != la_piece.get_proprietaire()){
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
    }

    public void effectuer_deplacement(Deplacement dpl) {
        int[] coordonnees = dpl.get_deplacement();
        int x1 = coordonnees[0], y1 = coordonnees[1], x2 = coordonnees[2], y2 = coordonnees[3];
        Pieces piece = matrice[x1][y1];
        matrice[x1][y1] = null;
        matrice[x2][y2] = piece;

        // envoyer_deplacement_au_serveur(x1, y1, x2, y2);
        mettre_a_jour_affichage(); // Mettre à jour le plateau après le déplacement
        reinitialiserCouleursCases();
        // recevoirDeplacementDuServeur();

    }

    private void envoyer_deplacement_au_serveur(int x1, int y1, int x2, int y2) {
        // Envoyer le mouvement sous forme de tableau d'entiers
        Deplacement deplacement = new Deplacement(x1, y1, x2, y2);
        try {
            sortie.writeObject(deplacement);
            System.out.println("Déplacement envoyé au serveur : " + deplacement.toString());
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi du déplacement au serveur : " + e.getMessage());
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

    // Méthode pour réinitialiser les couleurs des cases à leurs valeurs par défaut
    private void reinitialiserCouleursCases() {
        for (int i = 0; i < nb_lignes; i++) {
            for (int j = 0; j < nb_colonnes; j++) {
                JPanel case_panel = cases_graphiques[i][j];
                // Réinitialiser la couleur des cases
                if ((i + j) % 2 == 0) {
                    case_panel.setBackground(new Color(238, 238, 210)); // clair
                } else {
                    case_panel.setBackground(new Color(118, 150, 86)); // foncé
                }
            }
        }
    }

    private boolean tester_deplacement(int x_dest, int y_dest){
        Pieces piece_actuelle = matrice[ligne_buffer][colonne_buffer];
        Roi roi_du_joueur = trouver_roi();

        // Si le roi est en échec
        if(roi_du_joueur.est_attaque(ligne_buffer, colonne_buffer, matrice)){
            // Si le déplacement sort le roi de l'échec (le roi n'est plus en échec)
            System.out.println("Le roi est en echec");
            if(!roi_du_joueur.est_attaque(ligne_buffer, colonne_buffer, pre_deplacer(x_dest, y_dest))){
                if (piece_actuelle != null){
                    if(piece_actuelle.deplacement_valide(ligne_buffer, colonne_buffer, x_dest, y_dest, matrice)) {
                        return true;
                    }
                } else {
                    System.out.println("Déplacement invalide.");
                }
            }else{
                System.out.println("Le roi est toujours en echec");
            }
            // Si le déplacement ne met pas le roi en échec
        } else {
            if (!roi_du_joueur.est_attaque(ligne_buffer, colonne_buffer, pre_deplacer(x_dest, y_dest))) {
                if (piece_actuelle != null) {
                    if (piece_actuelle.deplacement_valide(ligne_buffer, colonne_buffer, x_dest, y_dest, matrice)) {
                        return true;
                    }
                }
            }else{
                System.out.println("Le deplacement met le roi en echec.");
            }
        }

        return false;
    }

    private Roi trouver_roi(){
        for(int colonne = 0; colonne < 8; colonne++){
            for(int ligne = 0; ligne < 8; ligne++){
                if(matrice[ligne][colonne] != null) {
                    if (matrice[ligne][colonne] instanceof Roi && matrice[ligne][colonne].get_proprietaire() == this.id_joueur) {
                        return (Roi)matrice[ligne][colonne];
                    }
                }
            }
        }
        return null; // Si on trouve rien, alors on retourne rien
    }

    private Pieces[][] pre_deplacer(int x_depl, int y_depl){
        // Deepcopy du plateau
        Pieces[][] matrice_temp = deepcopy_plateau();
        Pieces piece = matrice_temp[ligne_buffer][colonne_buffer];
        matrice_temp[ligne_buffer][colonne_buffer] = null;
        matrice_temp[x_depl][y_depl] = piece;
        return matrice_temp;
    }

    private Pieces[][] deepcopy_plateau(){
        Pieces[][] nouv_matrice = new Pieces[8][8];
        for(int colonne = 0; colonne < 8; colonne++){
            for(int ligne = 0; ligne < 8; ligne++){
                nouv_matrice[ligne][colonne] = matrice[ligne][colonne];
            }
        }
        return nouv_matrice;
    }

    public Pieces[][] getMatrice(){
        return this.matrice;
    }
}
