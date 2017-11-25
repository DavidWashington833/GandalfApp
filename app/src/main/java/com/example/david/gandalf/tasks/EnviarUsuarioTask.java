package com.example.david.gandalf.tasks;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.david.gandalf.LoginActivity;
import com.example.david.gandalf.CadastroActivity;
import com.example.david.gandalf.WebClient;
import com.example.david.gandalf.models.Cliente;
import com.example.david.gandalf.models.ClienteDTO;
import com.example.david.gandalf.models.Login;
import com.google.gson.Gson;

/**
 * Created by felipe.amiconi on 22/11/2017.
 */

public class EnviarUsuarioTask extends AsyncTask<Void, Void, String> {
    private CadastroActivity context;
    private ProgressDialog dialog;

    public EnviarUsuarioTask(CadastroActivity context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, "Aguarde", "Cadastrando cliente...", true, true);
    }

    @Override
    protected String doInBackground(Void... params) {
        ClienteDTO clienteDTO = new ClienteDTO(context.getCliente());
        Gson gson = new Gson();
        String jsonInString = gson.toJson(clienteDTO);
        WebClient client = new WebClient();
        String resposta = client.post("http://gandalf.azurewebsites.net/gandalf/rest/cliente/", jsonInString);
        return resposta;
    }

    @Override
    protected void onPostExecute(String resposta) {
        dialog.dismiss();

        if (!resposta.equals("null")) {
            Cliente cliente = new Gson().fromJson(resposta, Cliente.class);

            SharedPreferences preferences = context.getSharedPreferences("login", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("idCliente", cliente.getIdCliente());
            editor.commit();

            context.getCliente().setIdCliente(cliente.getIdCliente());

            new EnviaEnderecoTask(context, cliente.getIdCliente()).execute();
        }
    }
}