package beertab;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;

public class Main {

    public static final double conDbStart = 20;
    public static final double conDbSuccess = 30;
    public static final double conDbFailUnknownHost = 21;
    public static final double conDbFailIO = 22;
    public static final double conDbFailUnknownError = 29;

    public static void main(String[] args) {
        // Launch splashscreen (Preloader) before main
        LauncherImpl.launchApplication(Beertab.class, BeertabPreloader.class, args);
    }

}
