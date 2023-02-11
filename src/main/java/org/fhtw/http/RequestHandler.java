package org.fhtw.http;

import org.fhtw.application.router.Controller;
import org.fhtw.application.router.Router;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private final Socket socket;
    private final Router router;
    private BufferedReader br;
    private OutputStream out;

    public RequestHandler(Socket socket, Router router) {
        this.socket = socket;
        this.router = router;
    }

    @Override
    public void run() {
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Request request = new Request(br);
            Response response = new Response();

            Controller controller = router.route(request.getPath());
            if (controller != null) {
                response = controller.process(request);
            }
            out = socket.getOutputStream();
            out.write(response.responseBuilder().getBytes());
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeClient();
        }
    }


    private void closeClient() {
        try {
            if (br != null)
                br.close();
            if (out != null)
                out.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
