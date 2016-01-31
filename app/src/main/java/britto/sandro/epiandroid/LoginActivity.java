package britto.sandro.epiandroid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.rest.RestService;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import britto.sandro.epiandroid.Request.LoginTask;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    public String Token;
    private String response;


    @RestService
    MyRequest API;

    @ViewById
    AutoCompleteTextView vlogin;
    @ViewById
    EditText vpassword;
    @UiThread
    void settingFieldError() {
        vlogin.setError(null);
        vpassword.setError(null);
    }

    @UiThread
    void setLoginRequired() {
        vlogin.setError(getString(R.string.error_field_required));
    }
    @UiThread
    void setWrongLoginRequired() {
        vlogin.setError(getString(R.string.error_wlogin_required));
    }
    @UiThread
    void setPasswdRequired() {
        vpassword.setError(getString(R.string.error_field_required));
    }

    private boolean isLoginValid(String Login) {
        if (Login.length() > 8 || Login.indexOf('_') == -1)
            return (false);
        return (true);
    }

    @UiThread
    void setView(View focusView) {
        focusView.requestFocus();
    }

    @Background
    void attemptLogin() {
        String login = vlogin.getText().toString();
        String passwd = vpassword.getText().toString();

        View focusView = null;

        boolean cancel = false;

        settingFieldError();

        if (TextUtils.isEmpty(passwd)) {
            setPasswdRequired();
            focusView = vpassword;
            cancel = true;
        }
        if (TextUtils.isEmpty(login)) {
            setLoginRequired();
            focusView = vlogin;
            cancel = true;
        } else if (!isLoginValid(login)) {
            setWrongLoginRequired();
            focusView = vlogin;
            cancel = true;
        }
        if (cancel)
            setView(focusView);
        else {
            LoginTask log = new LoginTask(login, passwd);
            tokenParsingExecute(API.Login(log));
            Intent intent = new Intent(this, MainActivity_.class);
            intent.putExtra("token", Token);
            startActivity(intent);
        }
    }

     protected void tokenParsingExecute(String response) {
        JSONObject json;
        try {
            json = new JSONObject(response);
            if (json.has("token")) {
                this.Token = json.getString("token");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.login_sign_in_button)
    void SignInClicked() {
         attemptLogin();
        }
}

