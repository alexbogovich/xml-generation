package io.github.alexbogovich.xml.generation

import io.github.alexbogovich.xml.generation.extension.*
import io.github.alexbogovich.xml.generation.model.*
import io.github.alexbogovich.xml.generation.model.ArcRole.DIMENSION_DOMAIN
import io.github.alexbogovich.xml.generation.model.ArcRole.DOMAIN_MEMBER
import io.github.alexbogovich.xml.generation.model.NamespaceEnum.*
import io.github.alexbogovich.xml.generation.model.XbrlPeriodAttr.INSTANT
import io.github.alexbogovich.xml.generation.model.XbrlPeriodType.*
import io.github.alexbogovich.xml.generation.model.XbrlSubstitutionGroup.DIMENSION_ITEM
import io.github.alexbogovich.xml.generation.model.XbrlSubstitutionGroup.ITEM
import io.github.alexbogovich.xml.writer.dsl.DslXMLStreamWriter
import shared.AccountGroupCollection
import shared.DictContainer
import shared.MetricContainer
import java.nio.file.Path
import java.nio.file.Paths

var dirStringPath = System.getProperty("user.dir")!! + "\\build\\schema"
var dirPath: Path = Paths.get(dirStringPath)

fun main(args: Array<String>) {
    if (args.isNotEmpty()) {
        dirStringPath = args[0]
        dirPath = Paths.get(dirStringPath)
    }

    generateBalanceDomXsd()
    generateBalanceHierXsd()
    generateExplicitDomainXsd()
    generateDimensionXsd()
    generateAccountGroupDomXsd()
    generateAccountGroupHierXsd()
    generateMetricXsd()
    generateMetricHierXsd()
    genenrateForm101Xsd()


    generateDefinitionBalanceStatmentHier()
    generateDefaultAccountGroupHierDefinition()
    generateDimensionDefinition()
    generateMetricDefinition()
    genenrateForm101Definistion()

//    println("dict is = $DictContainer")
//    generateDefinitionForAccountXsd()
}


fun generateBalanceDomXsd() {
    val balanceStatementXsdPath: Path = Paths.get(dirStringPath).resolve(BALANCE_STATEMENT_MEM.location)
    balanceStatementXsdPath.createIfNotExist()
    println("open ${balanceStatementXsdPath.toAbsolutePath()}")
    val balanceStatementXsdWriter = DslXMLStreamWriter(balanceStatementXsdPath)
    balanceStatementXsdWriter.xsdSchema {
        namespace(listOf(XSI, XLINK, LINK, XBRLI, MODEL, NONNUM, BALANCE_STATEMENT_MEM))
        defaultNamespace(XSD)
        targetNamespace(BALANCE_STATEMENT_MEM)
        import(listOf(XBRLI, MODEL, NONNUM))

        DictContainer.incomingBalance = xsdElement("IncomingBalance") {
            periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
        }

        DictContainer.outgoingBalance = xsdElement("OutgoingBalance") {
            periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
        }

        DictContainer.revenueDebit = xsdElement("RevenueDebit") {
            periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
        }

        DictContainer.revenueCredit = xsdElement("RevenueCredit") {
            periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
        }
    }
}

fun generateBalanceHierXsd() {
    val balanceStatementHierXsdPath: Path = Paths.get(dirStringPath).resolve(BALANCE_STATEMENT_HIER.location)
    balanceStatementHierXsdPath.createIfNotExist()
    val balanceStatementHierXsdWriter = DslXMLStreamWriter(balanceStatementHierXsdPath)

    balanceStatementHierXsdWriter.xsdSchema {
        namespace(listOf(XSI, XLINK, LINK, XBRLI, MODEL, NONNUM, BALANCE_STATEMENT_HIER))
        defaultNamespace(XSD)
        targetNamespace(BALANCE_STATEMENT_HIER)
        import(listOf(XBRLI, MODEL))

        appinfo {
            linkbaseRef(getLinkBaseRefPath(LinkbaseEnum.BALANCE_STATEMENT_HIER_DEF, path), LinkBaseRefType.DEFINITION)
            defineRoleList(listOf(InternalTaxonomyRole.BS_DOM_IDCO))
        }
    }
}

