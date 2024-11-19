public class Main {
    public static void main(String[] args) {
        // Set le thème lors de l'initialisation du plateau (optionnel)
        // Ouvrir une fenêtre pour instancier les clients et les serveurs (optionnel)

        // Démarrer les clients, les serveurs
        Serveur le_serveur = null;
        try {
            le_serveur = new Serveur(); // Variable modifiable vers liste de serveurs pour lancer plusieurs instances
            le_serveur.start();// appel de run()
        }catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Client c1 = new Client(0);
            Client c2 = new Client(1);

            // Ajouter les clients au serveur correspondant
            le_serveur.ajouter_client(c1);
            le_serveur.ajouter_client(c2);
        }catch (Exception e) {
            System.out.println("Probleme lors de l'instanciation des clients");
            e.printStackTrace();
        }
    }
}
