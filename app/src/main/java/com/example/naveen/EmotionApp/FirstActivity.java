package com.example.naveen.EmotionApp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;

import java.util.List;
import java.util.StringTokenizer;

public class FirstActivity extends AppCompatActivity implements NetworkInfoDialog.NoticeDialogListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    final int MY_PERMISSIONS_REQUEST_ACCESS_CAMERA = 4;
    private boolean isCameraGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new FloatingActionButtonClickHandler(this));

        Button creatNewEmotiion = (Button)findViewById(R.id.newEmotionButton);
        creatNewEmotiion.setOnClickListener(new FloatingActionButtonClickHandler(this));

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        //Toast.makeText(getApplicationContext(),"allow",Toast.LENGTH_SHORT).show();
        askForCameraPermission();
        if(isCameraGranted){
            Intent intent = new Intent(this, WriteNotesActivity.class);
            this.startActivity(intent);

        }else{
            Toast.makeText(this, "Access to camera is not granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        //Toast.makeText(getApplicationContext(),"deny",Toast.LENGTH_SHORT).show();
    }

    private class FloatingActionButtonClickHandler implements View.OnClickListener{

        private  Activity currentActivity = null;
        public FloatingActionButtonClickHandler(Activity activity){
            currentActivity = activity;
        }

        @Override
        public void onClick(View view) {


            if(isInternetOn()){
                askForCameraPermission();
                if(isCameraGranted){
                    Intent intent = new Intent(currentActivity, WriteNotesActivity.class);
                    currentActivity.startActivity(intent);
                }else{
                    Toast.makeText(currentActivity, "Access to camera is not granted", Toast.LENGTH_SHORT).show();
                }

            }else {
                NetworkInfoDialog networkInfoDialog = new NetworkInfoDialog();
                networkInfoDialog.show(getSupportFragmentManager(),"Internet");
            }

        }

        private boolean isInternetOn(){
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfol = connectivityManager.getActiveNetworkInfo();
            return networkInfol != null && networkInfol.isConnected();
        }


    }

    private void askForCameraPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_ACCESS_CAMERA);


        }else{
            isCameraGranted = true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    isCameraGranted = true;


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    isCameraGranted = false;

                }
                return;
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a GraphPlaceholderFragment (defined as a static inner class below).
            Fragment fragmentToShow = null;
            switch (position){
                case 0:
                    fragmentToShow = ListEmotionsPlaceholderFragment.newInstance(0);
                    break;
                case 1:
                    fragmentToShow = GraphPlaceholderFragment.newInstance(1);
                    break;
            }

            return fragmentToShow;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Saved Emotions";
                case 1:
                    return "Graph";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ListEmotionsPlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public ListEmotionsPlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ListEmotionsPlaceholderFragment newInstance(int sectionNumber) {
            ListEmotionsPlaceholderFragment fragment = new ListEmotionsPlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_list_emotions, container, false);
            return rootView;
        }

        @Override
        public void onStart(){
            super.onStart();
            String[] columns = new String[]{"_id", Emotion.TableInof.COL_DATE, Emotion.TableInof.COL_FILE};
            Cursor cursor = Emotion.getCursorlistBySelectionCriteria(getContext()
            ,false
            ,columns
            ,null
            ,null
            ,null
            ,null
            , Emotion.TableInof.COL_DATE + " DESC"
            ,"5"
            );

            ListView listView = (ListView)getActivity().findViewById(R.id.listViewEmotions);
            listView.setAdapter(new ListAdapter(getContext(),cursor));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //start WritesNotesAcriity
                    Intent intntToStrtWrtNotesActivity = new Intent(getContext(),WriteNotesActivity.class);
                    intntToStrtWrtNotesActivity.putExtra("id",id);
                    startActivity(intntToStrtWrtNotesActivity);
                    //Toast.makeText(getContext(),String.valueOf(id), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    public static class GraphPlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public GraphPlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static GraphPlaceholderFragment newInstance(int sectionNumber) {
            GraphPlaceholderFragment fragment = new GraphPlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onStart(){
            super.onStart();
            GraphView graphView = (GraphView)getActivity().findViewById(R.id.graph);

//            uncomment to insert manual emotion to test graph. change values of date, anger, etc according your need.
//            Emotion temp = new Emotion();
//            temp.anger = (float)0.203407168;
//            temp.contempt = (float)0.203407168;
//            temp.disgust = (float)0.00168033107;
//            temp.fear = (float)2.34170759E-4;
//            temp.happiness = (float)0;
//            temp.neutral = (float)1.4002951E-6;
//            temp.sadness = (float)0.7927469;
//            temp.surprise = (float)0.001805695;
//            temp.date = "2017-04-17T13:11:17";
//            temp.fileName = "/storage/emulated/0/Android/data/" +
//                    "com.example.naveen.EmotionApp/files/Pictures/JPEG_20170416_131103_875443797.jpg";
//            temp.save(getContext());
            List<Emotion> list = Emotion.getEmotionsListForGraphView(getActivity());

            GraphHelper.showStatistics(list,graphView,getActivity());
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_graph, container, false);
            return rootView;
        }


    }

}
