package com.prashantchanne.chatbox;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class PageViewAdapter extends FragmentPagerAdapter {
    public PageViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0 : ProfileFragmnet profileFragmnet = new ProfileFragmnet();
                return profileFragmnet;

            case 1 : UsersFragment usersFragment = new UsersFragment();
                return usersFragment;

            case 2 : NotificationFragment notificationFragment = new NotificationFragment();
                return notificationFragment;

            case 3 : NotesFragment notesFragment = new NotesFragment();
                return  notesFragment;

            default: return null;

        }


    }

    @Override
    public int getCount() {
        return 4;  // Total number of tabs
    }
}
