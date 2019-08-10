package com.rotciv.organizze.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rotciv.organizze.R;
import com.rotciv.organizze.activity.config.ConfiguracaoFirebase;
import com.rotciv.organizze.activity.helper.Base64Custom;
import com.rotciv.organizze.activity.helper.DateUtil;
import com.rotciv.organizze.activity.model.Movimentacao;
import com.rotciv.organizze.activity.model.Usuario;

public class ReceitaActivity extends AppCompatActivity {

    private EditText editData, editCategoria, editDescricao, editValor;

    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();

    private Double receitaTotal, receitaGerada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita);

        editData = findViewById(R.id.editData);
        editCategoria = findViewById(R.id.editCategoria);
        editDescricao = findViewById(R.id.editDescricao);
        editValor = findViewById(R.id.editValor);

        //seta data com data atual
        editData.setText(DateUtil.dataAtual());

        recuperarReceitaTotal();
    }

    public void adicionarReceita(View view){
        if (checaCampos()){
            String idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());

            String data = editData.getText().toString();
            String dataReduzida = DateUtil.reduzData(data);

            movimentacao = new Movimentacao();

            movimentacao.setTipo("r");
            movimentacao.setDescricao(editDescricao.getText().toString());
            movimentacao.setCategoria(editCategoria.getText().toString());
            movimentacao.setValor(Double.parseDouble(editValor.getText().toString()));
            movimentacao.setData(data);

            firebaseRef.child("movimentacao")
                    .child(idUsuario)
                    .child(dataReduzida)
                    .push()
                    .setValue(movimentacao);

            receitaGerada = movimentacao.getValor();
            atualizaReceitaTotal(idUsuario);
            finish();
        }
    }

    public boolean checaCampos(){
        String campoData = editData.getText().toString();
        String campoCategoria = editCategoria.getText().toString();
        String campoDescricao = editDescricao.getText().toString();
        String campoValor = editValor.getText().toString();

        if (campoCategoria.isEmpty() || campoData.isEmpty() || campoData.isEmpty() || campoDescricao.isEmpty()){
            Toast.makeText(ReceitaActivity.this, "Preencha todos os campos!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void atualizaReceitaTotal(String idUsuario){
        receitaTotal = receitaTotal + receitaGerada;

        firebaseRef.child("usuarios").child(idUsuario).child("receitaTotal").setValue(receitaTotal);
    }

    public void recuperarReceitaTotal(){
        String idUsuario = autenticacao.getCurrentUser().getEmail();
        idUsuario = Base64Custom.codificarBase64(idUsuario);

        DatabaseReference usuarioRef = firebaseRef.child("usuarios")
                .child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
