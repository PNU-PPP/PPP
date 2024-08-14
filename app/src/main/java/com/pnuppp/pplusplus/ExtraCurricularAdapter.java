package com.pnuppp.pplusplus;

import android.content.Context;
import android.content.Intent; // 추가된 import 문
import android.net.Uri; // 추가된 import 문
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExtraCurricularAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RSSItem> rssItems;
    private Context context;

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    public ExtraCurricularAdapter(List<RSSItem> rssItems, Context context) {
        this.rssItems = rssItems;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.extra_curricular_item, parent, false);
            return new ItemViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.loading_item, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            if (position == 0) {
                float dp = context.getResources().getDisplayMetrics().density + 0.5f;
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) itemViewHolder.cardView.getLayoutParams();
                layoutParams.topMargin = (int) (8 * dp);
                itemViewHolder.cardView.setLayoutParams(layoutParams);
            }
            RSSItem item = rssItems.get(position);
            itemViewHolder.titleTextView.setText(item.getTitle());
            itemViewHolder.dateTextView.setText(item.getCategory());
            itemViewHolder.authorTextView.setText(item.getAuthor());
            itemViewHolder.itemView.setOnClickListener(v -> {
                // 상세 보기 동작 구현
                String url = item.getLink();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return rssItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return rssItems.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView titleTextView;
        public TextView dateTextView;
        public TextView authorTextView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            titleTextView = itemView.findViewById(R.id.tvTitle);
            dateTextView = itemView.findViewById(R.id.tvDate);
            authorTextView = itemView.findViewById(R.id.tvAuthor);
        }
    }
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