fun generateExplicitDomainXsd() {
    val explicitDomainXsdPath: Path = Paths.get(dirStringPath).resolve(EXPLICIT_DOMAINS.location)
    explicitDomainXsdPath.createIfNotExist()
    val explicitDomainXsdWriter = DslXMLStreamWriter(explicitDomainXsdPath)

    explicitDomainXsdWriter.xsdSchema {
        namespace(listOf(XSI, XLINK, LINK, XBRLI, MODEL, NONNUM, EXPLICIT_DOMAINS))
        defaultNamespace(XSD)
        targetNamespace(EXPLICIT_DOMAINS)
        import(listOf(XBRLI, MODEL))

        DictContainer.balanceStatementDomain = xsdElement("BalanceStatementDomain") {
            substitutionGroup(ITEM); type(EXPLICIT_DOMAIN_TYPE); periodType(INSTANT); isAbstract(); isNillable()
        }
        DictContainer.accountGroupDomain = xsdElement("AccountGroupDomain") {
            substitutionGroup(ITEM); type(EXPLICIT_DOMAIN_TYPE); periodType(INSTANT); isAbstract(); isNillable()
        }
    }
}

fun getLinkBaseRefPath(linkbase: LinkbaseEnum, path: Path): String {
    return Paths.get(dirStringPath)
            .resolve(linkbase.relatedPath)
            .let { getRelatedHrefWithUnixSlash(path, it) }
}

fun generateDefinitionBalanceStatmentHier() {
    val xierDefPath: Path = Paths.get(dirStringPath).resolve(LinkbaseEnum.BALANCE_STATEMENT_HIER_DEF.relatedPath)
    xierDefPath.createIfNotExist()

    println("open ${xierDefPath.toAbsolutePath()}")
    val xierDefWriter = DslXMLStreamWriter(xierDefPath)

    xierDefWriter.linkbase {
        namespace(listOf(XSI, XLINK, LINK))

        arcroleRef(ArcroleRef.DIMENSION_DOMAIN)
        roleRef(InternalTaxonomyRole.BS_DOM_IDCO, dirPath)

        definitionLink(InternalTaxonomyRole.BS_DOM_IDCO) {
            definitionArc(DOMAIN_MEMBER, DictContainer.balanceStatementDomain, DictContainer.incomingBalance, "1.0")
            definitionArc(DOMAIN_MEMBER, DictContainer.balanceStatementDomain, DictContainer.revenueDebit, "2.0")
            definitionArc(DOMAIN_MEMBER, DictContainer.balanceStatementDomain, DictContainer.revenueCredit, "3.0")
            definitionArc(DOMAIN_MEMBER, DictContainer.balanceStatementDomain, DictContainer.outgoingBalance, "4.0")
        }
    }
}

fun generateDimensionXsd() {
    val dimensionsXsdPath: Path = Paths.get(dirStringPath).resolve(DIMENSIONS.location)
    dimensionsXsdPath.createIfNotExist()
    val dimensionsXsdWriter = DslXMLStreamWriter(dimensionsXsdPath)

    dimensionsXsdWriter.xsdSchema {
        namespace(listOf(XSI, XLINK, LINK, XBRLI, XBRLDT, MODEL, NONNUM, DIMENSIONS))
        defaultNamespace(XSD)
        targetNamespace(DIMENSIONS)
        import(listOf(XBRLI, XBRLDT, MODEL))

        appinfo {
            linkbaseRef(getLinkBaseRefPath(LinkbaseEnum.DIMENSIONS_DEF, path), LinkBaseRefType.DEFINITION)
            defineRoleList(listOf(InternalTaxonomyRole.AG_SET, InternalTaxonomyRole.BS_SET))
        }

        DictContainer.balanceStatementDimension = xsdElement("BalanceStatementDimension") {
            periodType(INSTANT); type(STRING_ITEM_TYPE); substitutionGroup(DIMENSION_ITEM); isAbstract(); isNillable()
        }
        DictContainer.accountGroupDimension = xsdElement("AccountGroupDimension") {
            periodType(INSTANT); type(STRING_ITEM_TYPE); substitutionGroup(DIMENSION_ITEM); isAbstract(); isNillable()
        }
    }
}

