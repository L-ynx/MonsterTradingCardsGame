package org.fhtw.application.router;

import org.fhtw.http.Request;
import org.fhtw.http.Response;

public interface Controller {
    Response response = new Response();
    Response process(Request request);
}
