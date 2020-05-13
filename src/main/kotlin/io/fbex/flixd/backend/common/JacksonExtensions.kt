package io.fbex.flixd.backend.common

import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDate

fun JsonNode.asLocalDate(): LocalDate = LocalDate.parse(asText())

fun JsonNode.asLocalDateOrNull(): LocalDate? =
    asText().takeUnless { it.isBlank() || it == "null" }?.let { LocalDate.parse(it) }

fun JsonNode.asTextOrNull(): String? =
    asText().takeUnless { it.isBlank() || it == "null" }
