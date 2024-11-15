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
        super();
        id_joueur = x;

        this.setLayout(new BorderLayout());

        couleur = new JPanel();

        Color la_couleur;
        if(id_joueur == 0){ // Joueur 0 = blanc
            la_couleur = Color.GRAY; // Gris pour une question de contraste, peut-être modifié
        }else{
            la_couleur = Color.BLACK;
        }
        couleur.setBackground(la_couleur);
        le_plateau = new Plateau(la_couleur);

        // On ajoute le plateau à l'UI
        this.add(le_plateau, BorderLayout.CENTER);

        // On ajoute la couleur du joueur en bas (possibilité d'ajouter un chronomètre dessus)
        this.add(couleur, BorderLayout.SOUTH);

        this.setLocation((x - 1) * 400, 400);
        this.setSize(400, 400);
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



    public int get_id_joueur(){
        return this.id_joueur;
    }
}