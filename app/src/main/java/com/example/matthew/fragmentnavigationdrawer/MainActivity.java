package com.example.matthew.fragmentnavigationdrawer;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import static android.R.attr.bitmap;
import static android.R.attr.data;
import static android.R.attr.fragment;
import static android.R.attr.id;
import static com.example.matthew.fragmentnavigationdrawer.R.id.flContent;
import static com.example.matthew.fragmentnavigationdrawer.R.id.result_text;
import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity {
//    Declare global variables. Eventually look into using local variables and passing them to
// and returning them from methods.
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private boolean shareResults = false;
//*********************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//  onCreate set content view to activity_main and setup the drawer navigation
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);
//***********************************************************************************************
//        Setup to open on the first fragment, the LC
        // Create a new fragment and specify the fragment to show onCreate
        Fragment fragment = null;
        Class fragmentClass;
        fragmentClass = FirstFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(flContent, fragment).commit();
//*********************************************************************************************
    }
//  *************************  End of onCreate. ***********************************************

//  Construct a new ActionBarDrawerToggle with a Toolbar.
    private ActionBarDrawerToggle setupDrawerToggle() {
//   Passes in (Activity = this, Drawer layout to link to ActionBar = mDrawer, Independant toolbar to use = toolbar,
//   String to describe open drawer for accessibility = R.string.drawer_open,
//   String to describe closed drawer for accessibility = R.string.drawer_close,
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

// Setup listener to listen for touches on the NavDrawer
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }
// ****************************************************************************

// This is one of mine. I really need to document it properly at some stage.
//  Creates an implied email intent to send to my gmail with a subject about feedback, no extra text
//  and has a title on the app chooser dialogue of "Send Feedback"
    private void sendFeedbackEmail(){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(getString(R.string.mailto_command)+getString(R.string.developers_email_address)));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject_line));
        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body_text));
        startActivity(Intent.createChooser(emailIntent, getString(R.string.title_displayed_in_email_chooser)));
    }
// *****************************************************************************

//    This method creates a bitmap screencapture.
    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
//   ***************************************************************************

//  This method calls screenShot(View view) to screencap that view, then calls shareBitmap(Bitmap, String)
//    to actually share the bitmap
    private void createImageAndShare(){
        // TODO: 12/10/2016 Create an image to share.
        // Create a bitmap of the screen of view flContent whic is the Frame Layout that the fragments
        // are inflated into.
        final Bitmap screenshot = screenShot(findViewById(R.id.flContent));
        // Share the bitmap.
        shareBitmap(screenshot, "share");
    }
// *****************************************************************************

//  This method creates the implied intent that converts the bitmap to a .png and shares it.
    private void shareBitmap (Bitmap bitmap,String fileName) {
        try {
            File file = new File(this.getCacheDir(), fileName + ".png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//  **************************************************************************

// This method is the case statement for what happens when a nav drawer item is selected.
// It sets up and inflates the relevant fragment required as well as managing the nav drawer.
    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = FirstFragment.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = SecondFragment.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = ThirdFragment.class;
                break;
            case R.id.nav_fourth_fragment:
                fragmentClass = FourthFragment.class;
                break;
            case R.id.nav_fifth_fragment:
                fragmentClass = FifthFragment.class;
                break;
            case R.id.nav_sub1_fragment:
                fragmentClass = Sub1Fragment.class;
                shareResults = true;
//        Put the intent call in here. use if statement to test boolean. If i leave it in the case statement
//        then I really dont need the boolean and the if test.
        if(shareResults){
                // TODO: 12/10/2016 make facebook share work. It shares whatever fragment is open at the time.
                // It would be better if it switched to he fragment first, then screen captured and shared.
            createImageAndShare();
            shareResults = false;
        }
                break;
            case R.id.nav_sub2_fragment:
                sendFeedbackEmail();
                fragmentClass = Sub2Fragment.class;
                break;
            default:
                fragmentClass = FirstFragment.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(flContent, fragment).commit();
        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }
// *****************************************************************************

// Handles the nav drawer selection. Not really sure. I'll have to see what's going on.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
// *******************************************************************************


    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }
// *******************************************************************************

//  I think this is called when there is a configuration change. I don't know what that really means though.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }
// *******************************************************************************

// This method is to search for the plate. Hopefully it's in the right place.
    public void searchPlate(View view){
        Fragment fragment = null;
        Class fragmentClass;
// TODO: 12/10/2016 get all the info from the spinners then lookup what they translate into. Build into a string variable and pass it to the result intent
        Spinner spinner = (Spinner)findViewById(R.id.lc_model_spinner);
        String mModelText = spinner.getSelectedItem().toString();
        EditText edit = (EditText)findViewById(R.id.lc_body_editText);
        String mBodyText = edit.getText().toString();
        spinner = (Spinner)findViewById(R.id.lc_trim_spinner);
        String mTrimText = spinner.getSelectedItem().toString();
        spinner = (Spinner)findViewById(R.id.lc_paint_spinner);
        String mPaintText = spinner.getSelectedItem().toString();
        spinner = (Spinner)findViewById(R.id.lc_top_spinner);
        String mTopText = spinner.getSelectedItem().toString();
        // TODO: 12/10/2016 build this with a StringBuilder instead.
        String mresultText = "Model: " + mModelText + "\nBody No: " + mBodyText + "\nTrim: "
                + mTrimText + "\nPaint: " + mPaintText + "\nTop: " + mTopText;
        // TODO: 12/10/2016 Output to TextView @+id/result_text in fragment_result.xml
        Toast.makeText(this, mresultText, Toast.LENGTH_LONG).show();
        fragmentClass = ResultFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(flContent, fragment).commit();
//        TextView resultText = (TextView) findViewById(R.id.result_text);
//        resultText.setText(mresultText);
        // TODO: 12/10/2016 setup method to decode the results. Just populate it with the few options in the spinners.
    }
// *****************************************************************************************

}