package io.fbex.flixd.backend.common

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import org.springframework.http.HttpStatus

private val objectMapper = ObjectMapper().findAndRegisterModules()

fun OkHttpClient.call(url: String, block: Request.Builder.() -> Unit): Response {
    val request = Request.Builder().url(url).apply(block).build()
    return newCall(request).execute()
}

fun Request.Builder.queryParameters(block: QueryParamDsl.() -> Unit) {
    val url = this.build().url
    val urlWithParams = QueryParamDsl(url).apply(block).execute()
    url(urlWithParams)
}

fun <T> ResponseBody?.parseAs(clazz: Class<T>): T = when {
    this != null -> objectMapper.readValue(string(), clazz)
    else -> error("Received no response body. Could not parse.")
}

val Response.httpStatus
    get() = HttpStatus.valueOf(code)

val Response.isNotFound: Boolean
    get() = httpStatus == HttpStatus.NOT_FOUND

class QueryParamDsl(private val url: HttpUrl) {

    private val parameters = mutableMapOf<String, String>()

    fun addParameter(name: String, value: String) {
        parameters[name] = value
    }

    fun execute(): HttpUrl {
        val builder = url.newBuilder()
        parameters.forEach { builder.addQueryParameter(it.key, it.value) }
        return builder.build()
    }
}
