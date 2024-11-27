package pt.ipleiria.estg.dei.books;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

public class MenuMainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    public static final String EMAIL = "EMAIL";
    public static final int ADD = 100, EDIT = 200, DELETE = 300;
    public static final String OP_CODE = "DETAIL_OPERATION";
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private String email = "";
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);
        fragmentManager = getSupportFragmentManager();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.ndOpen, R.string.ndClose
        );
        toggle.syncState();
        drawer.addDrawerListener(toggle);
        loadHeader();
        navigationView.setNavigationItemSelectedListener(this);
        loadInitialFragment();
    }

    private void loadHeader() {
        email = getIntent().getStringExtra(EMAIL).toString();

        SharedPreferences sharedPreferencesUser = getSharedPreferences("USER_DATA", MODE_PRIVATE);

        if(email != null){
            SharedPreferences.Editor editorUser = sharedPreferencesUser.edit();
            editorUser.putString(EMAIL, email);
            editorUser.apply();
        } else {
            email = sharedPreferencesUser.getString(EMAIL,"No Email Provided");
        }

        View hView = navigationView.getHeaderView(0);
        TextView nav_tvEmail = hView.findViewById(R.id.textViewEmail);
        nav_tvEmail.setText(email);
    }

    private void loadInitialFragment() {
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.getItem(0);
        item.setCheckable(true);
        onNavigationItemSelected(item);
    }

    public void sendEmail() {
        String subject = "AMSI 2024/25";
        String message = "Olá " + email + " isto é uma mensagem de teste";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);
        else Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint({"NonConstantResourceId", "QueryPermissionsNeeded"})
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        if (item.getItemId() == R.id.listBook) {
            fragment = new ListBookFragment();
            setTitle(item.getTitle());
        }
        if (item.getItemId() == R.id.gridBook) {
//            fragment = new DynamicFragment();
//            setTitle(item.getTitle());
        }
        if (item.getItemId() == R.id.navEmail) {
            sendEmail();
        }
        drawer.closeDrawer(GravityCompat.START);
        if (fragment != null)
            fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();
        return true;
    }


}