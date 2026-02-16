import dao.AbonneDAO;
import dao.CategorieDAO;
import dao.LocationDAO;
import dao.TitreDAO;
import models.Abonne;
import models.Categorie;
import models.Location;
import models.Titre;
import util.DatabaseConnection;

import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║         BIENVENUE DANS LE CLUB VIDÉO !         ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        try {
            // ÉTAPE 1 : Initialiser la connexion MySQL
            System.out.println("[1] Connexion à MySQL...");
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            dbConnection.testConnection();
            System.out.println("✓ Connecté !\n");


            // ÉTAPE 2 : Utiliser les DAO pour récupérer les données
            System.out.println("[2] Récupération des données...\n");

            // Récupérer les catégories
            CategorieDAO categorieDAO = new CategorieDAO();
            List<Categorie> categories = categorieDAO.readAll();
            System.out.println("   📂 " + categories.size() + " catégories trouvées");

            // Récupérer les titres
            TitreDAO titreDAO = new TitreDAO();
            List<Titre> titres = titreDAO.readAll();
            System.out.println("   🎬 " + titres.size() + " titres trouvés");

            // Récupérer les abonnés
            AbonneDAO abonneDAO = new AbonneDAO();
            List<Abonne> abonnes = abonneDAO.readAll();
            System.out.println("   👥 " + abonnes.size() + " abonnés inscrits");

            // Récupérer les locations
            LocationDAO locationDAO = new LocationDAO();
            List<Location> locations = locationDAO.readAll();
            System.out.println("   📺 " + locations.size() + " locations actives\n");


            // ÉTAPE 3 : Afficher les films "Action"
            System.out.println("[3] Films de la catégorie 'Action' :\n");
            List<Titre> actionFilms = titreDAO.findByCategorie(1);  // 1 = Action
            for (Titre titre : actionFilms) {
                System.out.println("   • " + titre.getNomTitre() +
                        " (" + titre.getDuree() + " min) - Par " + titre.getAuteur());
            }
            System.out.println();


            // ÉTAPE 4 : Afficher les locations d'un abonné
            System.out.println("[4] Locations de Jean Dupont (Abonné #1) :\n");
            List<Location> locationsJean = locationDAO.findByAbonne(1);
            System.out.println("   Il loue " + locationsJean.size() + " cassette(s)\n");


            // ÉTAPE 5 : Afficher le succès
            System.out.println("╔════════════════════════════════════════════════╗");
            System.out.println("║    ✓ PROGRAMME EXÉCUTÉ AVEC SUCCÈS !          ║");
            System.out.println("╚════════════════════════════════════════════════╝");

        } catch (Exception e) {
            System.err.println("❌ ERREUR : " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Fermer la connexion proprement
            DatabaseConnection.getInstance().closeConnection();
            System.out.println("\n✓ Connexion fermée");
        }
    }
}