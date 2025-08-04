package com.pay.opay.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
    private String query = "";

    public AccountAdapter(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public interface OnDataChangedListener {
        void onChanged();
    }

    public void setOnDataChangedListener(OnDataChangedListener listener) {
        this.dataChangedListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Contact contact);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setQuery(String query) {
        this.query = query != null ? query.toLowerCase() : "";
        notifyDataSetChanged();
    }

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
        String name = contact.getName();
        String phone = contact.getPhone();

        SpannableString spannableName = new SpannableString(name);
        SpannableString spannablePhone;

        // Format phone number (for display)
        String formatted = phone.length() == 10
                ? phone.substring(0, 3) + " " + phone.substring(3, 6) + " " + phone.substring(6)
                : phone;
        spannablePhone = new SpannableString(formatted);

        if (!query.isEmpty()) {
            int nameIndex = name.toLowerCase().indexOf(query);
            if (nameIndex >= 0) {
                spannableName.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#00b875")),
                        nameIndex, nameIndex + query.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }

            String plainPhone = phone; // unformatted
            int phoneIndex = plainPhone.indexOf(query);
            if (phoneIndex >= 0 && phone.length() == 10) {
                // Adjust highlight range for formatted phone number
                int[] map = {0, 1, 2, 4, 5, 6, 8, 9, 10, 11}; // Mapping from raw to formatted positions
                int start = map[phoneIndex];
                int end = (phoneIndex + query.length() <= 10) ? map[phoneIndex + query.length() - 1] + 1 : formatted.length();
                spannablePhone.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#00b875")),
                        start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
        }

        holder.name.setText(spannableName);
        holder.phone.setText(spannablePhone);
        holder.image.setImageResource(contact.getImageId());

        holder.itemView.setOnClickListener(v -> {
            AccountInfo.getInstance().setUserAccount(name);
            AccountInfo.getInstance().setUserNumber(phone);
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
