package com.ms.coco.echo.controller;

import org.restexpress.Request;
import org.restexpress.Response;

public class SuccessController extends AbstractDelayingController {
    public Object create(Request request, Response response) {
        long delayms = delay(request);
        response.setResponseCreated();
        String message = request.getHeader("echo");
        return new DelayResponse("create", delayms, message);
    }

    public Object read(Request request, Response response) {
        long delayms = delay(request);
        String message = request.getHeader("echo");
        return new DelayResponse("read", delayms, message);
    }

    public Object readAll(Request request, Response response) {
        long delayms = delay(request);
        String message = "read all " + request.getHeader("echo");
        return new DelayResponse("post->create", delayms, message);
    }

    public Object update(Request request, Response response) {
        long delayms = delay(request);
        String message = request.getHeader("echo");
        return new DelayResponse("update", delayms, message);
    }

    public Object delete(Request request, Response response) {
        long delayms = delay(request);
        String message = request.getHeader("echo");
        return new DelayResponse("delete", delayms, message);
    }
}