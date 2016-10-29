package com.example.bruno.logindesigner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
import static com.example.bruno.logindesigner.R.string.senha;

public class RecuperarSenhaFragment extends Fragment {
    private static View view;
    private static Animation shakeAnimation;
    RequestQueue mRequestQueue;
    GsonRequest<String> myReq;

    @BindView(R.id.registred_email)
    TextInputEditText txtEmail;
    @BindView(R.id.btn_enviar)
    TextView btnEnviar;
    @BindView(R.id.btn_voltar)
    TextView btnVoltar;
    @BindView(R.id.recuperar_senha_layout)
    LinearLayout recuperarSenhaLayout;

    public RecuperarSenhaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_recuperar_senha, container, false);
        view = inflater.inflate(R.layout.fragment_recuperar_senha, container, false);

        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_voltar)
    public void recuperarSenha() {
        // Replace forgout fragment with animation
        MainActivity.replaceLoginFragment();
    }

    @OnClick(R.id.btn_enviar)
    void enviar() {

        if (!validate()) return;

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Ligth_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Recuperando senha...");
        progressDialog.show();

        String email = txtEmail.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        mRequestQueue = Volley.newRequestQueue(getActivity());
                        Map<String,String> params = new HashMap<String,String>();
                        //params.put("Content-Type", "application/json;charset=utf-8");
                        //params.put(Config.KEY_SENHA, senha);

                        myReq   = new GsonRequest<String>(
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
                        onSenhaRecuperadaSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);

    }

    private Response.Listener<String> createSuccessListener() {
        return new Response.Listener<Login>() {
            @Override
            public void onResponse(Login response) {
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


    private void onSenhaRecuperadaSuccess() {
        MainActivity.replaceLoginFragment();
        Toast.makeText(getActivity(), "Senha Recuperada com Sucesso. Verifique seu email!!!!", Toast.LENGTH_LONG).show();
    }

    private void onSenhaRecuperadaFailed() {
        new CustomToast().Show_Toast(getActivity(), view, "Falha ao acessar o servidor!!!");
    }

    private boolean validate() {

        boolean valid = true;

        String email = txtEmail.getText().toString();

        Pattern pEmail = Pattern.compile(Utils.alphabetNumeric);
        Matcher mEMail = pEmail.matcher(email);

        // Valitadion Email

        if (email.isEmpty()) {
            recuperarSenhaLayout.startAnimation(shakeAnimation);
            txtEmail.requestFocus();
            new CustomToast().Show_Toast(getActivity(), view, "Campo de preenchimento obrigatório!!!");
            valid = false;
        } else if (!mEMail.find()) {
            recuperarSenhaLayout.startAnimation(shakeAnimation);
            txtEmail.requestFocus();
            new CustomToast().Show_Toast(getActivity(), view, "O E-mail possui caracteres inválidos!!!");
            valid = false;
        } else if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            recuperarSenhaLayout.startAnimation(shakeAnimation);
            txtEmail.requestFocus();
            new CustomToast().Show_Toast(getActivity(), view, "Insira um email cadastrado válido");
            valid = false;
        } else {
            txtEmail.setError(null);
        }

        return valid;
    }
}
