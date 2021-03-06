package io.github.alexbogovich.xml.generation.model

import io.github.alexbogovich.xml.generation.model.NamespaceEnum.XBRLDT
import java.nio.file.Path

enum class XbrlSubstitutionGroup(val value: String) {
    ITEM("xbrli:item"),
    DIMENSION_ITEM("xbrldt:dimensionItem")
}

sealed class Element(open val id: String, open val name: String)

data class XsdElement(override val id: String, override val name: String, val xsdPath: Path, val xsdNamespace:
String): Element(id, name)

data class AccountXsdElement(val account: Account, val xsdElement: XsdElement)

data class ExternalXsdElement(override val id: String, override val name: String, val uri: String, val xsdNamespace: String): Element(id, name)

enum class ArcRole(val url: String) {
    DOMAIN_MEMBER("http://xbrl.org/int/dim/arcrole/domain-member"),
    DIMENSION_DOMAIN("http://xbrl.org/int/dim/arcrole/dimension-domain"),
    HYPERCUBE_DIMENSION("http://xbrl.org/int/dim/arcrole/hypercube-dimension"),
    HYPERCUBE_ALL("http://xbrl.org/int/dim/arcrole/all"),
    HYPERCUBE_NOT_ALL("http://xbrl.org/int/dim/arcrole/notAll"),
    DIMENSION_DEFAULT("http://xbrl.org/int/dim/arcrole/dimension-default"),
}

enum class ArcroleRef(val arcroleURI: String, val href: String) {
    ALL(ArcRole.HYPERCUBE_ALL.url, "${XBRLDT.location}#all"),
    NOT_ALL(ArcRole.HYPERCUBE_NOT_ALL.url, "${XBRLDT.location}#notAll"),
    DIMENSION_DEFAULT(ArcRole.DOMAIN_MEMBER.url, "${XBRLDT.location}#dimension-default"),
    DIMENSION_DOMAIN(ArcRole.DIMENSION_DOMAIN.url, "${XBRLDT.location}#dimension-domain"),
    DOMAIN_MEMBER(ArcRole.DOMAIN_MEMBER.url, "${XBRLDT.location}#domain-member"),
    HYPERCUBE_DIMENSION(ArcRole.HYPERCUBE_DIMENSION.url, "${XBRLDT.location}#hypercube-dimension")
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
    MONETARY_ITEM_TYPE("xbrli:monetaryItemType"),
}

enum class LinkBaseRefType(val arcRole: String, val role: String) {
    DEFINITION("http://www.w3.org/1999/xlink/properties/linkbase", "http://www.xbrl.org/2003/role/definitionLinkbaseRef")
}

data class XbrlRole(val id: String,
                    val role: String,
                    val path: Path,
                    val defLink: Boolean = true,
                    val labelLink: Boolean = true)