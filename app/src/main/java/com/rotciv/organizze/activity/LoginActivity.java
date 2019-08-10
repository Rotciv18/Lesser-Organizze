package com.rotciv.organizze.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.rotciv.organizze.R;
import com.rotciv.organizze.activity.config.ConfiguracaoFirebase;
import com.rotciv.organizze.activity.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText editMail, editSenha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editMail = findViewById(R.id.editMail);
        editSenha = findViewById(R.id.editSenha);
    }

    public void login (View view){
        Usuario user = new Usuario();
        user.setEmail(editMail.getText().toString());
        user.setSenha(editSenha.getText().toString());
        if (user.getEmail().isEmpty() || user.getSenha().isEmpty()){
            Toast.makeText(this, "Tenha certeza de que preencheu todos os campos!", Toast.LENGTH_LONG).show();
        } else {
            loginUsuario(user);
        }
    }

    public void loginUsuario(Usuario user){
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        autenticacao.signInWithEmailAndPassword(user.getEmail(), user.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    irParaTelaPrincipal();
                } else {
                    try {
                        throw (task.getException());
                    } catch (FirebaseAuthInvalidUserException e){
                        Toast.makeText(LoginActivity.this, "Email inválido!", Toast.LENGTH_LONG).show();
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        Toast.makeText(LoginActivity.this, "Senha inválida!", Toast.LENGTH_LONG).show();
                    } catch (Exception e){

                    }
                }
            }
        });
    }
    public void irParaTelaPrincipal (){
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }
}
