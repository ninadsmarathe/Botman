package ai.api.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class NavigationDrawerFragment extends Fragment {

    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private RecyclerView mRecyclerDrawer;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View mContainer;
    private boolean mDrawerOpened = false;
    private CustomAdapters mAdapter;
    List<Information> data= Collections.emptyList();



    public NavigationDrawerFragment() {
        // Required empty public constructor
    }
    public List<Information> getData() {
        //load only static data inside a drawer
        List<Information> data = new ArrayList<>();
        int[] icons = {R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,};
        String[] titles = {"one","two","three"};
        for (int i = 0; i < 3; i++) {
            Information information = new Information(icons[i],titles[i]);

            data.add(information);
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = MyApplication.readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, false);
        mFromSavedInstanceState = savedInstanceState != null ? true : false;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_navigation_drawer, container, false);


        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerDrawer = (RecyclerView) view.findViewById(R.id.drawerList);
        mAdapter = new CustomAdapters(getActivity(), getData());
        mRecyclerDrawer.setAdapter(mAdapter);
        mRecyclerDrawer.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout,  Toolbar toolbar) {
        mContainer = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d("VIVZ", "onDrawerOpened");
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    MyApplication.saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer);
                }
                getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d("VIVZ", "onDrawerClosed");
                getActivity().supportInvalidateOptionsMenu();
            }


        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
                if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
                    mDrawerLayout.openDrawer(mContainer);
                }
            }
        });


    }
}
