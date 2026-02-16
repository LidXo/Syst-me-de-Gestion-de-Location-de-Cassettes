import dao.CategorieDAO;
import models.Categorie;
import util.DatabaseConnection;

import java.util.List;

public class TestConnection {

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════");
        System.out.println("        TEST DE CONNEXION MYSQL");
        System.out.println("═══════════════════════════════════════════════\n");

        try {
            // TEST 1 : Charger et tester la connexion
            System.out.println("Test 1 : Initialisation de la connexion...");
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            dbConnection.testConnection();
            System.out.println("✓ Étape 1 réussie !\n");

            // TEST 2 : Utiliser un DAO pour récupérer les données
            System.out.println("Test 2 : Récupération des catégories...");
            CategorieDAO categorieDAO = new CategorieDAO();
            List<Categorie> categories = categorieDAO.readAll();
            System.out.println("✓ Nombre de catégories trouvées : " + categories.size());
            System.out.println("  Voici les catégories :");
            for (Categorie cat : categories) {
                System.out.println("    - " + cat.getLibelle());
            }
            System.out.println();

            // RÉSULTAT FINAL
            System.out.println("═══════════════════════════════════════════════");
            System.out.println("✓✓✓ TOUS LES TESTS RÉUSSIS ! ✓✓✓");
            System.out.println("    Votre base MySQL est connectée !");
            System.out.println("═══════════════════════════════════════════════");

        } catch (Exception e) {
            System.err.println("❌ ERREUR ! Quelque chose ne fonctionne pas :");
            System.err.println(e.getMessage());
            System.err.println();
            System.err.println("Vérifiez :");
            System.err.println("  1. MySQL est lancé");
            System.err.println("  2. La base 'club_video' existe");
            System.err.println("  3. database.properties est bien créé");
            System.err.println("  4. Le mot de passe MySQL est correct");
            e.printStackTrace();
        } finally {
            // Fermer proprement
            DatabaseConnection.getInstance().closeConnection();
        }
    }
}