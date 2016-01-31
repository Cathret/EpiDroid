package britto.sandro.epiandroid;

import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.json.JSONArray;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;

import britto.sandro.epiandroid.Request.InfosTask;
import britto.sandro.epiandroid.Request.LoginTask;

/**
 * Created by Sandro on 30/01/2016.
 */

@Rest(rootUrl="https://epitech-api.herokuapp.com/", converters = {StringHttpMessageConverter.class, FormHttpMessageConverter.class})

public interface MyRequest extends RestClientErrorHandling {
    @Post("/login")
    String Login(LoginTask log);
    @Post("/infos")
    String Info(InfosTask info);
    @Get("/messages?token={token}")
    String Notif (String token);
    @Get("/marks?token={token}")
    String Mark (String token);
}
