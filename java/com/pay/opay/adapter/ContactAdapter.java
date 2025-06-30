package com.pay.opay.adapter;

import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pay.opay.AccountInfo;
import com.pay.opay.R;
import com.pay.opay.database.Contact;
import com.pay.opay.straighttodeposit;

import java.util.List;
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private final List<Contact> contactList;
    public ContactAdapter(List<Contact> contactList) {
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
        Contact c = contactList.get(position);

        holder.tvName.setText(c.name);
        holder.tvPhone.setText(c.phone);


        try {
            holder.imageView.setImageResource(R.mipmap.profile_image);
        } catch (Resources.NotFoundException e) {
            holder.imageView.setImageResource(c.imageResId);
        }

        holder.itemView.setOnClickListener(v -> {
            AccountInfo.getInstance().setUserAccount(c.name);
            AccountInfo.getInstance().setUserNumber(c.phone);
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