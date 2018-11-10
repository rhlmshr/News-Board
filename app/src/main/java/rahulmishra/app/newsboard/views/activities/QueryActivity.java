package rahulmishra.app.newsboard.views.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import rahulmishra.app.newsboard.R;
import rahulmishra.app.newsboard.service.model.ArticleStructure;
import rahulmishra.app.newsboard.service.model.NewsResponse;
import rahulmishra.app.newsboard.viewmodel.NewsViewModel;
import rahulmishra.app.newsboard.views.adapters.DiscoverAdapter;
import rahulmishra.app.newsboard.views.interfaces.DbQueryListener;

public class QueryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,
        DiscoverAdapter.OnSaveClickListener {

    private SwipeRefreshLayout queryRefresh;
    private RecyclerView queryList;
    private String query;
    private NewsViewModel newsViewModel;
    private DiscoverAdapter discoverAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        queryRefresh = findViewById(R.id.query_refresh);
        queryList = findViewById(R.id.query_list);
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        discoverAdapter = new DiscoverAdapter();

        discoverAdapter.setOnSaveClickListener(this);
        queryRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
        queryRefresh.setOnRefreshListener(this);

        queryList.setLayoutManager(new LinearLayoutManager(this));
        queryList.setAdapter(discoverAdapter);

        queryRefresh.setRefreshing(true);
        this.query = getIntent().getStringExtra("query");
        setTitle(query.toUpperCase());
        onRefresh();
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        newsViewModel.getSearchResult(query).observe(this, new Observer<NewsResponse>() {
            @Override
            public void onChanged(@Nullable NewsResponse newsResponse) {
                if (newsResponse != null && newsResponse.getStatus().equalsIgnoreCase("ok")) {
                    discoverAdapter.setmResources(newsResponse.getArticles());
                }
                queryRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onSaveClicked(ArticleStructure articleStructure) {
        newsViewModel.storeArticle(articleStructure, new DbQueryListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(QueryActivity.this, "Article Archived!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
