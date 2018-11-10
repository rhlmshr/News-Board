package rahulmishra.app.newsboard;

import android.app.Application;
import android.content.res.Resources;

import rahulmishra.app.newsboard.service.database.DatabaseCreator;
import rahulmishra.app.newsboard.service.net.DataRepository;

public class NewsBoardApp extends Application {

    private static NewsBoardApp app;
    private static Resources res;

    public static NewsBoardApp instance() {
        return app;
    }

    public static Resources getResourses() {
        return res;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        res = getResources();
        DataRepository.instance();
        DatabaseCreator.getInstance().createDb(this);
    }
}