fun generateDimensionDefinition() {
    val dimensionDefPath: Path = Paths.get(dirStringPath).resolve(LinkbaseEnum.DIMENSIONS_DEF.relatedPath)
    dimensionDefPath.createIfNotExist()

    println("open ${dimensionDefPath.toAbsolutePath()}")
    val dimensionDefWriter = DslXMLStreamWriter(dimensionDefPath)

    dimensionDefWriter.linkbase {
        namespace(listOf(XSI, XLINK, LINK, XBRLDT))

        arcroleRef(ArcroleRef.DIMENSION_DOMAIN)
        roleRef(InternalTaxonomyRole.BS_DOM_IDCO, dirPath)
        roleRef(InternalTaxonomyRole.AG_TOTAL, dirPath)
        roleRef(InternalTaxonomyRole.AG_SET, dirPath)
        roleRef(InternalTaxonomyRole.BS_SET, dirPath)

        definitionLink(InternalTaxonomyRole.BS_SET) {
            definitionArc(DIMENSION_DOMAIN, DictContainer.balanceStatementDimension, DictContainer
                    .balanceStatementDomain, "1.0", InternalTaxonomyRole.BS_DOM_IDCO)
        }

        definitionLink(InternalTaxonomyRole.AG_SET) {
            definitionArc(DIMENSION_DOMAIN, DictContainer.accountGroupDimension, DictContainer
                    .accountGroupDomain, "1.0", InternalTaxonomyRole.AG_TOTAL)
        }


    }
}

fun Path.createIfNotExist(): Path {
    toFile().run {
        if (!exists()) {
            if (!parentFile.exists()) parentFile.mkdirs()
            println("create ${toAbsolutePath()}")
            createNewFile()
        }
    }
    return this
}

fun generateAccountGroupDomXsd() {

    val accountGroups = AccountGroupCollection.getAccountGroups()

    val accountGroupXsdPath: Path = Paths.get(dirStringPath).resolve(ACCOUNT_GROUP_MEM.location)
    accountGroupXsdPath.createIfNotExist()
    println("open ${accountGroupXsdPath.toAbsolutePath()}")
    val accountGroupXsdWriter = DslXMLStreamWriter(accountGroupXsdPath)
    accountGroupXsdWriter.xsdSchema {
        namespace(listOf(XSI, XLINK, LINK, XBRLI, MODEL, NONNUM, ACCOUNT_GROUP_MEM))
        defaultNamespace(XSD)
        targetNamespace(ACCOUNT_GROUP_MEM)
        import(listOf(XBRLI, MODEL, NONNUM))

        DictContainer.accountGroupTotal = xsdElement("AccountGroupTotal") {
            periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
        }
        DictContainer.accountGroupBalance = xsdElement("AccountGroupBalance") {
            periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
        }
        DictContainer.accountGroupBalanceActive = xsdElement("AccountGroupBalanceActive") {
            periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
        }
        DictContainer.accountGroupBalancePassive = xsdElement("AccountGroupBalancePassive") {
            periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
        }

        DictContainer.accountGroupTrust = xsdElement("AccountGroupTrust") {
            periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
        }
        DictContainer.accountGroupTrustActive = xsdElement("AccountGroupTrustActive") {
            periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
        }
        DictContainer.accountGroupTrustPassive = xsdElement("AccountGroupTrustPassive") {
            periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
        }

        DictContainer.accountGroupOffbalance = xsdElement("AccountGroupOffbalance") {
            periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
        }
        DictContainer.accountGroupOffbalanceActive = xsdElement("AccountGroupOffbalanceActive") {
            periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
        }
        DictContainer.accountGroupOffbalancePassive = xsdElement("AccountGroupOffbalancePassive") {
            periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
        }

        DictContainer.accountGroupOther = xsdElement("AccountGroupOther") {
            periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
        }
        DictContainer.accountGroupOtherActive = xsdElement("AccountGroupOtherActive") {
            periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
        }
        DictContainer.accountGroupOtherPassive = xsdElement("AccountGroupOtherPassive") {
            periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
        }

        DictContainer.accountXsdElements = accountGroups.map {
            val xsdElement = xsdElement("Account${it.number}") {
                periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
            }
            AccountXsdElement(it, xsdElement)
        }.toList()
    }
}

