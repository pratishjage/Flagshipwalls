package com.flagshipwalls.app.utils;

import androidx.fragment.app.Fragment;

public class FragmentTag {
    Fragment fragment;
    String Tag;

    public FragmentTag(Fragment fragment, String tag) {
        this.fragment = fragment;
        Tag = tag;
    }

    public FragmentTag() {
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }
}
