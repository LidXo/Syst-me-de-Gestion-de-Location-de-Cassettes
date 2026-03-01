# 🎥 VideoClub Manager

**VideoClub Manager** est une application desktop robuste développée en Java (Swing) pour la gestion complète d'un club de location de vidéos. Elle permet de gérer les abonnés, le catalogue de cassettes, les catégories et le suivi des locations en temps réel.

---

## ✨ Fonctionnalités

L'application est structurée autour de plusieurs modules clés :

*   **🔐 Authentification** : Système de connexion sécurisé pour les administrateurs ( user: admin ; mdp: admin123 ).
*   **👥 Gestion des Abonnés** :
    *   Ajout, modification et suppression d'abonnés.
    *   Génération de cartes d'abonnement personnalisées.
*   **📼 Catalogue de Cassettes** :
    *   Gestion de l'inventaire des cassettes vidéo par catégorie.
    *   Suivi de l'état des stocks.
*   **📑 Gestion des Catégories** : Organisation du catalogue par thématiques (Action, Comédie, Drame, etc.).
*   **🔄 Suivi des Locations** :
    *   Enregistrement des nouvelles locations.
    *   Suivi des dates de retour et historique.
*   **📍 Tableau de Bord** : Interface d'accueil intuitive avec accès rapide aux différentes sections.

---

## 🛠️ Technologies Utilisées

*   **Langage** : Java (JDK 8 ou supérieur)
*   **Interface Graphique** : Java Swing (AWT/Swing)
*   **Base de Données** : MySQL
*   **Accès aux Données** : JDBC (Java Database Connectivity) avec un pattern **DAO (Data Access Object)** pour une architecture propre.

---

## 📂 Structure du Projet

```text
src/clubvideo/
├── dao/        # Logique d'accès à la base de données (AbonneDAO, LocationDAO, etc.)
├── model/      # Classes métier (Abonne, Cassette, Location)
├── view/       # Composants de l'interface graphique (MainFrame, Panels)
├── util/       # Classes utilitaires et styles UI
└── Main.java   # Point d'entrée de l'application
```

---

## 🚀 Installation & Lancement

### Prérequis
1.  **JDK** installé sur votre machine.
2.  Serveur **MySQL** actif.
3.  **mysql-connector-j-*.jar** placé dans le dossier `lib/`.

### Configuration de la Base de Données
Modifiez les paramètres de connexion dans le fichier :
`src/clubvideo/dao/DatabaseConnection.java`
```java
private static final String URL = "jdbc:mysql://localhost:3306/votre_base";
private static final String USER = "votre_utilisateur";
private static final String PASSWORD = "votre_mot_de_passe";
```

### Lancement via Scripts (Recommandé)
Des scripts d'automatisation sont fournis à la racine du projet :

#### Sous Windows :
-   Double-cliquez sur `compile.bat` pour compiler le projet.
-   Double-cliquez sur `run.bat` pour lancer l'application.

#### Sous Linux/Mac :
```bash
chmod +x compile.sh run.sh
./compile.sh
./run.sh
```

### Lancement via un IDE (IntelliJ / Eclipse)
1.  Importez le projet en tant que projet Java.
2.  Ajoutez le JAR MySQL Connector au "Build Path" ou aux bibliothèques du projet.
3.  Exécutez la classe `clubvideo.Main`.

---

## 🎨 Design & UI
L'application utilise un système de styles personnalisés (`UIStyles.java`) pour offrir une interface moderne, cohérente et agréable à l'utilisation (Gradients, polices personnalisées, etc.).

---

## 📝 Auteur
**Lidao** - Étudiant en Informatique IA et BigData.