fun generateAccountGroupHierXsd() {
    val accountGroupHierXsdPath: Path = Paths.get(dirStringPath).resolve(ACCOUNT_GROUP_HIER.location)
    accountGroupHierXsdPath.createIfNotExist()
    val accountGroupHierXsdWriter = DslXMLStreamWriter(accountGroupHierXsdPath)

    accountGroupHierXsdWriter.xsdSchema {
        namespace(listOf(XSI, XLINK, LINK, XBRLI, MODEL, NONNUM, ACCOUNT_GROUP_HIER))
        defaultNamespace(XSD)
        targetNamespace(ACCOUNT_GROUP_HIER)
        import(listOf(XBRLI, MODEL))

        appinfo {
            linkbaseRef(getLinkBaseRefPath(LinkbaseEnum.ACCOUNT_GROUP_HIER_DEF, path), LinkBaseRefType.DEFINITION)
            defineRoleList(listOf(
                    InternalTaxonomyRole.AG_TOTAL,
                    InternalTaxonomyRole.AG_BALANCE,
                    InternalTaxonomyRole.AG_BALANCE_ACTIVE,
                    InternalTaxonomyRole.AG_BALANCE_PASSIVE,
                    InternalTaxonomyRole.AG_TRUST,
                    InternalTaxonomyRole.AG_TRUST_ACTIVE,
                    InternalTaxonomyRole.AG_TRUST_PASSIVE,
                    InternalTaxonomyRole.AG_OFFBALANCE,
                    InternalTaxonomyRole.AG_OFFBALANCE_ACTIVE,
                    InternalTaxonomyRole.AG_OFFBALANCE_PASSIVE,
                    InternalTaxonomyRole.AG_OTHER,
                    InternalTaxonomyRole.AG_OTHER_ACTIVE,
                    InternalTaxonomyRole.AG_OTHER_PASSIVE
            ))
        }
    }
}

