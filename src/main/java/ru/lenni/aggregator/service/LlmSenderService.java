package ru.lenni.aggregator.service;

import ru.lenni.aggregator.model.Task;

public interface LlmSenderService {

    void sendRequestToLlm(Task task);

    void resendErrorTask(Task task);
}
