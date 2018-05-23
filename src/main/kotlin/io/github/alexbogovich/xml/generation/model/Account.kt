package io.github.alexbogovich.xml.generation.model

data class Account(val group: Group, val number: String, val type: Type, val title: String) {
    enum class Type {
        ACTIVE,
        PASSIVE,
        UNKNOWN;
    }

    enum class Group {
        BALANCE,
        TRUST,
        OFFBALANCE,
        OTHER
    }
}