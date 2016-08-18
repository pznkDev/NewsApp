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

public class ShortArticleListAdapter extends RecyclerView.Adapter<ShortArticleListAdapter.ViewHolder>{

    List<ArticleShort> allNews = new ArrayList<ArticleShort>();
    Context context;

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(allNews.get(position).getTitle());
        holder.tvDate.setText(allNews.get(position).getDate());
        if (allNews.get(position).getImgURL() != "") {
            Picasso.with(context)
                    .load(allNews.get(position).getImgURL())
                    .into(holder.img);
        }
    }

    @Override
    public int getItemCount() {
        return allNews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDate;
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_article_short_title);
            tvDate = (TextView) itemView.findViewById(R.id.tv_article_short_date);
            img = (ImageView) itemView.findViewById(R.id.img_view_article_short);
        }
    }
}