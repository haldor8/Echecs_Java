import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

// Interface utilisateur
public class Client extends JFrame {
    // Ne pas oublier d'inverser le plateau visuellement lorsque les pions noirs
    // jouent

    private int numero;
    private JPanel Couleur;
    private Plateau le_plateau;

    private ObjectOutputStream sortie;
    private Interface_client i_client;

    public Client(int num_client) throws HeadlessException {
        super();
        numero = num_client;

        this.setLayout(new BorderLayout());

        Couleur = new JPanel();
        if (numero % 2 == 1) {
            Couleur.setBackground(Color.BLACK);
            // le_plateau = new Plateau(Color.BLACK);

        } else {
            Couleur.setBackground(Color.WHITE);
            // le_plateau = new Plateau(Color.yellow);
        }
        this.add(le_plateau, BorderLayout.CENTER);

        /*
         * // Si on veut ajouter un compteur etc
         * messageArea = new JTextArea();
         * messageArea.setEditable(false); // Empêcher l'édition du texte
         * JScrollPane scrollPane = new JScrollPane(messageArea); // Ajouter une barre
         * de défilement
         * this.add(scrollPane, BorderLayout.SOUTH);
         */

        this.add(Couleur, BorderLayout.SOUTH); // Pour savoir quel joueur on est, on peut rajouter un compteur dedans
        this.setLocation((num_client - 1) * 400, 400);
        this.setSize(400, 400);
        this.setVisible(true);

        /*
         * try {
         * InetAddress ip = InetAddress.getLocalHost();
         * System.out.println(ip);
         * Socket soc = new Socket(ip, 49512);
         * sortie = new ObjectOutputStream(soc.getOutputStream());
         * le_plateau.setOut(sortie);
         * i_client = new Interface_client(soc, x, le_plateau);
         * i_client.start();
         * } catch (UnknownHostException uhe) {
         * System.out.println("Souci de hostName  chez le client !!");
         * } catch (IOException uhe) {
         * System.out.println("Souci de connexion chez le client !!");
         * }
         */

    }
}
