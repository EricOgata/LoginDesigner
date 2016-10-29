package com.example.bruno.logindesigner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();

        // If savedinstnacestate is null then replace login fragment
        if (savedInstanceState == null) {
            replaceLoginFragment();
        }
    }

    // Adiciona o fragmento ao centro da tela
    protected static void replaceFragment(int animEnter, int animOut, Fragment fragment, String tag){
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(animEnter, animOut)
                .replace(R.id.frame_container, fragment, tag)
                .commit();
    }

    // Adiciona o fragmento de LOGIN
    protected static void replaceLoginFragment(){
        replaceFragment(R.anim.right_enter, R.anim.left_out, new LoginFragment(), Utils.LoginFragment);
    }

    // Adiciona o fragmento de Cadastro
    protected static void replaceCadastroFragment(){
        replaceFragment(R.anim.left_enter, R.anim.right_out, new CadastroFragment(), Utils.CadastroFragment);
    }

    // Adiciona o fragmento de Recuperar Senha
    protected static void replaceRecuperarSenhaFragment(){
        replaceFragment(R.anim.right_enter, R.anim.right_out, new RecuperarSenhaFragment(), Utils.CadastroFragment);
    }

    @Override
    public void onBackPressed() {

        // Find the tag of signup and forgot password fragment
        Fragment CadastroFragment = fragmentManager
                .findFragmentByTag(Utils.CadastroFragment);
        Fragment ForgotPasswordFragment = fragmentManager
                .findFragmentByTag(Utils.ForgotPasswordFragment);

        // Check if both are null or not
        // If both are not null then replace login fragment else do backpressed
        // task
        if (CadastroFragment != null)
            replaceLoginFragment();
        else if (ForgotPasswordFragment != null)
            replaceLoginFragment();
        else
            super.onBackPressed();
    }
}
