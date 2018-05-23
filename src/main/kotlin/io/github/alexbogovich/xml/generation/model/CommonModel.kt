package io.github.alexbogovich.xml.generation.model

import java.nio.file.Path

enum class XbrlSubstitutionGroup(val value: String) {
    ITEM("xbrli:item"),
    DIMENSION_ITEM("xbrldt:dimensionItem")
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
data class DefinitionLink(val role: String, var id: String = "")
data class RoleRef(val href: String, val roleURI: String) // use URI?
enum class XbrlPeriodAttr(val value: String) {
    INSTANT("instant"),
    DURATION("duration")
}

enum class XbrlPeriodType(val value: String) {
    DOMAIN_ITEM_TYPE("nonnum:domainItemType"),
    STRING_ITEM_TYPE("xbrli:stringItemType"),
    EXPLICIT_DOMAIN_TYPE("model:explicitDomainType"),
}

enum class LinkBaseRefType(val arcRole: String, val role: String) {
    DEFINITION("http://www.w3.org/1999/xlink/properties/linkbase", "http://www.xbrl.org/2003/role/definitionLinkbaseRef")
}

data class XbrlRole(val id: String,
                    val role: String,
                    val path: Path,
                    val defLink: Boolean = true,
                    val labelLink: Boolean = true)