package britto.sandro.epiandroid;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
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
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import britto.sandro.epiandroid.Request.InfosTask;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String IResponse;
    private String NResponse;
    private String pic;
    private String name;
    private String cycle;
    private String promotion;
    private String ville;
    private String currentlogTime;
    private String semester;
    private String currentGPA;
    private String title;
    private String user;
    private String date;
    private String content;
    LinkedList<String> MyList;

    protected String token;

    @RestService
    MyRequest API;

    @ViewById
    Toolbar toolbar;
    @ViewById
    DrawerLayout drawer_layout;
    @ViewById
    NavigationView nav_view;

    /*     Infos Utilisateurs */

    @ViewById
    ImageView profilPicture;
    @ViewById
    TextView Identity;
    @ViewById
    TextView Cycle;
    @ViewById
    TextView Promo;
    @ViewById
    TextView Localisation;
    @ViewById
    TextView LogTime;
    @ViewById
    TextView Semester;
    @ViewById
    TextView GPA;


    /* Notificatiins Utilisateur */

    @ViewById
    TextView Title;
    @ViewById
    TextView User;
    @ViewById
    TextView Date;
    @ViewById
    TextView Content;


    @UiThread
    void display(String api) {
        Picasso.with(getBaseContext()).load(this.pic).into(profilPicture);
        Identity.setText(this.name);
        Cycle.setText(this.cycle);
        Promo.setText("Promotion " + this.promotion);
        Localisation.setText(this.ville);
        LogTime.setText("Temps de Log : " + this.currentlogTime);
        Semester.setText("Semester " + this.semester);
        GPA.setText("GPA : " + this.currentGPA);
        for(int i = 0; i < MyList.size(); i++)
        {
            Title.setText(this.title);
            User.setText(this.user);
            Date.setText(this.date);
            Content.setText(this.content);
        }
    }

    @Background
    void testapi(String token) {
        InfosTask info = new InfosTask(token);
        String r = API.Info(info);
        String m = API.Notif(token);
        infosParsingExecute(r);
        notifParsingExecute(m);
        display(r);
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
    protected void infosParsingExecute(String IResponse) {
        if (IResponse != null) {
            try {
                JSONObject json = new JSONObject(IResponse);
                JSONObject Itmp = json.getJSONObject("infos");
                this.pic = "https://cdn.local.epitech.eu/userprofil/profilview/" + Itmp.getString("login") + ".jpg";
                this.name = Itmp.getString("title");
                this.cycle = Itmp.getString("course_code");
                this.promotion = Itmp.getString("promo");
                this.ville = Itmp.getString("location");
                this.semester = Itmp.getString("semester");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                JSONObject json = new JSONObject(IResponse);
                JSONArray array = json.getJSONArray("current");
                JSONObject time = array.getJSONObject(0);
                this.currentlogTime = time.getString("active_log");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            this.pic = "Image manquante";
        }
    }

    @Background
    void notifParsingExecute(String NResponse) {
        if (NResponse != null) {
            try {
                JSONArray array = new JSONArray(NResponse);
                MyList = new LinkedList<String>();
                for (int i = 0; i < array.length(); i++)
                {
                    MyList.add(this.title = array.getJSONObject(i).getString("title"));
                    MyList.add(this.user = array.getJSONObject(i).getJSONObject("user").getString("title"));
                    MyList.add(this.date = array.getJSONObject(i).getString("date"));
                    MyList.add(this.content = array.getJSONObject(i).getString("content"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
