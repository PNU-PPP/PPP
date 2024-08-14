package com.pnuppp.pplusplus;

import android.content.Context;
import android.content.Intent; // 추가된 import 문
import android.net.Uri; // 추가된 import 문
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExtraCurricularAdapter extends RecyclerView.Adapter<ExtraCurricularAdapter.ViewHolder> {

    private List<RSSItem> rssItems;
    private Context context;

    public ExtraCurricularAdapter(List<RSSItem> rssItems, Context context) {
        this.rssItems = rssItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.extra_curricular_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RSSItem item = rssItems.get(position);
        holder.titleTextView.setText(item.getTitle());
        holder.dateTextView.setText(item.getCategory());
        holder.authorTextView.setText(item.getAuthor());
        holder.itemView.setOnClickListener(v -> {
            // 상세 보기 동작 구현
            String url = item.getLink();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return rssItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView dateTextView;
        public TextView authorTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tvTitle);
            dateTextView = itemView.findViewById(R.id.tvDate);
            authorTextView = itemView.findViewById(R.id.tvAuthor);
        }
    }
}
