package com.pay.opay.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pay.opay.AccountInfo.AccountInfo;
import com.pay.opay.R;
import com.pay.opay.database.BankName;
import com.pay.opay.straighttodeposit;
import com.pay.opay.utils.LoaderHelper;
import java.util.List;

public class BankContactSearchAdapter extends RecyclerView.Adapter<BankContactSearchAdapter.ContactViewHolder> {

    private final List<BankName> contactList;
    private final int loaderId;
    private final int progressBarId;
    private static final long ROTATE_DURATION = 30000; // rotate for 3 seconds
    private static final long TIMEOUT_DURATION = 2000; // 3 seconds delay before next action

    public BankContactSearchAdapter(List<BankName> contactList, int loaderId, int progressBarId) {
        this.contactList = contactList;
        this.loaderId = loaderId;
        this.progressBarId = progressBarId;
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone, tvBankname;
        ImageView imageView;

        public ContactViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            imageView = itemView.findViewById(R.id.imageView);
            tvBankname = itemView.findViewById(R.id.tvBankname);
        }
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recent_search_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        BankName c = contactList.get(position);

        holder.tvName.setText(c.getAccountName());
        holder.tvPhone.setText(c.getBankNumber());
        holder.tvBankname.setText(c.getBankName());
        holder.imageView.setImageResource(c.getImageName());

        holder.itemView.setOnClickListener(v -> {
            View loaderView = v.getRootView().findViewById(loaderId);
            View progressBarView = v.getRootView().findViewById(progressBarId);

            LoaderHelper.startLoaderRotation(loaderView, progressBarView, () -> {
                AccountInfo.getInstance().setUserAccount(c.getAccountName());
                AccountInfo.getInstance().setUserNumber(c.getBankNumber());
                AccountInfo.getInstance().setUserBank(c.getBankName());
                AccountInfo.getInstance().setActivebank(c.getImageName());
                AccountInfo.getInstance().setRootAccount("ODOEGBULAM THANKGOD CHIGOZIE");
                AccountInfo.getInstance().setRootNumber("8165713623");
                AccountInfo.getInstance().setRootBank("OPay");

                Intent intent = new Intent(v.getContext(), straighttodeposit.class);
                v.getContext().startActivity(intent);
            });
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
