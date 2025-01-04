package org.pandas.bambooclub.domain.chat.dto

data class OpenAIResponse(
    val id: String? = null,
    val `object`: String? = null,
    val created: Long? = null,
    val model: String? = null,
    val choices: List<Choice>? = null,
    val usage: Usage? = null,
    val systemFingerprint: String? = null,
)

data class Choice(
    val index: Int? = null,
    val message: Message? = null,
    val logprobs: Any? = null,
    val finishReason: String? = null,
)

data class Message(
    val role: String? = null,
    val content: String? = null,
    val refusal: Any? = null,
)

data class Usage(
    val promptTokens: Int? = null,
    val completionTokens: Int? = null,
    val totalTokens: Int? = null,
    val promptTokensDetails: TokenDetails? = null,
    val completionTokensDetails: TokenDetails? = null,
)

data class TokenDetails(
    val cachedTokens: Int? = null,
    val audioTokens: Int? = null,
    val reasoningTokens: Int? = null,
    val acceptedPredictionTokens: Int? = null,
    val rejectedPredictionTokens: Int? = null,
)
