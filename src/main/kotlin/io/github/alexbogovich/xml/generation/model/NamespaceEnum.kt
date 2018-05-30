package io.github.alexbogovich.xml.generation.model

import io.github.alexbogovich.xml.generation.shared.SharedConstants
import io.github.alexbogovich.xml.generation.shared.SharedConstants.initDate
import io.github.alexbogovich.xml.generation.shared.SharedConstants.taxonomyPath

enum class NamespaceEnum(val prefix: String, val link: String, val location: String, val internal: Boolean = false) {
    XSI("xsi", "http://www.w3.org/2001/XMLSchema-instance", "http://www.xbrl.org/2003/xbrl-instance-2003-12-31.xsd"),
    XSD("xsd", "http://www.w3.org/2001/XMLSchema", ""),
    XLINK("xlink", "http://www.w3.org/1999/xlink", ""),
    LINK("link", "http://www.xbrl.org/2003/linkbase", ""),
    XBRLI("xbrli", "http://www.xbrl.org/2003/instance", "http://www.xbrl.org/2003/xbrl-instance-2003-12-31.xsd"),
    XBRLDT("xbrldt", "http://xbrl.org/2005/xbrldt", "http://www.xbrl.org/2005/xbrldt-2005.xsd"),
    MODEL("model", "http://www.eurofiling.info/xbrl/ext/model", "http://www.eurofiling.info/eu/fr/xbrl/ext/model.xsd"),
    NONNUM("nonnum", "http://www.xbrl.org/dtr/type/non-numeric",
            "http://www.xbrl.org/dtr/type/nonNumeric-2009-12-16.xsd"),
    BALANCE_STATEMENT_MEM("cbr_BS", "http://$taxonomyPath/dict/dom/balanceStatement",
            "$taxonomyPath/dict/dom/balanceStatement/mem.xsd", true),
    BALANCE_STATEMENT_HIER("cbr_BS_h", "http://$taxonomyPath/dict/dom/balanceStatement/hier",
    "$taxonomyPath/dict/dom/balanceStatement/hier.xsd", true),
    EXPLICIT_DOMAINS("cbr_exp", "http://$taxonomyPath/dict/exp",
    "$taxonomyPath/dict/dom/exp.xsd", true),
    DIMENSIONS("cbr_dim", "http://$taxonomyPath/dict/dim",
            "$taxonomyPath/dict/dim/dim.xsd", true),

    ACCOUNT_GROUP_MEM("cbr_AG", "http://$taxonomyPath/dict/dom/accountGroup",
            "$taxonomyPath/dict/dom/accountGroup/mem.xsd", true),
    ACCOUNT_GROUP_HIER("cbr_AG_h", "http://$taxonomyPath/dict/dom/accountGroup/hier",
            "$taxonomyPath/dict/dom/accountGroup/hier.xsd", true),

    METRIC_MEM("cbr_MET", "http://$taxonomyPath/dict/met",
            "$taxonomyPath/dict/met/met.xsd", true),

    METRIC_HIER("cbr_MET_h", "http://$taxonomyPath/dict/met/hier",
            "$taxonomyPath/dict/met/hier.xsd", true),

    FORM_101("form_101", "http://$taxonomyPath/rep/$initDate/tab/form_101",
            "$taxonomyPath/rep/$initDate/tab/form_101/form_101.xsd", true),
    EP_COMMON("ep_common", "http://$taxonomyPath/rep/$initDate/ep/ep_common",
            "$taxonomyPath/rep/$initDate/ep/ep_common.xsd", true)
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