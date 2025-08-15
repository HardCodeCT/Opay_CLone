package com.pay.opay.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pay.opay.AccountInfo.AccountInfo;
import com.pay.opay.BankData;
import com.pay.opay.BankItem;
import com.pay.opay.R;
import java.util.List;

public class BankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<BankItem> itemList;
    private final BankData bankData = BankData.getInstance();
    private final AccountInfo accountInfo = AccountInfo.getInstance();
    private String query = "";
    public interface OnDataChangedListener {
        void onChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(BankItem item);
    }

    private OnDataChangedListener dataChangedListener;
    private OnItemClickListener itemClickListener;

    public BankAdapter(List<BankItem> itemList) {
        this.itemList = itemList;
    }

    public void setOnDataChangedListener(OnDataChangedListener listener) {
        this.dataChangedListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void updateList(List<BankItem> newList) {
        itemList.clear();
        itemList.addAll(newList);
        notifyDataSetChanged();

        if (dataChangedListener != null) {
            dataChangedListener.onChanged();
        }
    }

    public void setQuery(String query) {
        this.query = query != null ? query.toLowerCase() : "";
        notifyDataSetChanged();
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BankItem item = itemList.get(position);

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).textHeader.setText(item.getName());
        } else {
            BankViewHolder bankHolder = (BankViewHolder) holder;

            String name = item.getName() != null ? item.getName() : "";
            SpannableString spannableName = new SpannableString(name);

            if (!query.isEmpty()) {
                int index = name.toLowerCase().indexOf(query);
                if (index >= 0) {
                    spannableName.setSpan(
                            new ForegroundColorSpan(Color.parseColor("#00b875")),
                            index,
                            index + query.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                }
            }

            bankHolder.textBankName.setText(spannableName);
            bankHolder.imageBankLogo.setImageResource(item.getLogoResId());

            bankHolder.itemView.setOnClickListener(v -> {
                handleBankSelected(item.getName() , item.getBankCode(), item.getName(), item.getLogoResId(), v.getContext());

                if (itemClickListener != null) {
                    itemClickListener.onItemClick(item);
                }
            });
        }
    }


    private void handleBankSelected(String userbank, String code, String fullName, int imageRes, Context context) {
        //String firstWord = fullName.contains(" ") ? fullName.substring(0, fullName.indexOf(" ")) : fullName;
        bankData.setBankCode(code);
        bankData.setBankName(fullName);
        bankData.setBankImage(imageRes);
        accountInfo.setBankUpdate(4);
        accountInfo.setUserBank(userbank);
        accountInfo.setActivebank(imageRes);
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
