package clubvideo;

import clubvideo.util.UIStylesDark;
import clubvideo.view.LoginFrame;
import javax.swing.*;

/**
 * ╔══════════════════════════════════════════════════════════╗
 *  PROJET CLUB VIDÉO — Application de Gestion
 *  HIT-T | 97 31 77 52 | formationfacile.hitt.tg@gmail.com
 *
 *  Prérequis :
 *   1. MySQL installé avec la base "clubvideo" créée
 *      (voir /sql/clubvideo.sql)
 *   2. mysql-connector-j-x.x.x.jar dans le classpath
 *
 *  Compilation :
 *   javac -cp .;mysql-connector-j.jar -d out
 *         src/main/java/clubvideo/**\/*.java
 *
 *  Exécution :
 *   java -cp out;mysql-connector-j.jar clubvideo.Main
 * ╚══════════════════════════════════════════════════════════╝
 */
public class Main {

    public static void main(String[] args) {
        // Appliquer le thème avant toute création de composant
        UIStylesDark.setup();

        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}
