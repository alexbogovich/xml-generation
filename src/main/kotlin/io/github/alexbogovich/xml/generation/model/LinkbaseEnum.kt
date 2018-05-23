package io.github.alexbogovich.xml.generation.model

import shared.SharedConstants

enum class LinkbaseEnum(val relatedPath: String) {
    BALANCE_STATEMENT_HIER_DEF("www.cbr-prototype.com/xbrl/fin/dict/dom/balanceStatement/hier-def.xml"),
    ACCOUNT_GROUP_HIER_DEF("www.cbr-prototype.com/xbrl/fin/dict/dom/accountGroup/hier-def.xml"),
    METRIC_HIER_DEF("www.cbr-prototype.com/xbrl/fin/dict/met/hier-def.xml"),
    DIMENSIONS_DEF("www.cbr-prototype.com/xbrl/fin/dict/dim/dim-def.xml"),
    FORM_101_DEF("www.cbr-prototype.com/xbrl/fin/rep/${SharedConstants.initDate}/tab/form_101/form_101-def.xml")
//    http://www.cbr-prototype.com/xbrl/fin/rep/${SharedConstants.initDate}/ep/ep_fin_form_101-def.xml
}