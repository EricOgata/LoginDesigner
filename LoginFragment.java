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

/**
 *
 */

public class LoginFragment extends Fragment {
    private static View view;
    private static Animation shakeAnimation;
    private Login loginClass;
    RequestQueue mRequestQueue;
    GsonRequest<Login> myReq;

    @BindView(R.id.input_email)
    TextInputEditText txtEmail;
    @BindView(R.id.input_senha)
    TextInputEditText txtSenha;
    @BindView(R.id.btn_login)
    AppCompatButton btnLogin;
    @BindView(R.id.link_cadastro)
    TextView linkCadastro;
    @BindView(R.id.login_layout)
    LinearLayout loginLayout;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);

        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.link_cadastro)
    public void cadastro() {
        // Replace signup fragment with animation
        MainActivity.replaceCadastroFragment();
    }

    @OnClick(R.id.link_recuperar_senha)
    public void recuperarSenha() {
        // Replace forgout fragment with animation
        MainActivity.replaceRecuperarSenhaFragment();
    }

    @OnClick(R.id.btn_login)
    void login() {

        if (!validate()) return;
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Ligth_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Autenticando...");
        progressDialog.show();

        final String login = txtEmail.getText().toString().trim();
        final String senha = txtSenha.getText().toString().trim();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        mRequestQueue = Volley.newRequestQueue(getActivity());
                        Map<String,String> params = new HashMap<String,String>();
                        //params.put("Content-Type", "application/json;charset=utf-8");
                        //params.put(Config.KEY_SENHA, senha);

                        Login loginClass = new Login();
                        loginClass.setLogin(login);
                        loginClass.setSenha(senha);

                        myReq   = new GsonRequest<Login>(
                                Request.Method.POST,
                                Config.LOGIN_URL,
                                Login.class,
                                params,
                                createSuccessListener(), // listener for success
                                createErrorListener(),
                                login);  // listener for failure

                        myReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                        mRequestQueue.add(myReq);
                        //mRequestQueue.cancelAll(myReq);


                        // On complete call either onLoginSuccess or onLoginFailed
                        //onLoginSuccess();
                        //onLoginFailed();
                        progressDialog.dismiss();
                    }


                }, 3000);

    }

    private Response.Listener<Login> createSuccessListener() {
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

    }

    private void onLoginSuccess() {
        startActivity(new Intent(getActivity(), AppActivity.class));
        getActivity().finish();
        Toast.makeText(getActivity(), "Seja Bem Vindo", Toast.LENGTH_LONG).show();
    }

    private void onLoginFailed() {
        new CustomToast().Show_Toast(getActivity(), view, "Falha no Login!!!");
    }

    private boolean validate() {

        boolean valid = true;

        String email = txtEmail.getText().toString();
        String senha = txtSenha.getText().toString();

        Pattern pEmail = Pattern.compile(Utils.alphabetNumeric);
        Matcher mEMail = pEmail.matcher(email);

        // Valitadion Senha e Email

        if (email.isEmpty()) {
            loginLayout.startAnimation(shakeAnimation);
            txtEmail.requestFocus();
            new CustomToast().Show_Toast(getActivity(), view, "Campo de preenchimento obrigatório!!!");
            valid = false;
        } else if (!mEMail.find()) {
            loginLayout.startAnimation(shakeAnimation);
            txtEmail.requestFocus();
            new CustomToast().Show_Toast(getActivity(), view, "O E-mail possui caracteres inválidos!!!");
            valid = false;
        } else if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginLayout.startAnimation(shakeAnimation);
            txtEmail.requestFocus();
            new CustomToast().Show_Toast(getActivity(), view, "Insira um email cadastrado válido");
            valid = false;
        } else if (senha.isEmpty() || senha.length() < 6) {
            loginLayout.startAnimation(shakeAnimation);
            txtSenha.requestFocus();
            new CustomToast().Show_Toast(getActivity(), view, "Mínimo de 6 caracteres são necessários!!!");
            valid = false;
        } else {
            txtEmail.setError(null);
            txtSenha.setError(null);
        }

        return valid;
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
