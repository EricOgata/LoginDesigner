package com.example.bruno.logindesigner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.ContentValues.TAG;

public class CadastroFragment extends Fragment {
    private static View view;
    private static Animation shakeAnimation;
    private Usuario usuario;
    RequestQueue mRequestQueue;
    GsonRequest<Usuario> myReq;

    @BindView(R.id.cadastro_nome)
    TextInputEditText txtNome;
    @BindView(R.id.cadastro_email)
    TextInputEditText txtEmail;
    @BindView(R.id.cadastro_senha)
    TextInputEditText txtSenha;
    @BindView(R.id.input_confirmar_senha)
    TextInputEditText txtConfirmarSenha;
    @BindView(R.id.btn_cadastrar)
    AppCompatButton btnCadastrar;
    @BindView(R.id.link_login)
    TextView linkLogin;
    @BindView(R.id.cadastrar_layout)
    LinearLayout cadastrarLayout;

    public CadastroFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_cadastro, container, false);
        view = inflater.inflate(R.layout.fragment_cadastro, container, false);

        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.link_login)
    public void cadastro() {
        // Replace signup fragment with animation
        MainActivity.replaceLoginFragment();
    }

    @OnClick(R.id.btn_cadastrar)
    void cadastrar() {

        if (!validate()) return;

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Ligth_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Criando a conta...");
        progressDialog.show();

        final String nome = txtNome.getText().toString().trim();
        final String email = txtEmail.getText().toString().trim();
        final String senha = txtSenha.getText().toString().trim();
        final String login = txtEmail.getText().toString().trim();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        mRequestQueue = Volley.newRequestQueue(getActivity());
                        Map<String,String> params = new HashMap<String,String>();
                        //params.put("Content-Type", "application/json;charset=utf-8");
                        //params.put(Config.KEY_SENHA, senha);

                        Usuario usuario = new Usuario();
                        usuario.setEmail(email);
                        usuario.setSenha(senha);
                        usuario.setNome(nome);
                        usuario.setLogin(login);
                        usuario.setAtivo(true);
                        usuario.setIdPerfil(0);
                        usuario.setIdPropriedade(0);

                        myReq   = new GsonRequest<Usuario>(
                                Request.Method.POST,
                                Config.CADASTRO_URL,
                                Usuario.class,
                                params,
                                createSuccessListener(), // listener for success
                                createErrorListener(),
                                usuario);  // listener for failure

                        myReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                        mRequestQueue.add(myReq);
                        // On complete call either onLoginSuccess or onLoginFailed
                        //onCadastroSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);

    }

    private Response.Listener<Usuario> createSuccessListener() {
        return new Response.Listener<Usuario>() {
            @Override
            public void onResponse(Usuario response) {
                mRequestQueue.stop();
                //Login obj = new Gson().toJson(response);
                Log.d("RESPONSE", response.toString());
            }
        };

    }

    private Response.ErrorListener createErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mRequestQueue.stop();
                Log.i(TAG, "Error : " + error.getLocalizedMessage());
            }
        };

    }

    private void onCadastroSuccess() {
        startActivity(new Intent(getActivity(), AppActivity.class));
        getActivity().finish();
        Toast.makeText(getActivity(), "Seja Bem Vindo", Toast.LENGTH_LONG).show();
    }

    private void onCadastroFailed() {
        new CustomToast().Show_Toast(getActivity(), view, "Falha no Cadastro!!!");
    }

    private boolean validate() {

        boolean valid = true;

        String nome = txtNome.getText().toString();
        String email = txtEmail.getText().toString().trim();
        String senha = txtSenha.getText().toString().trim();
        String confirmarSenha = txtConfirmarSenha.getText().toString().trim();

        Pattern pNome = Pattern.compile(Utils.alphabet);
        Matcher mNome = pNome.matcher(nome);

        Pattern pEmail = Pattern.compile(Utils.alphabetNumeric);
        Matcher mEMail = pEmail.matcher(email);

        // Valitadion do Cadastro

        txtEmail.setError(null);
        txtSenha.setError(null);

        if (nome.isEmpty() || nome.length() < 3) {
            cadastrarLayout.startAnimation(shakeAnimation);
            txtNome.requestFocus();
            new CustomToast().Show_Toast(getActivity(), view, "Insira seu nome completo!!!");
            valid = false;
        } else if (!mNome.find()) {
            cadastrarLayout.startAnimation(shakeAnimation);
            txtNome.requestFocus();
            new CustomToast().Show_Toast(getActivity(), view, "O nome possui caracteres inválidos. Digite somente letras!!!");
            valid = false;
        } else if (email.isEmpty() || email.length() < 3) {
            cadastrarLayout.startAnimation(shakeAnimation);
            txtEmail.requestFocus();
            new CustomToast().Show_Toast(getActivity(), view, "Campo de preenchimento obrigatório!!!");
            valid = false;
        } else if (!mEMail.find()) {
            cadastrarLayout.startAnimation(shakeAnimation);
            txtEmail.requestFocus();
            new CustomToast().Show_Toast(getActivity(), view, "O E-mail possui caracteres inválidos!!!");
            valid = false;
        } else if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            cadastrarLayout.startAnimation(shakeAnimation);
            txtEmail.requestFocus();
            new CustomToast().Show_Toast(getActivity(), view, "Insira um email válido");
            valid = false;
        } else if (senha.isEmpty() || senha.length() < 6) {
            cadastrarLayout.startAnimation(shakeAnimation);
            txtSenha.requestFocus();
            new CustomToast().Show_Toast(getActivity(), view, "Mínimo de 6 caracteres são necessários!!!");
            valid = false;
        } else if (confirmarSenha.isEmpty() || !(confirmarSenha.equals(senha))) {
            txtConfirmarSenha.requestFocus();
            new CustomToast().Show_Toast(getActivity(), view, "As senhas não são iguais!!!");
            valid = false;
        }

        return valid;
    }
}
