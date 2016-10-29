package com.example.bruno.logindesigner;

/**
 * Created by Bruno on 14/10/2016.
 */

public class Utils {

    //Email Validation pattern
    //public static final String regEx = "\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\b";
    public static final String alphabetNumeric = "^[0-9a-zA-Z_.-@-]*$";
    public static final String alphabet= "^[a-zA-Zà-úÀ-Ú\\s]*$";

    //Fragments Tags
    public static final String LoginFragment = "LoginFragment";
    public static final String CadastroFragment = "CadastroFragment";
    public static final String ForgotPasswordFragment = "ForgotPasswordFragment";
}
