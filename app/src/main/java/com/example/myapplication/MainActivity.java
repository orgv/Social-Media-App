package com.example.myapplication;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.Fragments.ChatsFragment;
import com.example.myapplication.Fragments.FavoritesFragment;
import com.example.myapplication.Fragments.HomeFragment;
import com.example.myapplication.Fragments.ProfileFragment;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    private static final int CAMERA_CODE_REQUEST = 891;
    private static final int CAMERA_CODE_REQUEST_PERM = 1764;
    private static final int STORAGE_REQUEST = 555;
    private static final int PICK_IMAGE_CODE_REQUEST = 2653;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    FirebaseAuth.AuthStateListener authStateListener;

    DatabaseReference reference;

    BottomNavigationView bottomNavigationView;
    BottomAppBar bottomAppBar;
    FloatingActionButton bottomFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MyApplication);
        //setTheme(R.style.Theme_MyApplication_NoTitleActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar_main));


//        if (FirebaseDatabase.getInstance() == null) {
//            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//        }


        bottomAppBar = findViewById(R.id.bottom_app_bar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomFab = findViewById(R.id.bottom_fab);

        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);




        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int selectedFragmentResId = 0;
            int itemId = item.getItemId();
            if (itemId == R.id.homeFragment) {
                navController.navigate(R.id.homeFragment);
                //NavigationUI.onNavDestinationSelected(item,navController);
                //selectedFragmentResId = R.id.homeFragment;
            } else if (itemId == R.id.favoritesFragment) {
                navController.navigate(R.id.favoritesFragment);
            } else if (itemId == R.id.chatsFragment) {
                navController.navigate(R.id.chatsFragment);
            }
            else if(itemId==R.id.profileFragment){
                navController.navigate(R.id.profileFragment);
            }
//            if (selectedFragmentResId != 0) {
//                navController.navigate(selectedFragmentResId);
//            }
            //NavigationUI.onNavDestinationSelected(item,navController);
            return true;
        }
        });

//



//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment, R.id.nearbyFragment, R.id.messagesFragment, R.id.profileFragment, R.id.favoritesFragment).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

//        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.home:
//                        switchFragment(homeFragment);
//                        return true;
//                    case R.id.favorites:
//                        switchFragment(favoritesFragment);
//                        return true;
//                    case R.id.nearby:
//                        switchFragment(nearbyFragment);
//                        return true;
//                    case R.id.messages:
//                        switchFragment(messagesFragment);
//                        return true;
//                    case R.id.profile:
//                        switchFragment(profileFragment);
//                        return true;
//                }
//                return false;
//            }
//        });


//        loginFragment = new LoginFragment();
//        registerFragment = new RegisterFragment();
//
//        homeFragment = new HomeFragment();
//        favoritesFragment = new FavoritesFragment();
//        nearbyFragment = new NearbyFragment();
//        messagesFragment = new ChatsFragment2();
//        profileFragment = new ProfileFragment();
//
//        RelativeLayout rootLayout = findViewById(R.id.root_layout);
//
//        switchFragment(homeFragment);
//
//
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) { // user signed in
                    Toast.makeText(MainActivity.this, "" + getCallingActivity() + " Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();

                    System.out.println("I am Inside MainActivity");
                    navGraph.setStartDestination(R.id.homeFragment);
                    navController.setGraph(navGraph);

                    showBottomViews();

                } else {
                    Toast.makeText(MainActivity.this, "Please Log In ", Toast.LENGTH_SHORT).show();
                    // send some kind of var to unhide buttons

                    hideBottomViews();

                    navController.setGraph(navGraph);
                    Log.e("Main_Activity_Check_LOGGED_OUT", "Please Log In ");
                }
            }
        };

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
        //firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
        //firebaseAuth.removeAuthStateListener(authStateListener);
    }



    public void hideBottomViews() {
        bottomAppBar.setVisibility(View.GONE);
        bottomNavigationView.setVisibility(View.GONE);
        bottomFab.setVisibility(View.GONE);
    }

    public void showBottomViews() {
        bottomAppBar.setVisibility(View.VISIBLE);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomFab.setVisibility(View.VISIBLE);

    }


    private void status(String status) {
        if (currentUser != null) {
            System.out.println("PRIME PEER " + currentUser.getEmail());
            reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("status", status);

            reference.updateChildren(hashMap);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item,navController) || super.onOptionsItemSelected(item);
    }


    //public int getNavBarHeight(){
//    Resources resources = getResources();
//    int resourceId = resources.getIdentifier("navigation_bar_height","dimen","android");
//    if(resourceId>0)
//        return resources.getDimensionPixelSize(resourceId);
//    return 0;
//}
}