package io.github.alexbogovich.xml.generation.extension

import io.github.alexbogovich.xml.generation.model.*
import io.github.alexbogovich.xml.generation.shared.LocationContainer
import io.github.alexbogovich.xml.generation.utils.getRelatedHrefWithUnixSlash
import io.github.alexbogovich.xml.writer.dsl.DslXMLStreamWriter

class LocationAwareDefinitionLink(private val xmlDsl: DslXMLStreamWriter, private val locations: LocationContainer) {
    fun definitionArc(arcrole: ArcRole, from: Element, to: Element, order: String = "", targetRole:
    InternalTaxonomyRole = InternalTaxonomyRole.NONE, closed: Boolean = false): DefinitionArc {
        createLocatorsIfMiss(listOf(from, to))
        return xmlDsl.definitionArc(arcrole, locations.locationMap[from.name]!!, locations.locationMap[to.name]!!, order,
                targetRole.roleUri, closed)
    }

    private fun createLocatorsIfMiss(list: List<Element>) {
        list.forEach {
            if (!locations.locationMap.containsKey(it.name)) {
                val href = when(it) {
                    is XsdElement -> getRelatedHrefWithUnixSlash(xmlDsl.path, it.xsdPath)
                    is ExternalXsdElement -> it.uri
                }
                locations.locationMap[it.name] = xmlDsl.location("$href#${it.id}", it.name)
            }
        }
    }

    fun writeArrayOfAccounts(domain: XsdElement, accounts: List<AccountXsdElement>, group: Account.Group, type: Account
    .Type) {
        accounts.asSequence()
                .filter { it.account.group == group && it.account.type == type }
                .map { it.xsdElement }
                .forEachIndexed { index, location ->
                    definitionArc(ArcRole.DOMAIN_MEMBER, domain, location, "${index + 1}.0")
                }
    }
}