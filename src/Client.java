import java.io.*;
import java.net.*;

import javax.swing.*;
import java.awt.*;

// Interface utilisateur
public class Client extends JFrame {
    // Ne pas oublier d'inverser le plateau visuellement lorsque les pions noirs
    // jouent
    private ObjectOutputStream id_stream_output;
    private Interface_client interface_c;

    private int id_joueur;
    private Plateau le_plateau;

    private JPanel couleur;


    public Client(int x) throws HeadlessException{
        super("Joueur "+x);
        id_joueur = x;

        this.setLayout(new BorderLayout());

        couleur = new JPanel();

        Color la_couleur = null;
        try{
            if(id_joueur == 1){ // Joueur 1 = blanc
                la_couleur = Color.GRAY; // Gris pour une question de contraste, peut-être modifié
            }else if (id_joueur == 2){
                la_couleur = Color.BLACK;
            }else{
                throw new Exception("Id joueur inconnu");
            }
        }catch(Exception exep){
            System.out.println(exep);
            System.exit(-1);
        }
        
        couleur.setBackground(la_couleur);
        le_plateau = new Plateau(id_joueur);

        //// On ajoute la couleur du joueur en bas (possibilité d'ajouter un chronomètre dessus)
        this.add(couleur, BorderLayout.SOUTH);
        JPanel rightLabels = createRowLabels();
        this.add(rightLabels, BorderLayout.WEST);
        JPanel topLabels = createColumnLabels();
        this.add(topLabels, BorderLayout.NORTH);
        // On ajoute le plateau à l'UI
        this.add(le_plateau, BorderLayout.CENTER);




        this.setLocation((x - 1) * 400, 400);
        this.setSize(400, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        // On lance le socket du client
        try {
            // On démarre le socket avec l'IP locale et le port 49512 arbitraire
            InetAddress ip = InetAddress.getLocalHost();
            System.out.println(ip);
            Socket soc = new Socket(ip, 49512);

            // On ajoute l'interface au plateau pour qu'ils soient liés
            id_stream_output = new ObjectOutputStream(soc.getOutputStream());
            le_plateau.set_out(id_stream_output);
            interface_c = new Interface_client(soc, x, le_plateau);
            interface_c.start();
        } catch (UnknownHostException uhe) {
            System.out.println("Souci de hostName  chez le client !!");
        } catch (IOException uhe) {
            System.out.println("Souci de connexion chez le client !!");
        }
    }

    private JPanel createColumnLabels() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, le_plateau.getNb_lignes()));
        if(this.id_joueur== 1) {
            for (int i = 0; i < le_plateau.getNb_colonnes(); i++) {
                JLabel label = new JLabel(String.valueOf((char) ('a' + i)), SwingConstants.CENTER);
                panel.add(label);
            }
        }
        else {
            for (int i = 0; i < le_plateau.getNb_colonnes(); i++) {
                JLabel label = new JLabel(String.valueOf((char) ('h' - i)), SwingConstants.CENTER);
                panel.add(label);
            }
        }

        return panel;
    }

    private JPanel createRowLabels() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout( le_plateau.getNb_lignes(), 1));
        if (this.id_joueur == 2) {
            for (int i = 1; i < 9; i++) {
                JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
                panel.add(label);
            }
        }
        else{
                for (int i = le_plateau.getNb_lignes(); i > 0; i--) {
                    JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
                    System.out.println(label);
                    panel.add(label);
                }
            }
        return panel;
    }



    public int get_id_joueur(){
        return this.id_joueur;
    }
}