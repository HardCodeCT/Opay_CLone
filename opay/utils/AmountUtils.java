package com.pay.opay.utils;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pay.opay.viewmodel.AmountViewModel;

import java.text.NumberFormat;
import java.util.Locale;

public class AmountUtils {

    /**
     * Fragment-safe version that gets Application from Fragment
     * and shows a Toast
     */
    @NonNull
    public static String getFormattedAmount(@NonNull Fragment fragment) {
        Application application = fragment.requireActivity().getApplication();
        String result = getFormattedAmount(application);

        // Show toast directly
        Toast.makeText(fragment.requireContext(), result, Toast.LENGTH_SHORT).show();

        return result;
    }

    /**
     * Original version with Application parameter
     * and shows a Toast if application is not null
     */
    @NonNull
    public static String getFormattedAmount(@Nullable Application application) {
        if (application == null) {
            return "0";
        }

        try {
            AmountViewModel viewModel = new ViewModelProvider
                    .AndroidViewModelFactory(application)
                    .create(AmountViewModel.class);

            Integer amount = viewModel.getAmountValue().getValue();
            String result = amount != null
                    ? NumberFormat.getNumberInstance(Locale.US).format(amount)
                    : "0";

            // Show toast (need a Context, so we use application here)
            Toast.makeText(application.getApplicationContext(), result, Toast.LENGTH_SHORT).show();

            return result;
        } catch (Exception e) {
            Toast.makeText(application.getApplicationContext(), "0", Toast.LENGTH_SHORT).show();
            return "0";
        }
    }

    /**
     * Parse formatted amount back into integer
     */
    public static int parseFormattedAmount(String formattedAmount) {
        try {
            return Integer.parseInt(formattedAmount.replaceAll("[^\\d]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
