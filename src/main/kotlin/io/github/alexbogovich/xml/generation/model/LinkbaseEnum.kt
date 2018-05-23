package io.github.alexbogovich.xml.generation.model

import shared.SharedConstants
import shared.SharedConstants.initDate
import shared.SharedConstants.taxonomyPath

enum class LinkbaseEnum(val relatedPath: String) {
    BALANCE_STATEMENT_HIER_DEF("$taxonomyPath/dict/dom/balanceStatement/hier-def.xml"),
    ACCOUNT_GROUP_HIER_DEF("$taxonomyPath/dict/dom/accountGroup/hier-def.xml"),
    METRIC_HIER_DEF("$taxonomyPath/dict/met/hier-def.xml"),
    DIMENSIONS_DEF("$taxonomyPath/dict/dim/dim-def.xml"),
    FORM_101_DEF("$taxonomyPath/rep/$initDate/tab/form_101/form_101-def.xml")
}