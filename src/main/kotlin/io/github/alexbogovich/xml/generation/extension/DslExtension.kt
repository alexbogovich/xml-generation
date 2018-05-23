package io.github.alexbogovich.xml.generation.extension

import io.github.alexbogovich.xml.generation.model.*
import io.github.alexbogovich.xml.writer.dsl.DslXMLStreamWriter
import io.github.alexbogovich.xml.writer.dsl.EmptyElementDsl

fun DslXMLStreamWriter.xsdSchema(lambda: DslXMLStreamWriter.() -> Unit) {
    document {
        "schema" {
            this.lambda()
        }
    }
}

fun DslXMLStreamWriter.linkbase(lambda: DslXMLStreamWriter.() -> Unit) {
    document {
        "link:linkbase" {
            this.lambda()
        }
    }
}

fun DslXMLStreamWriter.arcroleRef(arcroleURI: String, href: String) {
    "link:arcroleRef" emptyElement  {
        "arcroleURI" attr arcroleURI
        "xlink:type" attr "simple"
        "xlink:href" attr href
    }
}

fun DslXMLStreamWriter.writeAllArcroleRef() {
    ArcroleRef.values().forEach { arcroleRef(it.arcroleURI, it.href) }
}

fun DslXMLStreamWriter.location(href: String, label: String): Location {
    "link:loc" emptyElement {
        "xlink:type" attr "locator"
        "xlink:href" attr href
        "xlink:label" attr label
    }
    return Location(href, label)
}

fun DslXMLStreamWriter.definitionArc(arcrole: ArcRole, from: String, to: String, order: String = "", targetRole:
String = ""): DefinitionArc {
    "link:definitionArc" emptyElement {
        "xlink:type" attr "arc"
        "xlink:arcrole" attr arcrole.url
        "xlink:from" attr from
        "xlink:to" attr to
        if (!order.isEmpty()) "order" attr order
        if (!targetRole.isEmpty()) "xbrldt:targetRole" attr targetRole
    }
    return DefinitionArc(arcrole, from, to, order, targetRole)
}

fun DslXMLStreamWriter.definitionArc(arcrole: ArcRole, from: Location, to: Location, order: String = "", targetRole:
RoleRef = RoleRef("", "")): DefinitionArc {
    return this.definitionArc(arcrole, from.label, to.label, order, targetRole.roleURI)
}

fun DslXMLStreamWriter.definitionLink(role: RoleRef, id: String, lambda: DslXMLStreamWriter.() -> Unit): DefinitionLink {
    "link:definitionLink" {
        "xlink:type" attr "extended"
        "xlink:role" attr role.roleURI
        "id" attr id
        this.lambda()
    }
    return DefinitionLink(role.roleURI, id)
}

fun DslXMLStreamWriter.roleRef(href: String, roleURI: String): RoleRef {
    "link:roleRef" emptyElement  {
        "xlink:type" attr "simple"
        "xlink:href" attr href
        "roleURI" attr roleURI
    }
    return RoleRef(href, roleURI)
}

fun DslXMLStreamWriter.writeArrayOfAccounts(domain: Location, accounts: List<Account>, group: Account.Group, type: Account.Type) {
    accounts.asSequence()
            .filter { it.group == group && it.type == type }
            .map { location("account.xsd#account-list_Account${it.number}", "Account${it.number}") }
            .forEachIndexed { index, location ->
                definitionArc(ArcRole.DOMAIN_MEMBER, domain, location, "${index + 1}.0")
            }
}

fun DslXMLStreamWriter.namespace(ns: NamespaceEnum) = this.namespace(ns.prefix, ns.link)
fun DslXMLStreamWriter.defaultNamespace(ns: NamespaceEnum) = this.defaultNamespace(ns.link)
fun DslXMLStreamWriter.targetNamespace(ns: NamespaceEnum) = this.targetNamespace(ns.link)
fun DslXMLStreamWriter.targetNamespace(ns: String)  {
    this.writeAttribute("targetNamespace", ns)
    this.schemaNamespace = ns
}

fun DslXMLStreamWriter.import(ns: NamespaceEnum){
    "import" emptyElement {
        "namespace" attr ns.link
        if (ns.location.isNotEmpty()) {
            "schemaLocation" attr ns.location
        }
    }
}

fun DslXMLStreamWriter.import(nsList: List<NamespaceEnum>) {
    nsList.forEach {
        import(it)
    }
}

fun DslXMLStreamWriter.namespace(nsList: List<NamespaceEnum>) {
    nsList.forEach {
        this.namespace(it.prefix, it.link)
    }
}

fun DslXMLStreamWriter.xsdElement(name: String, lambda: EmptyElementDsl.() -> Unit): XsdElement {
    return this.xsdElement(name, name, lambda)
}

fun DslXMLStreamWriter.xsdElement(id: String, name: String, lambda: EmptyElementDsl.() -> Unit): XsdElement {
    "element" emptyElement {
        "id" attr id
        "name" attr name
        this.lambda()
    }
    return XsdElement(id, name, path, schemaNamespace)
}

fun DslXMLStreamWriter.linkbaseRef(href: String, type: LinkBaseRefType) {
    "link:linkbaseRef" emptyElement {
        "xlink:href" attr href
        "xlink:type" attr "simple"
        "xlink:arcrole" attr type.arcRole
        "xlink:role" attr type.role
    }
}

fun DslXMLStreamWriter.defineRoleList(list:List<InternalTaxonomyRole>) {
    list.forEach {
        "link:roleType" {
            "roleURI" attr it.roleName
            "id" attr it.id
            if (it.defLink) {
                "link:usedOn"("link:definitionLink")
            }
        }
    }
}

fun DslXMLStreamWriter.appinfo(lambda: DslXMLStreamWriter.() -> Unit) {
    "annotation" {
        "appinfo" {
            this.lambda()
        }
    }
}

fun EmptyElementDsl.periodType(period: XbrlPeriodAttr) {
    "xbrli:periodType" attr period.value
}

fun EmptyElementDsl.type(type: XbrlPeriodType) {
    this.type(type.value)
}

fun EmptyElementDsl.type(type: String) {
    "type" attr type
}

fun EmptyElementDsl.substitutionGroup(type: XbrlSubstitutionGroup) {
    this.substitutionGroup(type.value)
}

fun EmptyElementDsl.substitutionGroup(type: String) {
    "substitutionGroup" attr type
}

fun EmptyElementDsl.isNillable() {
    "nillable" attr "true"
}

fun EmptyElementDsl.isAbstract() {
    "abstract" attr "true"
}