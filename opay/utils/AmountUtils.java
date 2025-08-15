package com.pay.opay.utils;

import android.app.Application;
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
     */
    @NonNull
    public static String getFormattedAmount(@NonNull Fragment fragment) {
        Application application = fragment.requireActivity().getApplication();
        return getFormattedAmount(application);
    }

    /**
     * Original version with Application parameter
     */
    @NonNull
    public static String getFormattedAmount(@Nullable Application application) {
        if (application == null) {
            return "0";
        }

        try {
            AmountViewModel viewModel = new ViewModelProvider.AndroidViewModelFactory(application)
                    .create(AmountViewModel.class);

            Integer amount = viewModel.getAmountValue().getValue();
            return amount != null ?
                    NumberFormat.getNumberInstance(Locale.US).format(amount) : "0";
        } catch (Exception e) {
            return "0";
        }
    }

    // Keep your existing parseFormattedAmount method
    public static int parseFormattedAmount(String formattedAmount) {
        try {
            return Integer.parseInt(formattedAmount.replaceAll("[^\\d]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}