fun generateDefaultAccountGroupHierDefinition() {
    val xierDefPath: Path = Paths.get(dirStringPath).resolve(LinkbaseEnum.ACCOUNT_GROUP_HIER_DEF.relatedPath)
    xierDefPath.createIfNotExist()

    println("open ${xierDefPath.toAbsolutePath()}")
    val xierDefWriter = DslXMLStreamWriter(xierDefPath)

    xierDefWriter.linkbase {
        namespace(listOf(XSI, XLINK, LINK, XBRLDT, XBRLI))
        arcroleRef(ArcroleRef.DIMENSION_DOMAIN)
        addListOfRoleRef(
                listOf(
                        InternalTaxonomyRole.AG_TOTAL,
                        InternalTaxonomyRole.AG_BALANCE,
                        InternalTaxonomyRole.AG_BALANCE_ACTIVE,
                        InternalTaxonomyRole.AG_BALANCE_PASSIVE,
                        InternalTaxonomyRole.AG_TRUST,
                        InternalTaxonomyRole.AG_TRUST_ACTIVE,
                        InternalTaxonomyRole.AG_TRUST_PASSIVE,
                        InternalTaxonomyRole.AG_OFFBALANCE,
                        InternalTaxonomyRole.AG_OFFBALANCE_ACTIVE,
                        InternalTaxonomyRole.AG_OFFBALANCE_PASSIVE,
                        InternalTaxonomyRole.AG_OTHER,
                        InternalTaxonomyRole.AG_OTHER_ACTIVE,
                        InternalTaxonomyRole.AG_OTHER_PASSIVE
                ), dirPath
        )

        definitionLink(InternalTaxonomyRole.AG_TOTAL) {
            definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupDomain,
                    DictContainer.accountGroupBalance, "1.0", InternalTaxonomyRole.AG_BALANCE)
            definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupDomain,
                    DictContainer.accountGroupTrust, "2.0", InternalTaxonomyRole.AG_TRUST)
            definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupDomain,
                    DictContainer.accountGroupOffbalance, "3.0", InternalTaxonomyRole.AG_OFFBALANCE)
            definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupDomain,
                    DictContainer.accountGroupOther, "4.0", InternalTaxonomyRole.AG_OTHER)
        }

        definitionLink(InternalTaxonomyRole.AG_BALANCE) {
            definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupBalance,
                    DictContainer.accountGroupBalanceActive, "1.0", InternalTaxonomyRole.AG_BALANCE_ACTIVE)
            definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupBalance,
                    DictContainer.accountGroupBalancePassive, "2.0", InternalTaxonomyRole.AG_BALANCE_PASSIVE)
        }

        definitionLink(InternalTaxonomyRole.AG_TRUST) {
            definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupTrust,
                    DictContainer.accountGroupTrustActive, "1.0", InternalTaxonomyRole.AG_TRUST_ACTIVE)
            definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupTrust,
                    DictContainer.accountGroupTrustPassive, "2.0", InternalTaxonomyRole.AG_TRUST_PASSIVE)
        }

        definitionLink(InternalTaxonomyRole.AG_OFFBALANCE) {
            definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupOffbalance,
                    DictContainer.accountGroupOffbalanceActive, "1.0", InternalTaxonomyRole.AG_OFFBALANCE_ACTIVE)
            definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupOffbalance,
                    DictContainer.accountGroupOffbalancePassive, "2.0", InternalTaxonomyRole.AG_OFFBALANCE_PASSIVE)
        }

        definitionLink(InternalTaxonomyRole.AG_OTHER) {
            definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupOther,
                    DictContainer.accountGroupOtherActive, "1.0", InternalTaxonomyRole.AG_OTHER_ACTIVE)
            definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupOther,
                    DictContainer.accountGroupOtherPassive, "2.0", InternalTaxonomyRole.AG_OTHER_PASSIVE)
        }

        definitionLink(InternalTaxonomyRole.AG_BALANCE_ACTIVE) {
            writeArrayOfAccounts(DictContainer.accountGroupBalanceActive, DictContainer.accountXsdElements,
                    Account.Group.BALANCE, Account.Type.ACTIVE)
        }
        definitionLink(InternalTaxonomyRole.AG_BALANCE_PASSIVE) {
            writeArrayOfAccounts(DictContainer.accountGroupBalancePassive, DictContainer.accountXsdElements,
                    Account.Group.BALANCE, Account.Type.PASSIVE)
        }
        definitionLink(InternalTaxonomyRole.AG_TRUST_ACTIVE) {
            writeArrayOfAccounts(DictContainer.accountGroupTrustActive, DictContainer.accountXsdElements,
                    Account.Group.TRUST, Account.Type.ACTIVE)
        }
        definitionLink(InternalTaxonomyRole.AG_TRUST_PASSIVE) {
            writeArrayOfAccounts(DictContainer.accountGroupTrustPassive, DictContainer.accountXsdElements,
                    Account.Group.TRUST, Account.Type.PASSIVE)
        }
        definitionLink(InternalTaxonomyRole.AG_OFFBALANCE_ACTIVE) {
            writeArrayOfAccounts(DictContainer.accountGroupOffbalanceActive, DictContainer.accountXsdElements,
                    Account.Group.OFFBALANCE, Account.Type.ACTIVE)
        }
        definitionLink(InternalTaxonomyRole.AG_OFFBALANCE_PASSIVE) {
            writeArrayOfAccounts(DictContainer.accountGroupOffbalancePassive, DictContainer.accountXsdElements,
                    Account.Group.OFFBALANCE, Account.Type.PASSIVE)
        }
        definitionLink(InternalTaxonomyRole.AG_OTHER_ACTIVE) {
            writeArrayOfAccounts(DictContainer.accountGroupOtherActive, DictContainer.accountXsdElements,
                    Account.Group.OTHER, Account.Type.ACTIVE)
        }
        definitionLink(InternalTaxonomyRole.AG_OTHER_PASSIVE) {
            writeArrayOfAccounts(DictContainer.accountGroupOtherPassive, DictContainer.accountXsdElements,
                    Account.Group.OTHER, Account.Type.PASSIVE)
        }
    }
}

fun generateMetricXsd() {
    val metricXsdPath: Path = Paths.get(dirStringPath).resolve(METRIC_MEM.location)
    metricXsdPath.createIfNotExist()
    println("open ${metricXsdPath.toAbsolutePath()}")
    val metricXsdWriter = DslXMLStreamWriter(metricXsdPath)
    metricXsdWriter.xsdSchema {
        namespace(listOf(XSI, XLINK, LINK, XBRLI, MODEL, NONNUM, METRIC_MEM))
        defaultNamespace(XSD)
        targetNamespace(METRIC_MEM)
        import(listOf(XBRLI, MODEL, NONNUM))

        MetricContainer.assetTotal = xsdElement("AssetTotal") {
            periodType(INSTANT); type(MONETARY_ITEM_TYPE); substitutionGroup(ITEM); isNillable()
        }

        MetricContainer.assetNationalCurrency = xsdElement("AssetNationalCurrency") {
            periodType(INSTANT); type(MONETARY_ITEM_TYPE); substitutionGroup(ITEM); isNillable()
        }

        MetricContainer.assetForeignCurrencyOrPreciousMetals = xsdElement("AssetForeignCurrencyOrPreciousMetals") {
            periodType(INSTANT); type(MONETARY_ITEM_TYPE); substitutionGroup(ITEM); isNillable()
        }

    }
}

