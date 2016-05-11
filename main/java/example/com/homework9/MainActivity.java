/*
    Cole Howell, Manoj Bompada
    ITCS 4180
    MainActivity.java
 */

package example.com.homework9;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, SignupFragment.OnFragmentInteractionListener, ConversationsFragment.OnFragmentInteractionListener,
        ContactsFragment.OnFragmentInteractionListener, ViewContactFragment.OnFragmentInteractionListener, ViewMessagesFragment.OnFragmentInteractionListener,
        EditProfileFragment.OnFragmentInteractionListener {

    private Firebase mRef;
    private ActionBarDrawerToggle mDrawerToggle;

    private String[] mPlanetTitles;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    String[] osArray = { "Contacts", "Conversations", "Archived", "Settings", "Logout","Exit"};
    ArrayList<String> planetList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://homework-9.firebaseio.com/");

        AuthData authData = mRef.getAuth();
        if (authData != null) {
            getFragmentManager().beginTransaction().replace(R.id.container, new ConversationsFragment(), "conversation").commit();
//
        } else {
            getFragmentManager().beginTransaction().add(R.id.container, new LoginFragment(), "login").commit();
        }

        Collections.addAll(planetList,osArray);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, R.id.rowTextView, planetList));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer1,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(-1);
        }


    }

    @Override
    public void goToConversationFragmentfromEditProfile() {
        getFragmentManager().beginTransaction().replace(R.id.container, new ConversationsFragment(), "conversationfromEdit").commit();
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new Fragment();
        Bundle args = new Bundle();
        FragmentManager fragmentManager = getFragmentManager();
//        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
//        fragment.setArguments(args);
//
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        switch (position){
            case 0:
                if(mRef.getAuth() == null){
                    Toast.makeText(this,"Please login",Toast.LENGTH_SHORT).show();
                }
                else{
                    fragmentManager.beginTransaction().replace(R.id.container, new ContactsFragment(), "contacts").commit();
                }

                break;
            case 1:

                if(mRef.getAuth() == null){
                    Toast.makeText(this,"Please login",Toast.LENGTH_SHORT).show();
                }
                else{
                    fragmentManager.beginTransaction().replace(R.id.container, new ConversationsFragment(), "conversations").commit();
                }
                break;
            case 2:

                if(mRef.getAuth() == null){
                    Toast.makeText(this,"Please login",Toast.LENGTH_SHORT).show();
                }
                else{
                    ConversationsFragment.archieved = true;
                    fragmentManager.beginTransaction().replace(R.id.container, new ConversationsFragment(), "conversationsArchived").commit();
                }
                break;
            case 3:

                if(mRef.getAuth() == null){
                    Toast.makeText(this,"Please login",Toast.LENGTH_SHORT).show();
                }
                else{
                    fragmentManager.beginTransaction().replace(R.id.container, new EditProfileFragment(), "ediProfile").commit();
                }
                break;
            case 4:
                if(!(mRef.getAuth() == null)){
                mRef.child("users").unauth();}
                fragmentManager.beginTransaction().replace(R.id.container, new LoginFragment(), "login").commit();
                break;
            case 5:
                finish();
                break;
            default:
                fragmentManager.beginTransaction().replace(R.id.container, new LoginFragment(), "login screen").commit();
//                finish();
                break;
        }
//
// Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
//        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

//    @Override
//    public void setTitle(CharSequence title) {
//        mTitle = title;
//        getActionBar().setTitle(mTitle);
//    }

    public void goToLoginFragment() {

        getFragmentManager().beginTransaction().replace(R.id.container, new LoginFragment(), "login").commit();

    }

    @Override
    public void goToSignupFragment() {
        getFragmentManager().beginTransaction().replace(R.id.container, new SignupFragment(), "sign-up").commit();
    }

    @Override
    public void goToConversationFragment() {
//        Bundle bundle = new Bundle();
//        bundle.putString("email", email);
//        ConversationsFragment conversationsFragment = new ConversationsFragment();
//        conversationsFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container, new ConversationsFragment(), "conversation").commit();
    }

    @Override
    public void goToViewMessage() {
//        getFragmentManager().beginTransaction().replace(R.id.container, new ViewMessagesFragment(), "viewMessage").commit();
    }

    @Override
    public void goToViewContact() {
//        getFragmentManager().beginTransaction().replace(R.id.container, new ViewContactFragment(), "viewContact").addToBackStack(null).commit();

    }

    @Override
    public void onBackPressed() {

        if(getFragmentManager().getBackStackEntryCount()>0){
            getFragmentManager().popBackStack();
            setTitle("Stay in Touch");
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
