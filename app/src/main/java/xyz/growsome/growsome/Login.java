package xyz.growsome.growsome;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedHashMap;
import java.util.Map;

import xyz.growsome.growsome.Utils.Connection;
import xyz.growsome.growsome.Utils.DBHelper;
import xyz.growsome.growsome.DBTables.*;

/**
 * Descripcion: Activity de login de la aplicacion
 * Autor: Sergio Cruz
 * Fecha: 2016-10-08
 * Modificacin: 2017-03-12 Sergio Cruz
 **/

public class Login extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private Connection mAuthTask = null;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private  DBHelper dbHelper;
    public static String email;
    public static String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DBHelper(this);

        if(checkDB())
        {
            Intent intent = new Intent(Login.this, Main.class);
            startActivity(intent);
            finish(); ///Finaliza  la actividad
        }

        // Se liga la vista
        setContentView(R.layout.activity_login);
        setTitle(R.string.title_activity_login);

        // Se instancian los controles
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button btnLogIn = (Button) findViewById(R.id.email_sign_in_button);
        btnLogIn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                attemptLogin();
            }
        });
        Button btnRegister = (Button) findViewById(R.id.register_button);
        btnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private boolean checkDB()
    {
        try
        {
            Cursor cursor = dbHelper.selectQuery("select * from Usuarios");
            cursor.moveToFirst();
            int id = cursor.getInt(TableUsuarios.COL_ICOD_ID);
            String vchCorreo = cursor.getString(TableUsuarios.COL_CORREO_ID);
            cursor.close();
        }
        catch (Exception ex)
        {
            return false;
        }

        return true;
    }

    private void attemptLogin()
    {
        // Resetea los controles
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Obtiene los valores de los controles
        email = mEmailView.getText().toString().trim();
        password = mPasswordView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Validala contraseña
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password))
        {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Valida el correo
        if (TextUtils.isEmpty(email))
        {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        else if (!isEmailValid(email))
        {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // Si hay un error en la validacion de la contraseña no se intenta logear
            focusView.requestFocus();
        }
        else
        {
            // Se muestra el spinner mientras se crea la asynctask para traer la informacion del server

            showProgress(true);
            Map<String, Object> parametros = new LinkedHashMap<>();
            parametros.put("request", "login");
            parametros.put("email", email);
            parametros.put("pwd", password);
            mAuthTask = new Connection(this, parametros){
                @Override
                public void onTaskFinished(String data, boolean error)
                {
                    showProgress(false);
                    if(!data.isEmpty())
                    {
                        if(error)
                        {
                            Toast.makeText(Login.this, data, Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            //TODO Se debe agregar logica para parsear el json
                            Toast.makeText(Login.this, data, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Login.this, Main.class);
                            intent.putExtra("webData", data);
                            startActivity(intent);
                            finish(); ///Finaliza  la actividad
                        }
                    }
                    else
                    {
                        Toast.makeText(Login.this, R.string.error_no_data, Toast.LENGTH_LONG).show();
                    }
                }
            };
            mAuthTask.execute();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Logica del correo
        return email.contains("@");
    }

    private boolean isPasswordValid(String password)
    {
        //TODO: Logica de la contraseña
        return password.length() > 4;
    }

    // Animacion del proceso
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
        else
        {
            // Si la version de android no soporta la animacion solo se oculta y muestra lo que importa.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {}

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

}

