package rahulmishra.app.newsboard.service.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import rahulmishra.app.newsboard.service.model.ArticleStructure;
import rahulmishra.app.newsboard.util.IntArrayConverter;
import rahulmishra.app.newsboard.util.StringArrayConverter;

@Database(entities = {ArticleStructure.class},
        version = AppDatabase.DATABASE_VERSION,
        exportSchema = false)
@TypeConverters({IntArrayConverter.class, StringArrayConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "News.db";

    public abstract NewsItemDao getNewsItemDao();

}
