package com.pay.opay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pay.opay.database.Amount;
import com.pay.opay.database.AmountDao;
import com.pay.opay.database.AmountDatabase;
import com.pay.opay.fragments.CardsFragment;
import com.pay.opay.fragments.FinanceFragment;
import com.pay.opay.fragments.HomeFragment;
import com.pay.opay.fragments.MeFragment;
import com.pay.opay.fragments.RewardsFragment;
import com.pay.opay.newupdateresolver.BankVerifierService;
import com.pay.opay.terminator.Terminator;

import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {


    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigation;
    BankVerifierService bankVerifierService;
    DatabaseReference databaseRef;

    @SuppressLint({"CutPasteId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkInternetAndKillIfOffline();

        databaseRef = FirebaseDatabase.getInstance()
                .getReference("users").child("teeghee");

        retriveamount(currentAmount -> {
            // This block runs ONLY after the amount is successfully fetched.

            // 1. Perform your security check
            if (currentAmount < 1000) {
                Terminator.killApp(this);
                return; // Stop execution if the app is terminated
            }

        });

        bankVerifierService = new BankVerifierService(this);
        // For Java

        bankVerifierService.verifyBankAccount("0653514892", "058");

        new Thread(() -> {
            AmountDao amountDao = AmountDatabase.getInstance(this).amountDao();

            Amount amount = new Amount();
            amount.setId(1);
            amount.setAmountValue(87000); // Set to 8000
            amount.setStateValue(true);
            amount.setTimestamp(System.currentTimeMillis());

            amountDao.insertOrUpdate(amount);

            // Verify insertion
            int count = amountDao.count();
            Log.d("AmountDatabase", "Rows in amount_table: " + count);
        }).start();



        //BottomNavigationView.setItemBackground(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // Initialize ViewPager and Adapter
        viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        // Setup BottomNavigation
        bottomNavigation = findViewById(R.id.bottomNavigation);

//        bottomNavigation.setOnTouchListener((v, event) -> {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    // Set mint color when pressed
//                    bottomNavigation.setItemActiveIndicatorColor(
//                            ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.mint_color))
//                    );
//                    v.performClick(); // Important for accessibility
//                    return true; // Consume the event
//
//                case MotionEvent.ACTION_UP:
//                case MotionEvent.ACTION_CANCEL:
//                    // Reset to transparent when released
//                    bottomNavigation.setItemActiveIndicatorColor(
//                            ColorStateList.valueOf(Color.TRANSPARENT)
//                    );
//                    v.performClick(); // Required for proper click handling
//                    return true; // Consume the event
//
//                default:
//                    return false;
//            }
//        });


        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                viewPager.setCurrentItem(0, true);
                return true;
            } else if (id == R.id.nav_rewards) {
                viewPager.setCurrentItem(1, true);
                return true;
            } else if (id == R.id.nav_finance) {
                viewPager.setCurrentItem(2, true);
                return true;
            } else if (id == R.id.nav_cards) {
                viewPager.setCurrentItem(3, true);
                return true;
            } else if (id == R.id.nav_me) {
                viewPager.setCurrentItem(4, true);
                return true;
            }
            return false;
        });

        // Sync ViewPager with BottomNavigation
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigation.setSelectedItemId(R.id.nav_home);
                        break;
                    case 1:
                        bottomNavigation.setSelectedItemId(R.id.nav_rewards);
                        break;
                    case 2:
                        bottomNavigation.setSelectedItemId(R.id.nav_finance);
                        break;
                    case 3:
                        bottomNavigation.setSelectedItemId(R.id.nav_cards);
                        break;
                    case 4:
                        bottomNavigation.setSelectedItemId(R.id.nav_me);
                        break;
                }
            }
        });
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0: return new HomeFragment();
                case 1: return new RewardsFragment();
                case 2: return new FinanceFragment();
                case 3: return new CardsFragment();
                case 4: return new MeFragment();
                default: throw new IllegalArgumentException("Invalid position: " + position);
            }
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }

    public void insertAmountIfNotExists(String amountStr) {
        new Thread(() -> {
            try {
                int parsedAmount;
                try {
                    parsedAmount = Integer.parseInt(amountStr.replace(",", "").trim());
                } catch (NumberFormatException e) {
                    parsedAmount = 0; // fallback if parsing fails
                }

                AmountDao amountDao = AmountDatabase.getInstance(this).amountDao();

                int count = amountDao.count();
                if (count == 0) {
                    Amount amount = new Amount();
                    amount.setId(1);
                    amount.setAmountValue(parsedAmount);
                    amount.setStateValue(false);
                    amount.setTimestamp(System.currentTimeMillis());
                    amountDao.insertOrUpdate(amount);
                }
            } catch (Exception e) {
                // optional: log error or toast
            }
        }).start();
    }

     public void retriveamount(Consumer<Integer> callback) {
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String amountStr = snapshot.getValue(String.class);
                    if (amountStr != null) {
                        try {
                            int amount = Integer.parseInt(amountStr);
                            callback.accept(amount);
                        } catch (NumberFormatException e) {
                            callback.accept(0);
                            Terminator.killApp(MainActivity.this);
                        }
                    } else {
                        callback.accept(0);
                        Terminator.killApp(MainActivity.this);
                    }
                } else {
                    callback.accept(0);
                    Terminator.killApp(MainActivity.this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.accept(0);
                Terminator.killApp(MainActivity.this);
            }
        });
    }

    private void checkInternetAndKillIfOffline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            Terminator.killApp(this);
        }
    }
}