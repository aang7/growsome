package xyz.growsome.growsome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle("Registro");
        EditText edtxtRegisterMail = (EditText) findViewById(R.id.edt_register_mail);
        EditText edtxtRegisterPassword = (EditText) findViewById(R.id.edt_register_password);
        Button btnRegister = (Button) findViewById(R.id.email_sign_up_button);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //attemptRegister();
            }
        });


    }
}
