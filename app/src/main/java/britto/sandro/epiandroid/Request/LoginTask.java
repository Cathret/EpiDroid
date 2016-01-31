package britto.sandro.epiandroid.Request;

import android.media.session.MediaSession;

import org.springframework.util.LinkedMultiValueMap;

/**
 * Created by Sandro on 30/01/2016.
*/
public class LoginTask extends LinkedMultiValueMap<String, String> {
    public LoginTask(String login, String passwd) {
        add("login", login);
        add("password", passwd);
}
}
