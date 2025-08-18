package com.pay.opay.adapter;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pay.opay.AccountInfo.AccountInfo;
import com.pay.opay.BankData;
import com.pay.opay.SearchBankItem;
import com.pay.opay.R;
import java.util.ArrayList;
import java.util.List;

public class BankSearchAdapter extends RecyclerView.Adapter<BankSearchAdapter.BankViewHolder> {
    private final List<SearchBankItem> itemList;
    private String query = "";
    private final BankData bankData = BankData.getInstance();
    private final AccountInfo accountInfo = AccountInfo.getInstance();

    public interface OnItemClickListener {
        void onItemClick(SearchBankItem item);
    }

    private OnItemClickListener itemClickListener;

    public BankSearchAdapter(List<SearchBankItem> itemList) {
        this.itemList = itemList != null ? itemList : new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void updateList(List<SearchBankItem> newList) {
        itemList.clear();
        if (newList != null) {
            itemList.addAll(newList);
            Log.d("BankSearchAdapter", "Updated list size: " + itemList.size());
            for (SearchBankItem item : itemList) {
                Log.d("BankSearchAdapter", "Item: " + item.getBankName());
            }
        }
        notifyDataSetChanged();
    }

    public void setQuery(String query) {
        this.query = query != null ? query.trim().toLowerCase() : "";
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bank, parent, false);
        return new BankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BankViewHolder holder, int position) {
        SearchBankItem item = itemList.get(position);
        String name = item.getBankName() != null ? item.getBankName() : "";
        SpannableString spannableName = new SpannableString(name);
        if (!query.isEmpty()) {
            int index = name.toLowerCase().indexOf(query);
            if (index >= 0) {
                spannableName.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#00b875")), // Consider R.color.highlight_color
                        index,
                        index + query.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
        }
        holder.textBankName.setText(spannableName);
        holder.imageBankLogo.setImageResource(item.getBankImage());
        holder.itemView.setOnClickListener(v -> {
            handleBankSelected(item.getBankName(), item.getBankCode(), item.getBankName(), item.getBankImage());
            if (itemClickListener != null) {
                itemClickListener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private void handleBankSelected(String userbank, String code, String fullName, int imageRes) {
        bankData.setBankCode(code);
        bankData.setBankName(fullName);
        bankData.setBankImage(imageRes);
        accountInfo.setBankUpdate(4);
        accountInfo.setUserBank(userbank);
        accountInfo.setActivebank(imageRes);
    }

    static class BankViewHolder extends RecyclerView.ViewHolder {
        TextView textBankName;
        ImageView imageBankLogo;

        BankViewHolder(View itemView) {
            super(itemView);
            textBankName = itemView.findViewById(R.id.textBankName);
            imageBankLogo = itemView.findViewById(R.id.imageBankLogo);
        }
    }
}