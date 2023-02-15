package org.fhtw.application.router;

import org.fhtw.application.model.ModelSerializer;
import org.fhtw.http.Request;
import org.fhtw.http.Response;

public interface Controller {
    Response response = new Response();
    ModelSerializer serializer = new ModelSerializer();
    Response process(Request request);
}
