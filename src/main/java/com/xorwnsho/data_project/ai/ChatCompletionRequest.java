package com.xorwnsho.data_project.ai;

import java.util.List;

public record ChatCompletionRequest(String model, List<ChatMessage> messages, double temperature) {
}
