import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.ServerSocket;

import javax.swing.*;
import java.awt.*;
import java.util.*;

// Gère les connexions des joueurs
public class Serveur extends Thread {
    // Sert juste à afficher les joueurs connectés et potentiellement l'état de la
    // partie

    private JFrame ui_serveur;
    private final ServerSocket id_socket_serveur;

    private LinkedList<Interface_serveur> liste_des_interfaces;
    private List<Client> liste_clients;

    public Serveur() throws IOException {
        super();
        ui_serveur = new JFrame();
        ui_serveur.setSize(300, 300);
        ui_serveur.setLocation(0, 0);
        ui_serveur.setLayout(new BorderLayout());
        listeDesIP = new JList();
        ui_serveur.add(listeDesIP, BorderLayout.CENTER);
        ui_serveur.setVisible(true);
        dlm = new DefaultListModel();
        laListeDesConnexions = new LinkedList<>();

        listeDesClients = new ArrayList<>();


        this.socketDuServeurQuiEcoute = new ServerSocket(49512); // numero de port un peu au hasard

    }
}
