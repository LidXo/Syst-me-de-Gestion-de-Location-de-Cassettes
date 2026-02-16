# 🎬 Club Video — Système de Gestion de Location de Cassettes

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge\&logo=java)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge\&logo=mysql)
![JDBC](https://img.shields.io/badge/JDBC-Database-green?style=for-the-badge)
![Swing](https://img.shields.io/badge/GUI-Swing%2FJavaFX-purple?style=for-the-badge)
![Architecture](https://img.shields.io/badge/Architecture-DAO%20Pattern-red?style=for-the-badge)

</div>

---

## 📌 Description

Application Java complète permettant la gestion d’un club de location de cassettes vidéo.

Le système gère :

* 👤 Les abonnés
* 🎞 Les titres
* 📦 Les cassettes physiques
* 🗂 Les catégories
* 🔄 Les locations et retours

L’architecture repose sur une séparation en couches : **Models – DAO – GUI**, avec persistance via **JDBC et MySQL**.

---

# 🏗️ Architecture du Projet

```
src/
 ├── models/
 ├── dao/
 ├── gui/
 └── util/
```

### 🔹 Models

Classes entités (POJO) représentant les tables de la base.

### 🔹 DAO

Implémentation du pattern **Data Access Object** avec opérations CRUD complètes.

### 🔹 GUI

Interface graphique développée en Swing (ou JavaFX).

### 🔹 Util

Gestion centralisée de la connexion base de données (Singleton).

---

# 🗃️ Base de Données

## 📌 Entités principales

* ABONNE
* CATEGORIE
* TITRE
* CASSETTE
* LOCATION

## 🔐 Contraintes métier

* ✅ Maximum 3 locations simultanées par abonné
* ✅ Une cassette appartient à un seul titre
* ✅ Un titre appartient à une seule catégorie
* ✅ Intégrité référentielle (PK / FK)

---

# 🖥️ Interface Graphique

## 🔐 Page Login

Authentification sécurisée.

## 🏠 Page d’accueil

Navigation centralisée via menu principal.

## 📦 Gestion des Cassettes

* Ajouter
* Modifier
* Supprimer
* Rechercher

## 👤 Gestion des Abonnés

CRUD complet + recherche par nom.

## 🎞 Gestion des Titres et Catégories

Organisation logique du catalogue.

## 🔄 Gestion des Locations

* Vérification disponibilité
* Limite de 3 cassettes
* Enregistrement automatique de la date

## ↩ Gestion des Retours

* Sélection des locations actives
* Mise à jour automatique

---

# 🧠 Fonctionnalités Clés

✨ Recherche multicritère (titre, catégorie, auteur)

✨ Validation des saisies utilisateur

✨ Messages d’erreur explicites

✨ Architecture propre et maintenable

✨ Respect des bonnes pratiques JDBC

---

# 🚀 Installation

## 1️⃣ Prérequis

* Java JDK 17+
* MySQL 8+
* IDE (IntelliJ / Eclipse / NetBeans)

## 2️⃣ Cloner le projet

```bash
git clone https://github.com/votre-username/club-video.git
```

## 3️⃣ Créer la base de données

```sql
CREATE DATABASE club_video;
```

Importer ensuite :

* create_database.sql
* insert_test_data.sql

## 4️⃣ Configurer la connexion

Modifier le fichier :

```
database.properties
```

---

# 📊 Diagramme Conceptuel (MCD)

```
CATEGORIE (1,N) — TITRE (1,N) — CASSETTE

ABONNE (0,3) — LOCATION — (0,1) CASSETTE
```

---

# 🧪 Tests Réalisés

✔ CRUD complet validé

✔ Vérification contrainte 3 locations

✔ Tests d’intégrité référentielle

✔ Tests d’erreurs de saisie

✔ Scénarios complets (inscription → location → retour)

---

# 📦 Livrables

* 📄 Rapport technique (Word)
* 🗃 Scripts SQL
* 💻 Code source structuré
* 📸 Captures d’écran
* 📘 README détaillé

---

# 🔮 Améliorations Futures

* 📈 Statistiques d’activité
* 📄 Export PDF des locations
* 🔐 Gestion avancée des rôles utilisateurs
* ☁ Migration vers architecture Spring Boot

---

# 📜 Licence

Projet académique – Usage pédagogique.

---

<div align="center">

### 🎬 Club Video — Gestion Professionnelle de Location

Conçu avec rigueur, architecture propre et respect des standards Java.

</div>
