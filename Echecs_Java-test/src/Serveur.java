import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.ServerSocket;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

// Gère les connexions des joueurs
public class Serveur extends Thread {
    // Sert juste à afficher les joueurs connectés et potentiellement l'état de la
    // partie

    private JFrame ui_serveur;
    private final ServerSocket id_socket_serveur;

    private JList interface_liste_ip;
    private DefaultListModel liste_ip;

    private LinkedList<Interface_serveur> liste_des_interfaces;
    private List<Client> liste_clients;

    public Serveur() throws IOException {
        super();
        ui_serveur = new JFrame();

        ui_serveur.setSize(300, 300);
        ui_serveur.setLocation(0, 0);
        ui_serveur.setLayout(new BorderLayout());

        interface_liste_ip = new JList();
        ui_serveur.add(interface_liste_ip, BorderLayout.CENTER);

        ui_serveur.setVisible(true);

        liste_des_interfaces = new LinkedList<>();
        liste_clients = new ArrayList<>();
        liste_ip = new DefaultListModel();

        this.id_socket_serveur = new ServerSocket(49512); // numero de port un peu au hasard
    }

    public void run() {
        Interface_serveur connex;
        do {
            try {
                System.out.println("Serveur en attente");
                Socket socket_client = id_socket_serveur.accept();

                liste_ip.addElement("-->l'ip " + socket_client.getInetAddress() + " s'est connecte");
                interface_liste_ip.setModel(liste_ip);
                
                connex = new Interface_serveur(socket_client, liste_clients.get(0).get_id_joueur(), this);

                liste_des_interfaces.add(connex);
        
                System.out.println("nombre de clients connectés " + liste_clients.size());
                connex.start();
            } catch (IOException e) {
                System.out.println("IOException coté serveur : ");
                e.printStackTrace();
            } catch (Exception ex) {
                System.out.println("Exception inconnue coté serveur : ");
                ex.printStackTrace();
            }
        } while (true);
    }

    // Méthode pour ajouter un client à la liste des clients du serveur
    public void ajouter_client(Client client) {
        liste_clients.add(client);
    }

    public LinkedList<Interface_serveur> get_liste_des_interfaces(){
        return this.liste_des_interfaces;
    }
}