fun generateMetricHierXsd() {
    val metricXsdPath: Path = Paths.get(dirStringPath).resolve(METRIC_HIER.location)
    metricXsdPath.createIfNotExist()
    println("open ${metricXsdPath.toAbsolutePath()}")
    val metricXsdWriter = DslXMLStreamWriter(metricXsdPath)
    metricXsdWriter.xsdSchema {
        namespace(listOf(XSI, XLINK, LINK, XBRLI, MODEL, METRIC_HIER))
        defaultNamespace(XSD)
        targetNamespace(METRIC_HIER)
        import(listOf(XBRLI, MODEL))

        appinfo {
            linkbaseRef(getLinkBaseRefPath(LinkbaseEnum.METRIC_HIER_DEF, path), LinkBaseRefType.DEFINITION)
            defineRoleList(listOf(InternalTaxonomyRole.MET_ASSET_NF))
        }

    }
}

fun generateMetricDefinition() {
    val metricHierDefPath: Path = Paths.get(dirStringPath).resolve(LinkbaseEnum.METRIC_HIER_DEF.relatedPath)
    metricHierDefPath.createIfNotExist()

    println("open ${metricHierDefPath.toAbsolutePath()}")
    val metricHierDefWriter = DslXMLStreamWriter(metricHierDefPath)

    metricHierDefWriter.linkbase {
        namespace(listOf(XSI, XLINK, LINK, XBRLDT))

        arcroleRef(ArcroleRef.DIMENSION_DOMAIN)
        roleRef(InternalTaxonomyRole.MET_ASSET_NF, dirPath)

        definitionLink(InternalTaxonomyRole.MET_ASSET_NF) {
            definitionArc(DIMENSION_DOMAIN, MetricContainer.assetTotal, MetricContainer.assetNationalCurrency, "1.0")
            definitionArc(DIMENSION_DOMAIN, MetricContainer.assetTotal, MetricContainer.assetForeignCurrencyOrPreciousMetals, "2.0")
        }
    }
}

fun genenrateForm101Xsd() {
    val form101XsdPath: Path = Paths.get(dirStringPath).resolve(FORM_101.location)
    form101XsdPath.createIfNotExist()
    println("open ${form101XsdPath.toAbsolutePath()}")
    val form101XsdWriter = DslXMLStreamWriter(form101XsdPath)
    form101XsdWriter.xsdSchema {
        namespace(listOf(XSI, XLINK, LINK, XBRLI, MODEL, FORM_101))
        defaultNamespace(XSD)
        targetNamespace(FORM_101)
        import(listOf(XBRLI, MODEL))

        appinfo {
            linkbaseRef(getLinkBaseRefPath(LinkbaseEnum.FORM_101_DEF, path), LinkBaseRefType.DEFINITION)
            defineRoleList(listOf(InternalTaxonomyRole.MAIN_ROLE_FORM_101))
        }
    }
}

fun genenrateForm101Definistion() {
    Paths.get(dirStringPath)
            .resolve(LinkbaseEnum.FORM_101_DEF.relatedPath)
            .createIfNotExist()
            .let { path: Path -> DslXMLStreamWriter(path) }
            .linkbase {
                namespace(listOf(XSI, XLINK, LINK, XBRLDT))

                arcroleRef(ArcroleRef.DIMENSION_DOMAIN)
                arcroleRef(ArcroleRef.ALL)
                arcroleRef(ArcroleRef.HYPERCUBE_DIMENSION)

                roleRef(InternalTaxonomyRole.MAIN_ROLE_FORM_101, dirPath)
                roleRef(InternalTaxonomyRole.AG_SET, dirPath)
                roleRef(InternalTaxonomyRole.BS_SET, dirPath)
                roleRef(InternalTaxonomyRole.MET_ASSET_NF, dirPath)

                definitionLink(InternalTaxonomyRole.MAIN_ROLE_FORM_101) {
                    definitionArc(DIMENSION_DOMAIN, MetricContainer.assetTotal,
                            MetricContainer.assetNationalCurrency, "1.0")
                    definitionArc(DIMENSION_DOMAIN, MetricContainer.assetTotal,
                            MetricContainer.assetForeignCurrencyOrPreciousMetals, "2.0")
                }
            }
}