package rahulmishra.app.newsboard.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rahulmishra.app.newsboard.R;
import rahulmishra.app.newsboard.service.model.SourcesStructure;
import rahulmishra.app.newsboard.util.GeneralUtils;

public class SourceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SourcesStructure> mResources = new ArrayList<>();
    private Context context;
    private OnSourceItemClicked onSourceItemClicked;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.fragment_source_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final SourcesStructure article = mResources.get(i);
        ViewHolder holder = (ViewHolder) viewHolder;

        GeneralUtils.ifNullViewGone(holder.title, article.getName());
        GeneralUtils.ifNullViewGone(holder.text_img, article.getName().substring(0, 1));
        GeneralUtils.ifNullViewGone(holder.category,
                article.getCategory().substring(0, 1).toUpperCase() + article.getCategory().substring(1));
        GeneralUtils.ifNullViewGone(holder.desc, article.getDescription());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSourceItemClicked.onItemClicked(article.getName());
            }
        });
        holder.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSourceItemClicked.onItemClicked(article.getCategory());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mResources.size();
    }

    public void setOnSourceItemClicked(OnSourceItemClicked onSourceItemClicked) {
        this.onSourceItemClicked = onSourceItemClicked;
    }

    public void setmResources(List<SourcesStructure> mResources) {
        this.mResources.clear();
        this.mResources.addAll(mResources);
        notifyDataSetChanged();
    }

    public interface OnSourceItemClicked {
        void onItemClicked(String query);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title, desc, text_img, category;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.source_title);
            desc = itemView.findViewById(R.id.source_desc);
            text_img = itemView.findViewById(R.id.source_text_img);
            category = itemView.findViewById(R.id.source_category);
        }
    }
}
