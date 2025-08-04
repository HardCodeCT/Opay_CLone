package com.pay.opay.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pay.opay.R;
import com.pay.opay.TransactionModel;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<TransactionModel> transactions;

    public TransactionAdapter(List<TransactionModel> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionModel model = transactions.get(position);
        holder.transimage.setImageResource(R.mipmap.greenarrow);
        holder.user.setText(model.getUser());
        holder.date.setText(model.getDateTime());
        holder.amount.setText(model.getAmount());
        holder.status.setText(model.getStatus());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView user, date, amount, status;
        ImageView transimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            transimage = itemView.findViewById(R.id.transimage);
            user = itemView.findViewById(R.id.user2);
            date = itemView.findViewById(R.id.datetime2);
            amount = itemView.findViewById(R.id.price2);
            status = itemView.findViewById(R.id.status2); // Make sure you give the Status TextView an id in XML
        }
    }
}

