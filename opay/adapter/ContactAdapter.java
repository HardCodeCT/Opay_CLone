package com.pay.opay.adapter;

import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        String raw = c.phone;

        // Format 10-digit number like 081 023 7363
        String formatted = raw.length() == 10
                ? raw.substring(0, 3) + " " + raw.substring(3, 6) + " " + raw.substring(6)
                : raw;

        holder.tvPhone.setText(formatted);

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