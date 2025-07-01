package com.jacob.fruitoftek.kotlinschoolmanagement.model

data class ApiResponse(
    val message: String? = null,
    val user: User? = null,
    val error: String? = null,
    val status: String? = null
)