package rahulmishra.app.newsboard.service.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import rahulmishra.app.newsboard.service.model.ArticleStructure;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
interface NewsItemDao {

    @Insert(onConflict = REPLACE)
    void storeArticle(ArticleStructure articleStructure);

    @Query("SELECT * from ArticleStructure ORDER BY id DESC")
    LiveData<List<ArticleStructure>> getAllArticles();

    @Delete
    void removeArticle(ArticleStructure articleStructure);
}
