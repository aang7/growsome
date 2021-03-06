package xyz.growsome.growsome;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONException;
import org.json.JSONObject;

import xyz.growsome.growsome.Categorias.CategoriasAddFragment;
import xyz.growsome.growsome.Categorias.CategoriasMainFragment;
import xyz.growsome.growsome.DBTables.TableUsuarios;
import xyz.growsome.growsome.Gastos.GastosAddFragment;
import xyz.growsome.growsome.Gastos.GastosMainFragment;
import xyz.growsome.growsome.Ingresos.IngresosAddFragment;
import xyz.growsome.growsome.Ingresos.IngresosMainFragment;
import xyz.growsome.growsome.Utils.DBHelper;
import xyz.growsome.growsome.Utils.JSONHelper;


public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    DrawerLayout drawer;
    Fragment fragment;
    private TextView navName;
    private TextView navEmail;
    private ImageView navImage;
    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton fab_ingresos;
    private FloatingActionButton fab_gastos;
    private FloatingActionButton fab_categorias;
    private String userName = "";
    private String userEmail = "";
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DBHelper(this);

        setContentView(R.layout.activity_main);

        setup();

        floatingActionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

            }
        });

        //setting up home_fragment at the beginning
        setFragment(new HomeFragment());

        //Obtiene informacion de el Intent que mando a llamar esta actividad.
        Bundle b = getIntent().getExtras();
        if(b != null)
        {
            String jsonBundle = b.getString("webData"); //Referencia a la info traida del intent

            //Tomando los valores del JSON
            try
            {
                JSONHelper jsonParser = new JSONHelper(jsonBundle);
                JSONObject jsonObj = jsonParser.getJsonObject();
                userName = jsonParser.getJsonObject().getString("vchUsuario"); //Tomo el nombre
                userEmail = jsonParser.getJsonObject().getString("vchCorreo"); //Tomo el Correo
                navName.setText(userName);
                navEmail.setText(userEmail);
                DBHelper dbHelper =  new DBHelper(this);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                TableUsuarios.insert(db, jsonObj);

//                PrefHelper.saveToPrefs(this, "PREF_USERNAME", userName);
//                PrefHelper.saveToPrefs(this, "PREF_USEREMAIL", userEmail);
            }
            catch (JSONException ex)
            {
                ex.printStackTrace();
                Toast.makeText(this, R.string.error_default, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            try
            {
                String[] data = TableUsuarios.getUserData(dbHelper.getReadableDatabase());
                navName.setText(data[0]);
                navEmail.setText(data[1]);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                Toast.makeText(this, R.string.error_default, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setup()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerNV = navigationView.getHeaderView(0);
        navName = (TextView) headerNV.findViewById(R.id.nav_name);
        navEmail = (TextView)  headerNV.findViewById(R.id.nav_email);

        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.floating_action_menu);
        floatingActionMenu.setClosedOnTouchOutside(true);
        fab_ingresos = (FloatingActionButton) findViewById(R.id.fab_ingresos);
        fab_gastos = (FloatingActionButton) findViewById(R.id.fab_gastos);
        fab_categorias = (FloatingActionButton) findViewById(R.id.fab_categorias);

        fab_ingresos.setOnClickListener(clickListener);
        fab_gastos.setOnClickListener(clickListener);
        fab_categorias.setOnClickListener(clickListener);

        /* Navigation Drawer */
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
        Log.d("ENTRO", "Entro aqui");
    }

    /* Here we can handle which menu object will represent the action bar */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_logout)
        {
            this.deleteDatabase("growsome.db");
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case R.id.home_item:
                fragment = new HomeFragment();
                break;
            case R.id.ingresos_item:
                fragment = new IngresosMainFragment();
                break;
            case R.id.gastos_item:
                fragment = new GastosMainFragment();
                break;
            case R.id.categorias_item:
                fragment = new CategoriasMainFragment();
                break;
            case R.id.settings_item:
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                return true;
            case R.id.about_item:
                fragment = new HomeFragment();
                break;
            case R.id.action_logout:
                this.deleteDatabase("growsome.db");
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                return true;
            default:
                fragment = new HomeFragment();
        }

        setFragment(fragment);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment(Fragment fragment)
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void setFragment(Fragment fragment, Boolean backStack, int transition)
    {
        if(backStack)
        {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame,fragment);
            ft.addToBackStack(null);
            ft.setTransition(transition);
            ft.commit();
        }
        else
        {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.setTransition(transition);
            ft.commit();
        }

    }

    public void showFAB(boolean value)
    {
        if(value)
        {
            floatingActionMenu.showMenuButton(true);
        }
        else
        {
            floatingActionMenu.hideMenuButton(true);

        }
    }

    public void showDrawer(boolean value)
    {
        int lockMode = value ? DrawerLayout.LOCK_MODE_UNLOCKED :
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        drawer.setDrawerLockMode(lockMode);
        toggle.setDrawerIndicatorEnabled(value);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab_ingresos:
                    fragment = new IngresosAddFragment();
                    break;
                case R.id.fab_gastos:
                    fragment = new GastosAddFragment();
                    break;
                case R.id.fab_categorias:
                    fragment = new CategoriasAddFragment();
                    break;
            }

            setFragment(fragment, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
    };
}
