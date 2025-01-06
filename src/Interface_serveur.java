import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import  java.io.*;
import java.util.Arrays;

// Permet d'envoyer des messages aux clients
public class Interface_serveur extends Thread {
    // On vérifie les coups à la réception du message, si invalide on le refuse, si valide on envoie le deplacement à effectuer aux clients
    // On change le joueur s'il est valide

    private final Socket id_socket_client;
    private ObjectInputStream entree;
    private ObjectOutputStream sortie;

    private Serveur le_serveur;
    private int id_joueur;


    public Interface_serveur(Socket soc, int num, Serveur s){
        this.id_socket_client = soc;
        this.id_joueur = num;
        le_serveur = s;

        // Demarrer le lien entre l'interface serveur et celles des clients
        try {
            System.out.println("Tentative d'initialisation du flux d'entrée pour interface serveur " + num);
            entree = new ObjectInputStream(id_socket_client.getInputStream());
            System.out.println("Flux d'entrée initialisé avec succès pour interface serveur " + num);
        } catch (Exception ex) {
            System.out.println("Erreur lors de l'initialisation du flux d'entrée pour interface serveur " + num);
            ex.printStackTrace();
        }
    }

    @Override
    public void run(){
        System.out.println("Interface serveur " + this.id_joueur + " lancée");

        try {
            entree = new ObjectInputStream(this.id_socket_client.getInputStream()); // Récupérer le flux d'entrée
        } catch (IOException exep) {
            System.out.println("Erreur lors de la récupération de l'interface serveur du client : " + this.id_joueur);
        }

        Object obj = null; // Initialiser la variable ici avant d'entrer dans la boucle
        do {
            try {
                obj = entree.readObject(); // Recevoir l'objet envoyé par le client
                System.out.println("Inter serveur : " + this.id_joueur + " a reçu un objet");

                    if (obj instanceof Deplacement) {
                        Deplacement deplacement = (Deplacement) obj; // Déplacement sous forme de tableau
                        System.out.println("Déplacement recu cote serveur : " + deplacement.toString());

                    // Relayer le déplacement à tous les clients
                    for (Interface_serveur interf : le_serveur.get_liste_des_interfaces()) {
                        // Envoyer le déplacement à tous les clients
                        try {
                            ObjectOutputStream sortie = new ObjectOutputStream(interf.id_socket_client.getOutputStream());
                            sortie.writeObject(deplacement);
                            System.out.println("Message envoye au socket : " + interf.id_socket_client);
                        } catch (IOException e) {
                            System.out.println("Erreur lors de l'envoi du déplacement au client " + interf.id_joueur);
                        }
                    }
                }

            } catch (IOException | ClassNotFoundException exep) {
                System.out.println("Erreur dans le thread de l'interface serveur du joueur : " + this.id_joueur);
                exep.printStackTrace();
            }
        } while (obj != null); // La condition de la boucle continue tant que obj n'est pas null
    }

}
