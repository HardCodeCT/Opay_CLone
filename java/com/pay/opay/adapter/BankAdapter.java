package com.pay.opay.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import com.pay.opay.AccountInfo;
import com.pay.opay.BankData;
import com.pay.opay.BankItem;
import com.pay.opay.R;

import java.util.List;

public class BankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<BankItem> itemList;
    private final BankData bankData = new BankData();
    AccountInfo accountInfo = AccountInfo.getInstance();
    public BankAdapter(List<BankItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == BankItem.TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_bank, parent, false);
            return new BankViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder, int position) {

        BankItem item = itemList.get(position);

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).textHeader.setText(item.getName());
        } else {
            BankViewHolder bankHolder = (BankViewHolder) holder;
            bankHolder.textBankName.setText(item.getName());
            bankHolder.imageBankLogo.setImageResource(item.getLogoResId());

            bankHolder.itemView.setOnClickListener(v -> {
                String name = item.getName();
                String code = item.getBankCode();
                Integer imagge = item.getLogoResId();
                handleBankSelected(code, name, imagge);
            });
        }
    }

    private void handleBankSelected(String code, String fullName, Integer imagge) {
        String firstWord = fullName.contains(" ") ? fullName.substring(0, fullName.indexOf(" ")) : fullName;
        bankData.setBankCode(code);
        bankData.setBankName(firstWord);
        bankData.setBankImage(imagge);
        accountInfo.setBankUpdate(4);
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView textHeader;

        HeaderViewHolder(View itemView) {
            super(itemView);
            textHeader = itemView.findViewById(R.id.textHeader);
        }
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