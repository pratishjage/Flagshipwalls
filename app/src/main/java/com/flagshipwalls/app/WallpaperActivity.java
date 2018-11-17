package com.flagshipwalls.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.flagshipwalls.app.Fragments.DevicesFragment;
import com.flagshipwalls.app.Fragments.HomeFragment;
import com.flagshipwalls.app.Fragments.OSFragment;
import com.flagshipwalls.app.Fragments.QueryWallpaperFrgment;
import com.flagshipwalls.app.interfaces.IWallpaperActivity;
import com.flagshipwalls.app.utils.FragmentTag;
import com.flagshipwalls.app.utils.AppConstants;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WallpaperActivity extends AppCompatActivity implements IWallpaperActivity {


    private ArrayList<String> mfragmentTags = new ArrayList<>();
    private ArrayList<FragmentTag> mFragments = new ArrayList<>();
    private int mExitCount = 0;

    private HomeFragment homeFragment;
    private OSFragment osFragment;
    private DevicesFragment devicesFragment;
    private QueryWallpaperFrgment queryWallpaperFrgment;
    private ImageView backArrow, logoImageview;
    TextView headerText;
    String header;
    private InterstitialAd mInterstitialAd;
    FirebaseFirestore db;
    Map<String, Object> fcmData;
    String TAG = getClass().getSimpleName();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mfragmentTags.clear();
                    mfragmentTags = new ArrayList<>();
                    init();

                    return true;
                case R.id.navigation_dashboard:

                    if (osFragment == null) {
                        osFragment = OSFragment.newInstance();
                        fragmentTransaction.add(R.id.main_container, osFragment, getString(R.string.tag_fragment_os));
                        fragmentTransaction.commit();
                        mfragmentTags.add(getString(R.string.tag_fragment_os));
                        mFragments.add(new FragmentTag(osFragment, getString(R.string.tag_fragment_os)));
                    } else {

                        mfragmentTags.remove(getString(R.string.tag_fragment_os));
                        mfragmentTags.add(getString(R.string.tag_fragment_os));

                    }
                    setFragmentVisible(getString(R.string.tag_fragment_os));

                    return true;
                case R.id.navigation_notifications:

                    if (devicesFragment == null) {
                        devicesFragment = DevicesFragment.newInstance();
                        fragmentTransaction.add(R.id.main_container, devicesFragment, getString(R.string.tag_fragment_devices));
                        fragmentTransaction.commit();
                        mfragmentTags.add(getString(R.string.tag_fragment_devices));
                        mFragments.add(new FragmentTag(devicesFragment, getString(R.string.tag_fragment_devices)));
                    } else {

                        mfragmentTags.remove(getString(R.string.tag_fragment_devices));
                        mfragmentTags.add(getString(R.string.tag_fragment_devices));

                    }

                    setFragmentVisible(getString(R.string.tag_fragment_devices));

                    return true;
            }
            return false;
        }
    };
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View view = getSupportActionBar().getCustomView();
        backArrow = view.findViewById(R.id.back_arrow);
        headerText = view.findViewById(R.id.header_txt);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        logoImageview = view.findViewById(R.id.logo_image);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fcmData = new HashMap<>();
        db = FirebaseFirestore.getInstance();
        getFcmToken();
        init();
        setUpAd();

    }

    private void getFcmToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                if (!task.isSuccessful()) {
                    Log.w(TAG, "getInstanceId failed", task.getException());
                    return;
                }
                // Get new Instance ID token
                String token = task.getResult().getToken();
                String instanceId = task.getResult().getId();
                Log.d(TAG, "onCompleteFCM:" + token);
                String android_id = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                fcmData.clear();
                fcmData.put("token", token);
                fcmData.put("instanceid", instanceId);
                fcmData.put("deviceid", android_id);
                fcmData.put("osversion", android.os.Build.VERSION.SDK_INT);
                fcmData.put("created_at", FieldValue.serverTimestamp());


                db.collection("users").document(android_id).set(fcmData, SetOptions.merge());

            }
        });
    }

    private void setUpAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
    }

    private void init() {
        if (homeFragment == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            homeFragment = HomeFragment.newInstance();
            fragmentTransaction.add(R.id.main_container, homeFragment, getString(R.string.tag_fragment_home));
            fragmentTransaction.commit();
            mfragmentTags.add(getString(R.string.tag_fragment_home));
            mFragments.add(new FragmentTag(homeFragment, getString(R.string.tag_fragment_home)));
        } else {

            mfragmentTags.remove(getString(R.string.tag_fragment_home));
            mfragmentTags.add(getString(R.string.tag_fragment_home));

        }
        setFragmentVisible(getString(R.string.tag_fragment_home));
    }

    private void setFragmentVisible(String tagName) {
        if (tagName.equals(getString(R.string.tag_fragment_home))) {
            showBottomNavigation();
            showLogoInFragment();
        } else if (tagName.equals(getString(R.string.tag_fragment_devices))) {
            showBottomNavigation();
            showLogoInFragment();
        } else if (tagName.equals(getString(R.string.tag_fragment_os))) {
            showBottomNavigation();
            showLogoInFragment();
        }
        if (tagName.equals(getString(R.string.tag_fragment_query_wallpaper))) {
            hideBottomNavigation();
            showHeaderInFreagment();
        }

        for (int i = 0; i < mFragments.size(); i++) {
            if (tagName.equals(mFragments.get(i).getTag())) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.show(mFragments.get(i).getFragment()).commit();

            } else {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(mFragments.get(i).getFragment()).commit();

            }
        }
        setNavigationIcon(tagName);
    }

    @Override
    public void onBackPressed() {
        int backsatckCount = mfragmentTags.size();
        if (backsatckCount > 1) {
            String topfragmentTag = mfragmentTags.get(mfragmentTags.size() - 1);
            String newTopfragmentTag = mfragmentTags.get(mfragmentTags.size() - 2);
            setFragmentVisible(newTopfragmentTag);
            mfragmentTags.remove(topfragmentTag);
            mExitCount = 0;
        } else if (backsatckCount == 1) {
            mExitCount++;
            Toast.makeText(this, "One more time", Toast.LENGTH_SHORT).show();
        }

        if (mExitCount >= 2) {
            super.onBackPressed();
        }
    }


    private void setNavigationIcon(String tagName) {
        Menu menu = navigation.getMenu();
        MenuItem menuItem = null;

        if (tagName.equals(getString(R.string.tag_fragment_home))) {
            menuItem = menu.getItem(0);
            menuItem.setChecked(true);
        } else if (tagName.equals(getString(R.string.tag_fragment_os))) {
            menuItem = menu.getItem(1);
            menuItem.setChecked(true);
        } else if (tagName.equals(getString(R.string.tag_fragment_devices))) {
            menuItem = menu.getItem(2);
            menuItem.setChecked(true);
        }
    }

    @Override
    public void inflateQueryWallpaperFragment(String whereTag, String whereValue) {
        header = whereValue;
        if (queryWallpaperFrgment != null) {
            getSupportFragmentManager().beginTransaction().remove(queryWallpaperFrgment).commitAllowingStateLoss();
        }
        queryWallpaperFrgment = new QueryWallpaperFrgment();
        Bundle args = new Bundle();
        args.putString(AppConstants.INTENT_WHERE_TAG, whereTag);
        args.putString(AppConstants.INTENT_WHERE_VALUE, whereValue);
        queryWallpaperFrgment.setArguments(args);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, queryWallpaperFrgment, getString(R.string.tag_fragment_query_wallpaper));
        fragmentTransaction.commit();
        mfragmentTags.add(getString(R.string.tag_fragment_query_wallpaper));

        mFragments.add(new FragmentTag(queryWallpaperFrgment, getString(R.string.tag_fragment_query_wallpaper)));
        setFragmentVisible(getString(R.string.tag_fragment_query_wallpaper));
    }

    @Override
    public void showSetWallpaperActivity(String wallurl, String downloadurl) {

        Intent intent = new Intent(WallpaperActivity.this, SetWallpaperActivity.class);
        intent.putExtra(AppConstants.WALLURL, wallurl);
        intent.putExtra(AppConstants.DOWNLOAD_URL, downloadurl);
        startActivityIfNeeded(intent, 111);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "onActivityResult: Wallpaperset");
                Toast.makeText(getApplicationContext(), "Wallpaper Set", Toast.LENGTH_SHORT).show();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        }
    }

    private void hideBottomNavigation() {
        if (navigation != null) {
            navigation.setVisibility(View.GONE);
        }
    }

    private void showBottomNavigation() {
        if (navigation != null) {
            navigation.setVisibility(View.VISIBLE);
        }
    }

    private void showHeaderInFreagment() {
        logoImageview.setVisibility(View.GONE);
        headerText.setVisibility(View.VISIBLE);
        Log.d(TAG, "showHeaderInFreagment: " + header);
        headerText.setText(header);
        backArrow.setVisibility(View.VISIBLE);
    }

    private void showLogoInFragment() {
        logoImageview.setVisibility(View.GONE);
        headerText.setVisibility(View.VISIBLE);
        header = "Flagship Walls";
        headerText.setText(header);

      /*  logoImageview.setVisibility(View.VISIBLE);
        headerText.setVisibility(View.GONE);
        headerText.setText(header);*/
        backArrow.setVisibility(View.GONE);
    }

}
