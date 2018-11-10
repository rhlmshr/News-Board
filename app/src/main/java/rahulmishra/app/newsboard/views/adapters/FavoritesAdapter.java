package rahulmishra.app.newsboard.views.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ArticleStructure> mResources = new ArrayList<>();
    private Context context;
    private OnSaveClickListener onSaveClickListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.fragment_favorites_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final ArticleStructure article = mResources.get(i);
        ViewHolder holder = (ViewHolder) viewHolder;

        GeneralUtils.ifNullViewGone(holder.title, article.getTitle());
        GeneralUtils.ifNullViewGone(holder.source, article.getAuthor());
        GeneralUtils.ifNullViewGone(holder.subtext, article.getDescription());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_placeholder);

        Glide.with(context)
                .load(article.getUrlToImage())
                .apply(options)
                .apply(options).transition(new DrawableTransitionOptions().crossFade())
                .into(holder.thumb);
    }

    @Override
    public int getItemCount() {
        return mResources.size();
    }

    public void setmResources(List<ArticleStructure> mResources) {
        this.mResources.clear();
        this.mResources.addAll(mResources);
        notifyDataSetChanged();
    }

    public void setOnSaveClickListener(OnSaveClickListener onSaveClickListener) {
        this.onSaveClickListener = onSaveClickListener;
    }

    public interface OnSaveClickListener {
        void onRemoveClicked(ArticleStructure articleStructure);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageView thumb;
        private TextView title, source, subtext;
        private Button save;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.discover_thumb);
            title = itemView.findViewById(R.id.discover_title);
            source = itemView.findViewById(R.id.discover_source);
            subtext = itemView.findViewById(R.id.discover_subtext);
            save = itemView.findViewById(R.id.discover_save);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String headLine = mResources.get(getAdapterPosition()).getTitle();
            if (headLine.endsWith(" - Times of India")) {
                headLine = headLine.replace(" - Times of India", "");
            } else if (headLine.endsWith(" - Firstpost")) {
                headLine = headLine.replace(" - Firstpost", "");
            }
            String description = mResources.get(getAdapterPosition()).getDescription();
            String date = mResources.get(getAdapterPosition()).getPublishedAt();
            String imgURL = mResources.get(getAdapterPosition()).getUrlToImage();
            String URL = mResources.get(getAdapterPosition()).getUrl();

            Intent intent = new Intent(context, ArticleActivity.class);

            intent.putExtra(Constants.INTENT_HEADLINE, headLine);
            intent.putExtra(Constants.INTENT_DESCRIPTION, description);
            intent.putExtra(Constants.INTENT_DATE, date);
            intent.putExtra(Constants.INTENT_IMG_URL, imgURL);
            intent.putExtra(Constants.INTENT_ARTICLE_URL, URL);

            context.startActivity(intent);

            ((Activity) context).overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Do you want to remove this article?")
                    .setTitle("Are you sure?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onSaveClickListener.onRemoveClicked(mResources.get(getAdapterPosition()));
                    mResources.remove(mResources.get(getAdapterPosition()));
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), getItemCount());
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
    }
}
