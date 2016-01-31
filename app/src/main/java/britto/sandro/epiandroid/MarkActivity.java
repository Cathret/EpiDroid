package britto.sandro.epiandroid;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedList;
import britto.sandro.epiandroid.MarkActivity;

@EActivity(R.layout.activity_mark)
public class MarkActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String MResponse;
    LinkedList<String> MyList;

    private String token;
    private String title;
    private String correcteur;
    private String mark;


    @RestService
    MyRequest API;

    @ViewById
    Toolbar toolbar;

    @ViewById
    DrawerLayout drawer_layout;

    @ViewById
    NavigationView nav_view;

    @ViewById
    TextView Title;

    @ViewById
    TextView Correcteur;

    @ViewById
    TextView Note;


    @UiThread
    void display(String api) {
        for (int i = 0; i < MyList.size(); i++) {
            Title.setText(this.title);
            Correcteur.setText(this.correcteur);
            Note.setText(this.mark);
        }
    }

    @Background
    void testapi(String token) {
        String m = API.Mark(token);
        markParsingExecute(m);
        display(m);
    }

    @AfterViews
    void init() {
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.setDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(this);
        token = getIntent().getExtras().getString("token");
        testapi(token);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            drawer_layout.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(this, MainActivity_.class);
            intent.putExtra("token", token);
            startActivity(intent);
        } else if (id == R.id.nav_planning) {
            drawer_layout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, PlanningActivity_.class));
        } else if (id == R.id.nav_module) {
            drawer_layout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, ModuleActivity_.class));
        } else if (id == R.id.nav_mark) {
            drawer_layout.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(this, MarkActivity_.class);
            intent.putExtra("token", token);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            drawer_layout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, LoginActivity_.class));
        }
        drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Background
    void markParsingExecute(String MResponse) {
        if (MResponse != null) {
            try {
                JSONArray array = new JSONArray(MResponse);
                MyList = new LinkedList<String>();
                for (int i = 0; i < array.length(); i++) {
                    MyList.add(this.title = array.getJSONObject(i).getString("title"));
                    MyList.add(this.correcteur = array.getJSONObject(i).getString("correcteur"));
                    MyList.add(this.mark = array.getJSONObject(i).getString("final_note"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}