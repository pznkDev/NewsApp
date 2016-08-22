package slava.kpi.com.newsusa.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import slava.kpi.com.newsusa.R;
import slava.kpi.com.newsusa.entities.ArticleShort;

public class ShortArticleListAdapter extends RecyclerView.Adapter<ShortArticleListAdapter.ViewHolder> {

    List<ArticleShort> allNews = new ArrayList<ArticleShort>();
    Context context;

    public interface OnArticleClickListener {
        void onClick(String articleFullURL, String title);
    }

    private OnArticleClickListener mArticleClickListener;

    public void setArticleListener(OnArticleClickListener mArticleClickListener) {
        this.mArticleClickListener = mArticleClickListener;
    }


    public ShortArticleListAdapter(Context context, List<ArticleShort> allNews) {
        this.allNews = allNews;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_article_short, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.img.setVisibility(View.INVISIBLE);
        holder.tvCap.setVisibility(View.INVISIBLE);

        holder.tvTitle.setText(allNews.get(position).getTitle());
        holder.tvDate.setText(allNews.get(position).getDate());
        // if article has image then load it, otherwise don't load image
        if (allNews.get(position).getImgURL().equals("")) holder.tvCap.setVisibility(View.VISIBLE);
        else {
            holder.img.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(allNews.get(position).getImgURL())
                    .into(holder.img);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mArticleClickListener != null) {
                    mArticleClickListener.onClick(allNews.get(position).getFullArticleURL(), allNews.get(position).getTitle());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return allNews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDate, tvCap;
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_article_short_title);
            tvDate = (TextView) itemView.findViewById(R.id.tv_article_short_date);
            tvCap = (TextView) itemView.findViewById(R.id.tv_article_short_cap);
            img = (ImageView) itemView.findViewById(R.id.img_view_article_short);
        }
    }
}
