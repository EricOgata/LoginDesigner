package com.example.bruno.logindesigner;

/**
 * Created by SENAI on 22/10/2016.
 */

public class Config {
    //URL to our


    public static final String LOGIN_URL = "http://192.168.2.233/BravosWebServices/consultarUsuario";

    public static final String CADASTRO_URL = "http://192.168.2.233/BravosWebServices/cadastrarUsuario";

    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_EMAIL = "email";
    public static final String KEY_SENHA = "password";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the email of current logged in user
    public static final String EMAIL_SHARED_PREF = "email";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

}
