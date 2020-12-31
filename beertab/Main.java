package beertab;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;

public class Main {

    public static void main(String[] args) {
        // Launch splashscreen (Preloader) before main
        LauncherImpl.launchApplication(Beertab.class, BeertabPreloader.class, args);
    }

}
