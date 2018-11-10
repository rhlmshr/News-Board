package rahulmishra.app.newsboard.service.database;

import android.arch.lifecycle.LiveData;
import android.os.Handler;
import android.os.HandlerThread;

import java.util.List;

import rahulmishra.app.newsboard.service.model.ArticleStructure;
import rahulmishra.app.newsboard.views.interfaces.DbQueryListener;

public class LocalDataRepository {

    private static LocalDataRepository dataRepository;
    private AppDatabase appDatabase;

    private LocalDataRepository() {
        appDatabase = DatabaseCreator.getInstance().getDatabase();
    }

    public static LocalDataRepository instance() {
        if (dataRepository == null) {
            dataRepository = new LocalDataRepository();
        }
        return dataRepository;
    }

    public void storeArticle(final ArticleStructure item, final DbQueryListener listener) {
        final HandlerThread mHandlerThread = new HandlerThread("Handler");
        mHandlerThread.start();
        final Handler handler = new Handler(mHandlerThread.getLooper());
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                appDatabase.getNewsItemDao().storeArticle(item);
                listener.onSuccess();
            }
        };
        handler.post(runnable);
    }

    public LiveData<List<ArticleStructure>> getAllArticles() {
        return appDatabase.getNewsItemDao().getAllArticles();
    }

    public void removeArticle(final ArticleStructure item, final DbQueryListener listener) {
        final HandlerThread mHandlerThread = new HandlerThread("Handler");
        mHandlerThread.start();
        final Handler handler = new Handler(mHandlerThread.getLooper());
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                appDatabase.getNewsItemDao().removeArticle(item);
                listener.onSuccess();
            }
        };
        handler.post(runnable);
    }
}
