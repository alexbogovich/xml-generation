package io.github.alexbogovich.xml.generation.model

import java.nio.file.Path

enum class NamespaceEnum(val prefix: String, val link: String, val location: String) {
    XSI("xsi", "http://www.w3.org/2001/XMLSchema-instance", "http://www.xbrl.org/2003/xbrl-instance-2003-12-31.xsd"),
    XSD("xsd", "http://www.w3.org/2001/XMLSchema", ""),
    XLINK("xlink", "http://www.w3.org/1999/xlink", ""),
    LINK("link", "http://www.xbrl.org/2003/linkbase", ""),
    XBRLI("xbrli", "http://www.xbrl.org/2003/instance", ""),
    MODEL("model", "http://www.eurofiling.info/xbrl/ext/model", "http://www.eurofiling.info/eu/fr/xbrl/ext/model.xsd"),
    NONNUM("nonnum", "http://www.xbrl.org/dtr/type/non-numeric", "http://www.xbrl.org/dtr/type/nonNumeric-2009-12-16.xsd")
}

enum class XbrlSubstitutionGroup(val value: String) {
    ITEM("xbrli:item")
}

data class XsdElement(val id: String, val name: String, val xsdPath: Path, val xsdNamespace: String)
enum class ArcRole(val url: String) {
    DOMAIN_MEMBER("http://xbrl.org/int/dim/arcrole/domain-member"),
    DIMENSION_DOMAIN("http://xbrl.org/int/dim/arcrole/dimension-domain")
}

enum class ArcroleRef(val arcroleURI: String, val href: String) {
    ALL("http://xbrl.org/int/dim/arcrole/all",
            "http://www.xbrl.org/2005/xbrldt-2005.xsd#all"),
    NOT_ALL("http://xbrl.org/int/dim/arcrole/notAll",
            "http://www.xbrl.org/2005/xbrldt-2005.xsd#notAll"),
    DIMENSION_DEFAULT("http://xbrl.org/int/dim/arcrole/dimension-default",
            "http://www.xbrl.org/2005/xbrldt-2005.xsd#dimension-default"),
    DIMENSION_DOMAIN("http://xbrl.org/int/dim/arcrole/dimension-domain",
            "http://www.xbrl.org/2005/xbrldt-2005.xsd#dimension-domain"),
    DOMAIN_MEMBER("http://xbrl.org/int/dim/arcrole/domain-member",
            "http://www.xbrl.org/2005/xbrldt-2005.xsd#domain-member"),
    HYPERCUBE_DIMENSION("http://xbrl.org/int/dim/arcrole/hypercube-dimension",
            "http://www.xbrl.org/2005/xbrldt-2005.xsd#hypercube-dimension")
}

data class Location(val href: String, val label: String)
data class DefinitionArc(val arcrole: ArcRole, val from: String, val to: String, val order: String = "", val targetRole: String = "")
data class DefinitionLink(val role: String, val id: String)
data class RoleRef(val href: String, val roleURI: String) // use URI?
enum class XbrlPeriodAttr(val value: String) {
    INSTANT("instant"),
    DURATION("duration")
}

enum class XbrlPeriodType(val value: String) {
    DOMAIN_ITEM_TYPE("nonnum:domainItemType"),
}