package com.pay.opay.recyclerheightadjuster;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerHeightAdjuster {

    private final RecyclerView recyclerView;
    private final RecyclerView.Adapter adapter;
    private final Context context;

    public RecyclerHeightAdjuster(Context context, RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
    }

    public void setupDynamicHeight() {
        if (adapter == null) return;

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                adjustHeight();
            }
        });

        recyclerView.post(this::adjustHeight);
    }

    private void adjustHeight() {
        int maxHeight = dpToPx(200);
        int totalHeight = 0;

        int itemCount = adapter.getItemCount();
        int bufferDp = itemCount >= 2 ? 20 : (itemCount == 1 ? 15 : 0);

        for (int i = 0; i < itemCount; i++) {
            RecyclerView.ViewHolder holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i));
            adapter.onBindViewHolder(holder, i);

            holder.itemView.measure(
                    View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.UNSPECIFIED
            );
            totalHeight += holder.itemView.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = Math.min(totalHeight + dpToPx(bufferDp), maxHeight);
        recyclerView.setLayoutParams(params);
    }

    private int dpToPx(int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}
