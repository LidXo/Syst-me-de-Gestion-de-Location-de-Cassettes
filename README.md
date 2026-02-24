# 🎬 PROJET CLUB VIDÉO — Application de Gestion Java Swing

Une application complète de gestion de vidéo-club avec une interface graphique moderne (thème sombre) développée en Java Swing.

---

## 🔐 Connexion par défaut

Pour accéder à l'application, utilisez les identifiants suivants :

| Champ | Valeur |
| :--- | :--- |
| **Identifiant** | `admin` |
| **Mot de passe** | `1234` |

---

## ✨ Fonctionnalités Principales

*   **🎨 Interface Moderne** : Nouveau thème sombre ("Dark Mode") pour un confort visuel accru.
*   **🏠 Tableau de Bord** : Vue d'ensemble avec statistiques en temps réel (nombre d'abonnés, cassettes, locations en cours).
*   **📼 Gestion des Cassettes** : Ajout, modification, suppression et recherche de cassettes. Gestion des catégories.
*   **👤 Gestion des Abonnés** : Inscription, édition, et suppression. Génération automatique de carte de membre.
*   **🔄 Gestion des Locations** :
    *   Nouvelle location avec vérification des disponibilités.
    *   **Règle métier** : Limite de 3 locations simultanées par abonné.
    *   Retour de cassette facile et historique des locations.
*   **✉️ Contact** : Formulaire de contact intégré.

## 📂 Structure du Projet

```text
clubvideo/
├── sql/
│   └── clubvideo.sql              ← Script SQL complet (création base + données démo)
└── src/main/java/clubvideo/
    ├── Main.java                  ← Point d'entrée de l'application
    ├── dao/                       ← Couche d'accès aux données (JDBC)
    │   ├── DatabaseConnection.java
    │   ├── ... (DAO pour chaque entité)
    ├── model/                     ← Modèles de données (POJO)
    │   ├── ... (Classes Java correspondant aux tables)
    ├── view/                      ← Interface Graphique (Swing)
    │   ├── LoginFrame.java        ← Fenêtre de connexion
    │   ├── MainFrame.java         ← Fenêtre principale
    │   ├── ... (Panneaux pour chaque fonctionnalité)
    └── util/                      ← Utilitaires
        ├── UIStylesDark.java      ← Nouveau moteur de thème sombre
        └── UIStyles.java          ← (Ancien thème clair, conservé pour référence)
```

## 🚀 Installation et Exécution

### Prérequis
*   **Java JDK 8** ou supérieur.
*   **MySQL 5.7+** ou MariaDB.
*   **Pilote JDBC** : `mysql-connector-j-x.x.x.jar` (à placer à la racine ou dans le classpath).

### 1. Base de Données
Importez le script SQL fourni pour créer la base de données et les tables :
```bash
mysql -u root -p < sql/clubvideo.sql
```

### 2. Configuration
Vérifiez les paramètres de connexion dans `src/main/java/clubvideo/dao/DatabaseConnection.java` :
```java
private static final String URL      = "jdbc:mysql://localhost:3306/clubvideo?useSSL=false&serverTimezone=UTC";
private static final String USER     = "root";
private static final String PASSWORD = ""; // Votre mot de passe MySQL
```

### 3. Compilation
Depuis la racine du projet :
```bash
# Windows
javac -cp ".;mysql-connector-j-8.x.x.jar" -d out src/main/java/clubvideo/Main.java src/main/java/clubvideo/**/*.java

# Linux / Mac
javac -cp ".:mysql-connector-j-8.x.x.jar" -d out src/main/java/clubvideo/Main.java src/main/java/clubvideo/**/*.java
```

### 4. Lancement
```bash
# Windows
java -cp "out;mysql-connector-j-8.x.x.jar" clubvideo.Main

# Linux / Mac
java -cp "out:mysql-connector-j-8.x.x.jar" clubvideo.Main
```

---
*© 2026 Lidao - Tous droits réservés.*
