package com.pnuppp.pplusplus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HtmlNoticeAdapter extends RecyclerView.Adapter<HtmlNoticeAdapter.ViewHolder> {

    private List<HtmlItem> htmlItems;
    private Context context;

    public HtmlNoticeAdapter(List<HtmlItem> htmlItems, Context context) {
        this.htmlItems = htmlItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notice_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HtmlItem item = htmlItems.get(position);
        holder.titleTextView.setText(item.getTitle());
        holder.dateTextView.setText(item.getDate());
        // 필요시 다른 필드들도 설정 가능
        // 예: holder.numberTextView.setText(item.getNumber());
    }

    @Override
    public int getItemCount() {
        return htmlItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView dateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}
