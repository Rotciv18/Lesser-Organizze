package com.rotciv.organizze.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.rotciv.organizze.R;
import com.rotciv.organizze.activity.config.ConfiguracaoFirebase;
import com.rotciv.organizze.activity.helper.Base64Custom;
import com.rotciv.organizze.activity.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText textNome, textMail, textSenha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        textNome = findViewById(R.id.editNome);
        textMail = findViewById(R.id.editMail);
        textSenha = findViewById(R.id.editSenha);
    }

    public void cadastrar (View view){
        String nome = textNome.getText().toString();
        String email = textMail.getText().toString();
        String senha = textSenha.getText().toString();
        Usuario user = new Usuario(nome, email, senha);
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()){
            Toast.makeText(this, "Tenha certeza de que preencheu todos os campos!", Toast.LENGTH_LONG).show();
        } else {
            cadastrarUsuario(user);
        }
    }

    public void cadastrarUsuario (final Usuario user){
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        autenticacao.createUserWithEmailAndPassword(user.getEmail(), user.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String idUsuario = Base64Custom.codificarBase64(user.getEmail());
                    user.setIdUsuario(idUsuario);
                    user.salvar();

                    Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    String excecao;
                    try {
                        throw task.getException();
                    } catch(FirebaseAuthWeakPasswordException e) {
                        excecao = "Digite uma senha mais forte!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Digite um email válido!";
                    } catch (FirebaseAuthUserCollisionException e){
                        excecao = "Já existe um usuário com este email!";
                    } catch (Exception e){
                        excecao = "Erro: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
