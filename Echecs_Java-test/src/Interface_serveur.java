import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
        System.out.println("Interface serveur du client : " + id_joueur + " lancee");

        // On se branche sur la sortie du client
        try{
            entree = new ObjectInputStream(this.id_socket_client.getInputStream());
        }catch(Exception exep){
            System.out.println("Probleme lors de la recup de l'interface serveur du client : " + this.id_joueur);
        }

        Object obj = null;
        Pieces piece_recu = null;
        do{
            System.out.println("Interface serveur " + this.id_joueur + " en attente de reception d'un objet");
            try{
                obj = entree.readObject();
                System.out.println("Inter serv : " + this.id_joueur + " a recu un objet");
                

                if (obj instanceof Pieces){
                    piece_recu = (Pieces)obj;
                    System.out.println("Pion recu : " + piece_recu.toString());
                    
                    // Effectuer le traitement du coup ici
                    for(Interface_serveur interf : le_serveur.get_liste_des_interfaces()){
                        sortie = new ObjectOutputStream(interf.id_socket_client.getOutputStream());
                        sortie.writeObject(piece_recu);
                    }
                }// else if (instanceof Message_interne) ...
            }catch(Exception exep){
                System.out.println("Erreur dans le thread de l'interf serveur du joueur : " + this.id_joueur + exep.toString());
            }
        }while(obj != null);
    }
}
