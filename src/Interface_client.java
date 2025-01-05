import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

// Permet d'envoyer des messages au serveur
public class Interface_client extends Thread {
    // Mettre à jour le plateau côté client si on reçoit un coup

    private Socket id_socket_client;

    public ObjectOutputStream getSortie() {
        return sortie;
    }

    public void setSortie(ObjectOutputStream sortie) {
        this.sortie = sortie;
    }

    public ObjectInputStream getEntree() {
        return entree;
    }

    public void setEntree(ObjectInputStream entree) {
        this.entree = entree;
    }

    private ObjectInputStream entree;
    private ObjectOutputStream sortie;
    private Plateau le_plateau;
    private int id_joueur;

    public Interface_client(Socket soc, int num, Plateau plto){
        super();
        id_socket_client = soc;
        id_joueur = num;
        le_plateau = plto;

        // On connecte la sortie de l'interface vers le socket du client
        try {
            sortie = new ObjectOutputStream(soc.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Code du thread
    public void run(){
        Object obj;
        Boolean continuer = true;
        do{
            try{
                entree = new ObjectInputStream(id_socket_client.getInputStream()); // On lit le flux d'entrée pour récupérer les objets envoyés
                obj = entree.readObject(); // L'objet que reçoit l'interface

                if(obj instanceof Pion){
                    Pion pion_recu = (Pion)obj;
                    // System.out.println("Reception cote client :" + pion_recu.toString());
                }
                // Ajouter quelque-chose pour recevoir un objet qui désactive l'interface jusqu'à réactivation
                // Ajouter quelque-chose pour recevoir une notif sur l'état de la partie (gagnée, égalitée etc)
                
                // Mettre à jour le plateau ici
            }catch(IOException exec){
                System.out.println("Soucis dans le thread de l'interface client numero : " + id_joueur);
                exec.printStackTrace();
            }catch(ClassNotFoundException exec){
                System.out.println("Objet de type inconnu recu");
                exec.printStackTrace();
            }
        }while (continuer);
    }
}
