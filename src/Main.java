public class Main {
    public static void main(String[] args) {
        // Set le thème lors de l'initialisation du plateau (optionnel)
        // Ouvrir une fenêtre pour instancier les clients et les serveurs (optionnel)
        // Démarrer les clients, les serveurs
        // Ajouter les clients au serveur correspondant
        Serveur le_serveur = null;
        try {
            le_serveur = new Serveur();
            le_serveur.start();// appel de run()
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            Client c1 = new Client(1);
            Client c2 = new Client(2);

            // Après avoir instancié les clients, les ajouter au serveur
            le_serveur.ajouter_client(c1);
            le_serveur.ajouter_client(c2);

            // Client c3 = new Client(3);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
