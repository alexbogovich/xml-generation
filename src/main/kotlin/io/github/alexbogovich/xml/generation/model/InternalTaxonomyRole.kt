package io.github.alexbogovich.xml.generation.model

enum class InternalTaxonomyRole(val id: String,
                                val roleName: String,
                                val taxomomyPath: String,
                                val defLink: Boolean = true,
                                val labelLink: Boolean = false) {
    BS_DOM_IDCO("role_groupIncDebCreOut",
            "http://www.cbr-prototype.com/xbrl/fin/dict/dom/balanceStatement/groupIncDebCreOut",
            NamespaceEnum.BALANCE_STATEMENT_HIER.location),
}