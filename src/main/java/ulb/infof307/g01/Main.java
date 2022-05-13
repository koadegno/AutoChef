package ulb.infof307.g01;


import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;

/**
 * Classe qui sert seulement à lancer l'application
 * <br>
 * Nécessaire pour faire un JAR avec {@code JavaFX}
 */
public class Main  {
    public static void main(String[] args) {

        String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = path.replace("g01-iteration-3.jar", "jniLibs/");

        ArcGISRuntimeEnvironment.setInstallDirectory(path);
        Autochef app = new Autochef();
        app.launchApp(args);
    }
}


