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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rotciv.organizze.R;
import com.rotciv.organizze.activity.config.ConfiguracaoFirebase;
import com.rotciv.organizze.activity.helper.Base64Custom;
import com.rotciv.organizze.activity.helper.DateUtil;
import com.rotciv.organizze.activity.model.Movimentacao;
import com.rotciv.organizze.activity.model.Usuario;

public class DespesaActivity extends AppCompatActivity {

    private EditText editData, editCategoria, editDescricao, editValor;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();
    private Double despesaTotal;
    private Double despesaGerada;
    private Double despesaAtualizada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

        editData = findViewById(R.id.editData);
        editCategoria = findViewById(R.id.editCategoria);
        editDescricao = findViewById(R.id.editDescricao);
        editValor = findViewById(R.id.editValor);

        //preenche campo Data com data atual
        editData.setText(DateUtil.dataAtual());

        recuperarDespesaTotal();
    }

    public void salvarDespesa (View view){

        if (validaCampos()) {
            movimentacao = new Movimentacao();
            String data = editData.getText().toString();
            despesaGerada = Double.parseDouble(editValor.getText().toString());

            movimentacao.setValor(despesaGerada);
            movimentacao.setCategoria(editCategoria.getText().toString());
            movimentacao.setDescricao(editDescricao.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("d"); //despesa

            despesaAtualizada = despesaTotal + despesaGerada;
            atualizaDespesa();

            movimentacao.salvar(data);
            finish();
        } else {
            Toast.makeText(DespesaActivity.this, "Preencha todos os campos!", Toast.LENGTH_LONG).show();
        }
    }

    public Boolean validaCampos(){
        String textoData = editData.getText().toString();
        String textoCategoria = editCategoria.getText().toString();
        String textoDescricao = editDescricao.getText().toString();
        String textoValor = editValor.getText().toString();

        if (textoData.isEmpty() || textoCategoria.isEmpty() || textoDescricao.isEmpty() || textoValor.isEmpty())
            return false;

        return true;
    }

    public void recuperarDespesaTotal(){
        String idUsuario = autenticacao.getCurrentUser().getEmail();
        idUsuario = Base64Custom.codificarBase64(idUsuario);

        DatabaseReference usuarioRef = firebaseRef.child("usuarios")
                                                  .child(idUsuario);
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void atualizaDespesa(){
        String idUsuario = autenticacao.getCurrentUser().getEmail();
        idUsuario = Base64Custom.codificarBase64(idUsuario);

        DatabaseReference usuarioRef = firebaseRef.child("usuarios")
                .child(idUsuario);
        usuarioRef.child("despesaTotal").setValue(despesaAtualizada);
    }
}
