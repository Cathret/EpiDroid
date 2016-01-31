package britto.sandro.epiandroid.Request;

import org.springframework.util.LinkedMultiValueMap;

/**
 * Created by Sandro on 30/01/2016.
 */

public class InfosTask extends LinkedMultiValueMap<String, String> {
    public InfosTask(String token) {
        add("token", token);
    }
}
