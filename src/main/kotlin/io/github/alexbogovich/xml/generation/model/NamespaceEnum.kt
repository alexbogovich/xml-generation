package io.github.alexbogovich.xml.generation.model

enum class NamespaceEnum(val prefix: String, val link: String, val location: String, val internal: Boolean = false) {
    XSI("xsi", "http://www.w3.org/2001/XMLSchema-instance", "http://www.xbrl.org/2003/xbrl-instance-2003-12-31.xsd"),
    XSD("xsd", "http://www.w3.org/2001/XMLSchema", ""),
    XLINK("xlink", "http://www.w3.org/1999/xlink", ""),
    LINK("link", "http://www.xbrl.org/2003/linkbase", ""),
    XBRLI("xbrli", "http://www.xbrl.org/2003/instance", "http://www.xbrl.org/2003/xbrl-instance-2003-12-31.xsd"),
    XBRLDT("xbrldt", "http://xbrl.org/2005/xbrldt", "http://www.xbrl.org/2005/xbrldt-2005.xsd"),
    MODEL("model", "http://www.eurofiling.info/xbrl/ext/model", "http://www.eurofiling.info/eu/fr/xbrl/ext/model.xsd"),
    NONNUM("nonnum",
            "http://www.xbrl.org/dtr/type/non-numeric",
            "http://www.xbrl.org/dtr/type/nonNumeric-2009-12-16.xsd"),
    BALANCE_STATEMENT_MEM("cbr_BS",
            "http://www.cbr-prototype.com/xbrl/fin/dict/dom/balanceStatement",
            "www.cbr-prototype.com/xbrl/fin/dict/dom/balanceStatement/mem.xsd", true),
    BALANCE_STATEMENT_HIER("cbr_BS_h",
    "http://www.cbr-prototype.com/xbrl/fin/dict/dom/balanceStatement/hier",
    "www.cbr-prototype.com/xbrl/fin/dict/dom/balanceStatement/hier.xsd", true),
    EXPLICIT_DOMAINS("cbr_exp",
    "http://www.cbr-prototype.com/xbrl/fin/dict/exp",
    "www.cbr-prototype.com/xbrl/fin/dict/dom/exp.xsd", true),
    DIMENSIONS("cbr_dim",
            "http://www.cbr-prototype.com/xbrl/fin/dict/dim",
            "www.cbr-prototype.com/xbrl/fin/dict/dim/dim.xsd", true),

    ACCOUNT_GROUP_MEM("cbr_AG",
            "http://www.cbr-prototype.com/xbrl/fin/dict/dom/accountGroup",
            "www.cbr-prototype.com/xbrl/fin/dict/dom/accountGroup/mem.xsd", true),
    ACCOUNT_GROUP_HIER("cbr_AG_h",
            "http://www.cbr-prototype.com/xbrl/fin/dict/dom/accountGroup/hier",
            "www.cbr-prototype.com/xbrl/fin/dict/dom/accountGroup/hier.xsd", true),

    METRIC_MEM("cbr_MET",
            "http://www.cbr-prototype.com/xbrl/fin/dict/met",
            "www.cbr-prototype.com/xbrl/fin/dict/met/met.xsd", true),

    METRIC_HIER("cbr_MET_h",
            "http://www.cbr-prototype.com/xbrl/fin/dict/met/hier",
            "www.cbr-prototype.com/xbrl/fin/dict/met/hier.xsd", true),

    ;

    companion object {
        fun getByLink(link: String): NamespaceEnum {
            NamespaceEnum.values().forEach {
                if (it.link == link)
                    return it
            }
            throw RuntimeException("Not such namespace in NamespaceEnum $link")
        }
    }
}