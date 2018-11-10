package rahulmishra.app.newsboard.views.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import rahulmishra.app.newsboard.R;
import rahulmishra.app.newsboard.util.Constants;

public class ArticleActivity extends AppCompatActivity {

    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        createToolbar();
        receiveFromDataAdapter();
        buttonLinktoFullArticle();
        floatingButton();
    }

    private void buttonLinktoFullArticle() {
        Button linkToFullArticle = findViewById(R.id.article_button);
        linkToFullArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebViewActivity();
            }
        });
    }

    private void openWebViewActivity() {
        Intent browserIntent = new Intent(ArticleActivity.this, WebViewActivity.class);
        browserIntent.putExtra(Constants.INTENT_URL, URL);
        startActivity(browserIntent);
        this.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    private void receiveFromDataAdapter() {
        String headLine = getIntent().getStringExtra(Constants.INTENT_HEADLINE);
        String description = getIntent().getStringExtra(Constants.INTENT_DESCRIPTION);
        String date = getIntent().getStringExtra(Constants.INTENT_DATE);
        String imgURL = getIntent().getStringExtra(Constants.INTENT_IMG_URL);
        URL = getIntent().getStringExtra(Constants.INTENT_ARTICLE_URL);

        TextView content_Headline = findViewById(R.id.content_Headline);
        content_Headline.setText(headLine);

        TextView content_Description = findViewById(R.id.content_Description);
        content_Description.setText(description);

        ImageView collapsingImage = findViewById(R.id.collapsingImage);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_placeholder);

        Glide.with(this)
                .load(imgURL)
                .apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(collapsingImage);
    }

    private void createToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_article);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void floatingButton() {
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this news! Send from News Baord App\n" + Uri.parse(URL));
                startActivity(Intent.createChooser(shareIntent, "Share with"));
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*
             * Override the Up/Home Button
             * */
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
