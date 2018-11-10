package rahulmishra.app.newsboard.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import rahulmishra.app.newsboard.R;
import rahulmishra.app.newsboard.service.model.ArticleStructure;
import rahulmishra.app.newsboard.util.Constants;
import rahulmishra.app.newsboard.util.GeneralUtils;
import rahulmishra.app.newsboard.views.activities.ArticleActivity;

public class HeadlinesAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<ArticleStructure> mResources;

    public HeadlinesAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResources = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mResources.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.top_headlines_item, container, false);

        ImageView thumb = itemView.findViewById(R.id.headlines_thumbnail);
        TextView title = itemView.findViewById(R.id.headlines_text);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_placeholder);

        Glide.with(mContext)
                .load(mResources.get(position).getUrlToImage())
                .apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(thumb);

        GeneralUtils.ifNullViewGone(title, mResources.get(position).getTitle());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String headLine = mResources.get(position).getTitle();
                if (headLine.endsWith(" - Times of India")) {
                    headLine = headLine.replace(" - Times of India", "");
                } else if (headLine.endsWith(" - Firstpost")) {
                    headLine = headLine.replace(" - Firstpost", "");
                }
                String description = mResources.get(position).getDescription();
                String date = mResources.get(position).getPublishedAt();
                String imgURL = mResources.get(position).getUrlToImage();
                String URL = mResources.get(position).getUrl();

                Intent intent = new Intent(mContext, ArticleActivity.class);

                intent.putExtra(Constants.INTENT_HEADLINE, headLine);
                intent.putExtra(Constants.INTENT_DESCRIPTION, description);
                intent.putExtra(Constants.INTENT_DATE, date);
                intent.putExtra(Constants.INTENT_IMG_URL, imgURL);
                intent.putExtra(Constants.INTENT_ARTICLE_URL, URL);

                mContext.startActivity(intent);

                ((Activity) mContext).overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void setmResources(List<ArticleStructure> mResources) {
        this.mResources.addAll(mResources);
        notifyDataSetChanged();
    }
}
