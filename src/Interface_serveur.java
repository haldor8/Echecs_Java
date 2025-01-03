import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

// Permet d'envoyer des messages aux clients
public class Interface_serveur extends Thread {
    private final Socket id_socket_client;
    private ObjectInputStream entree;
    private ObjectOutputStream sortie;

    private final Serveur le_serveur;
    private final int id_joueur;

    public Interface_serveur(Socket soc, int num, Serveur s) {
        this.id_socket_client = soc;
        this.id_joueur = num;
        le_serveur = s;

        // Initialize streams for communication
        try {
            System.out.println("Tentative d'initialisation des flux pour l'interface serveur " + num);
            sortie = new ObjectOutputStream(id_socket_client.getOutputStream());
            sortie.flush(); // Flush header to avoid potential stream corruption
            entree = new ObjectInputStream(id_socket_client.getInputStream());
            System.out.println("Flux initialisés avec succès pour l'interface serveur " + num);
        } catch (Exception ex) {
            System.out.println("Erreur lors de l'initialisation des flux pour l'interface serveur " + num);
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Interface serveur démarrée pour le joueur " + id_joueur);

        while (true) {
            try {
                Object obj = entree.readObject(); // Objet reçu du client
                if (obj instanceof int[]) {
                    int[] mouvement = (int[]) obj; // Format : {x_initial, y_initial, x_final, y_final}
                    System.out.println("Mouvement reçu : " + Arrays.toString(mouvement));

                    // Validate movement logic here
                    boolean valide = validerMouvement(mouvement);
                    if (valide) {
                        System.out.println("Mouvement valide, diffusion aux clients.");
                        notifierTousLesClients(mouvement);
                    } else {
                        System.out.println("Mouvement invalide, rejeté.");
                    }
                }
            } catch (Exception e) {
                System.out.println("Erreur dans l'interface serveur du joueur " + id_joueur);
                e.printStackTrace();
                break;
            }
        }
    }

    // Méthode pour valider un mouvement
    private boolean validerMouvement(int[] mouvement) {
        // Logic validation of movement (to be implemented)
        return true; // For now, always valid
    }

    // Méthode pour notifier tous les clients
    private void notifierTousLesClients(int[] mouvement) {
        for (Interface_serveur interfaceServeur : le_serveur.get_liste_des_interfaces()) {
            try {
                synchronized (interfaceServeur.sortie) {
                    interfaceServeur.sortie.writeObject(mouvement);
                    interfaceServeur.sortie.flush();
                }
            } catch (IOException e) {
                System.out.println("Erreur lors de l'envoi du mouvement au client.");
                e.printStackTrace();
            }
        }
    }
}