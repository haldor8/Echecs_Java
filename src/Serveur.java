import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.ServerSocket;

import javax.swing.*;
import java.awt.*;
import java.awt.List;
import java.util.*;

// Gère les connexions des joueurs
public class Serveur extends Thread {
    // Sert juste à afficher les joueurs connectés et potentiellement l'état de la
    // partie

    private JFrame ui_serveur;
    private final ServerSocket id_socket_serveur;

    private LinkedList<Interface_serveur> liste_des_interfaces;
    private List<Client> liste_clients;

}
