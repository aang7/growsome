package xyz.growsome.growsome;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import xyz.growsome.growsome.DBTables.TableUsuarios;
import xyz.growsome.growsome.Gastos.GastosMainFragment;
import xyz.growsome.growsome.Ingresos.IngresosMainFragment;
import xyz.growsome.growsome.Utils.DBHelper;
import xyz.growsome.growsome.Utils.JSONHelper;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    Fragment fragment;
    private TextView navName;
    private TextView navEmail;
    private ImageView navImage;
    private  FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //Codigo temporal para exportar la base de datos a Descargas;
                try
                {
                    int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    int request = 0;

                    if(permissionCheck != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(Main.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                request);
                    }

                    File backupDB = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                            "growsome.db");
                    File currentDB = getApplicationContext().getDatabasePath("growsome.db");
                    if (currentDB.exists()) {
                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                    }
                    Snackbar.make(view, "Database Exported", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                catch(Exception ex)
                {
                    Log.d("FAILED",ex.toString());
                    Snackbar.make(view, "Failed", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        /* Navigation Drawer */
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //setting up home_fragment at the beginning
        setFragment(new HomeFragment());

        //Obtiene informacion de el Intent que mando a llamar esta actividad.
        Bundle b = getIntent().getExtras();
        if(b != null)
        {
            String jsonBundle = b.getString("webData"); //Referencia a la info traida del intent

            View headerNV = navigationView.getHeaderView(0);

            navName = (TextView) headerNV.findViewById(R.id.nav_name);
            navEmail = (TextView)  headerNV.findViewById(R.id.nav_email);

            //Tomando los valores del JSON
            try
            {
                JSONHelper jsonParser = new JSONHelper(jsonBundle);
                JSONObject jsonObj = jsonParser.getJsonObject();
                String userName = jsonParser.getJsonObject().getString("vchUsuario"); //Tomo el nombre
                String userEmail = jsonParser.getJsonObject().getString("vchCorreo"); //Tomo el Correo
                navName.setText(userName);
                navEmail.setText(userEmail);
                DBHelper dbHelper =  new DBHelper(this);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                TableUsuarios.insert(db, jsonObj);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_logout) {

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

        if(id == R.id.home_item)
        {
            fragment = new HomeFragment();
        }
        else if (id == R.id.ingresos_item)
        {
            fragment = new IngresosMainFragment();
        }
        else if (id == R.id.gastos_item)
        {
            fragment = new GastosMainFragment();
        }
        else if (id == R.id.settings_item)
        {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
            return  true;
        }
        else if (id == R.id.about_item)
        {
            fragment = new HomeFragment();
        }
        else
        {
            fragment = new HomeFragment();
        }

        setFragment(fragment);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment(Fragment frgmnt)
    {
        /* Fragment stuff */
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        //Layout a remplazar, instancia del fragmento, tag opcional
        ft.replace(R.id.content_frame, frgmnt, "tag");
        ft.addToBackStack("tag");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void showFloatingActionButton() {
        fab.show();
    };

    public void hideFloatingActionButton() {
        fab.hide();
    };
}
