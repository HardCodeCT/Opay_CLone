package com.pay.opay.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pay.opay.AccountInfo;
import com.pay.opay.R;
import com.pay.opay.database.Contact;
import com.pay.opay.straighttodeposit;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    private List<Contact> contactList;
    private OnDataChangedListener dataChangedListener;
    private OnItemClickListener itemClickListener;

    // Constructor
    public AccountAdapter(List<Contact> contactList) {
        this.contactList = contactList;
    }

    // Data change interface
    public interface OnDataChangedListener {
        void onChanged();
    }

    public void setOnDataChangedListener(OnDataChangedListener listener) {
        this.dataChangedListener = listener;
    }

    // Item click interface
    public interface OnItemClickListener {
        void onItemClick(Contact contact);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    // Update the list and notify
    public void updateList(List<Contact> newList) {
        contactList.clear();
        contactList.addAll(newList);
        notifyDataSetChanged();

        if (dataChangedListener != null) {
            dataChangedListener.onChanged();
        }
    }

    @Override
    public AccountAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccountAdapter.ViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.name.setText(contact.getName());
        holder.phone.setText(contact.getPhone());
        holder.image.setImageResource(contact.getImageId());

        holder.itemView.setOnClickListener(v -> {
            AccountInfo.getInstance().setUserAccount(holder.name.getText().toString());
            AccountInfo.getInstance().setUserNumber(holder.phone.getText().toString());
            AccountInfo.getInstance().setUserBank("Opay");
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone;
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            phone = itemView.findViewById(R.id.tvPhone);
            image = itemView.findViewById(R.id.imageView);
        }
    }
}
