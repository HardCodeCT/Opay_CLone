package com.pay.opay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OfferCardAdapter extends RecyclerView.Adapter<OfferCardAdapter.OfferViewHolder> {

    private final List<OfferModel> offers;

    public OfferCardAdapter(List<OfferModel> offers) {
        this.offers = offers;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offer_card, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        OfferModel offer = offers.get(position);
        holder.title.setText(offer.title);
        holder.subtitle.setText(offer.subtitle);
        holder.image.setImageResource(offer.imageRes);
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    static class OfferViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        ImageView image;

        OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleText);
            subtitle = itemView.findViewById(R.id.subtitleText);
            image = itemView.findViewById(R.id.imageView); // add ID in XML if needed
        }
    }
}

