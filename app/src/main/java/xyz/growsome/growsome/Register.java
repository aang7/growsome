package xyz.growsome.growsome;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedHashMap;
import java.util.Map;

import xyz.growsome.growsome.Utils.Connection;

public class Register extends AppCompatActivity {

    private Connection mAuthTask = null;
    private EditText edtxtRegisterName;
    private EditText edtxtRegisterMail;
    private EditText edtxtRegisterPassword;
    private Button btnRegister;
    private View mProgressView;
    private View mLoginFormView;

    public static String name;
    public static String email;
    public static String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle(R.string.title_activity_register);

        edtxtRegisterName = (EditText) findViewById(R.id.edt_register_name);
        edtxtRegisterMail = (EditText) findViewById(R.id.edt_register_mail);
        edtxtRegisterPassword = (EditText) findViewById(R.id.edt_register_password);
        btnRegister = (Button) findViewById(R.id.email_sign_up_button);

        mLoginFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
    }

    private void attemptRegister()
    {
        // Resetea los controles

        edtxtRegisterName.setError(null);
        edtxtRegisterMail.setError(null);
        edtxtRegisterPassword.setError(null);

        // Obtiene los valores de los controles
        name = edtxtRegisterName.getText().toString();
        email = edtxtRegisterMail.getText().toString();
        password = edtxtRegisterPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Validala contraseña
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password))
        {
            edtxtRegisterPassword.setError(getString(R.string.error_invalid_password));
            focusView = edtxtRegisterPassword;
            cancel = true;
        }

        // Valida el correo
        if (TextUtils.isEmpty(email))
        {
            edtxtRegisterMail.setError(getString(R.string.error_field_required));
            focusView = edtxtRegisterMail;
            cancel = true;
        }
        else if (!isEmailValid(email))
        {
            edtxtRegisterMail.setError(getString(R.string.error_invalid_email));
            focusView = edtxtRegisterMail;
            cancel = true;
        }

        if (TextUtils.isEmpty(name))
        {
            edtxtRegisterName.setError(getString(R.string.error_field_required));
            focusView = edtxtRegisterName;
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
            parametros.put("request", "register");
            parametros.put("name", name);
            parametros.put("email", email);
            parametros.put("pwd", password);
            mAuthTask = new Connection(this, parametros){
                @Override
                public void onTaskFinished(String data, boolean error)
                {
                    showProgress(false);
                    if(!data.isEmpty())
                    {
                        //TODO Se debe agregar logica para parsear el json
                        Toast.makeText(Register.this, data, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Register.this, Login.class);
                        startActivity(intent);
                        finish(); ///Finaliza  la actividad
                    }
                    else
                    {
                        Toast.makeText(Register.this, R.string.error_no_data, Toast.LENGTH_LONG).show();
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

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
}
