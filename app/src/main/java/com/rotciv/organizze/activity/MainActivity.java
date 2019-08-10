package com.rotciv.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.rotciv.organizze.R;
import com.rotciv.organizze.activity.config.ConfiguracaoFirebase;

public class MainActivity extends IntroActivity {

    FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        setButtonNextVisible(false);
        setButtonBackVisible(false);

        addSlide(new SimpleSlide.Builder()
                .title("Organize suas contas")
                .description("Simples e fácil de usar")
                .image(R.drawable.um)
                .background(android.R.color.white)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Titulo2")
                .description("Descricao2")
                .image(R.drawable.dois)
                .background(android.R.color.white)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Titulo3")
                .description("Descricao3")
                .image(R.drawable.tres)
                .background(android.R.color.white)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Titulo4")
                .description("Descricao4")
                .image(R.drawable.quatro)
                .background(android.R.color.white)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.cadastro_fragment)
                .canGoForward(false)
                .build()
        );
    }

    public void btEntrar (View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void btCadastrar (View view){
        startActivity(new Intent(this, CadastroActivity.class));
    }

    public void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        if (autenticacao.getCurrentUser() != null){
            startActivity(new Intent(this, PrincipalActivity.class));
        }
    }

    @Override
    protected void onStart() {
        verificarUsuarioLogado();
        super.onStart();
    }
}
