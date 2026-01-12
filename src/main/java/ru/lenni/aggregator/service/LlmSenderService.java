package ru.lenni.aggregator.service;

import ru.lenni.aggregator.model.Request;

public interface LlmSenderService {

    void sendRequestToLlm(Request request);

    void resendErrorTask(Request request);
}
