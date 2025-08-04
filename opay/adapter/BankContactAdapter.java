package com.pay.opay.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pay.opay.AccountInfo;
import com.pay.opay.R;
import com.pay.opay.database.BankName;
import com.pay.opay.straighttodeposit;

import java.util.List;

public class BankContactAdapter extends RecyclerView.Adapter<BankContactAdapter.ContactViewHolder> {
    private final List<BankName> contactList;

    public BankContactAdapter(List<BankName> contactList) {
        this.contactList = contactList;
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone;
        ImageView imageView;

        public ContactViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        BankName c = contactList.get(position);

        holder.tvName.setText(c.getAccountName());
        holder.tvPhone.setText(c.getBankNumber());
        holder.imageView.setImageResource(c.getImageName());

        holder.itemView.setOnClickListener(v -> {
            AccountInfo.getInstance().setUserAccount(c.getBankName());
            AccountInfo.getInstance().setUserNumber(c.getBankNumber());
            AccountInfo.getInstance().setUserBank("Access Bank");
            AccountInfo.getInstance().setRootAccount("ODOEGBULAM THANKGOD CHIGOZIE");
            AccountInfo.getInstance().setRootNumber("8165713623");
            AccountInfo.getInstance().setRootBank("OPay");

            Intent intent = new Intent(v.getContext(), straighttodeposit.class);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
