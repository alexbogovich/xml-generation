package io.github.alexbogovich.xml.generation

import io.github.alexbogovich.xml.generation.extension.*
import io.github.alexbogovich.xml.generation.model.*
import io.github.alexbogovich.xml.generation.model.ArcRole.*
import io.github.alexbogovich.xml.generation.model.InternalTaxonomyRole.*
import io.github.alexbogovich.xml.generation.model.NamespaceEnum.*
import io.github.alexbogovich.xml.generation.model.XbrlPeriodAttr.INSTANT
import io.github.alexbogovich.xml.generation.model.XbrlPeriodType.*
import io.github.alexbogovich.xml.generation.model.XbrlSubstitutionGroup.DIMENSION_ITEM
import io.github.alexbogovich.xml.generation.model.XbrlSubstitutionGroup.ITEM
import io.github.alexbogovich.xml.generation.shared.AccountGroupCollection
import io.github.alexbogovich.xml.generation.shared.DictContainer
import io.github.alexbogovich.xml.generation.shared.ExternalElemensts
import io.github.alexbogovich.xml.generation.shared.MetricContainer
import io.github.alexbogovich.xml.generation.utils.getDslSteamWriter
import java.nio.file.Path
import java.nio.file.Paths

var dirStringPath = System.getProperty("user.dir")!! + "/build/schema"
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
    generateForm101Xsd()

    generateBalanceHierDefinition()
    generateAccountGroupHierDefinition()
    generateDimensionDefinition()
    generateMetricDefinition()
    generateForm101Definition()
}

fun generateBalanceDomXsd() {
    BALANCE_STATEMENT_MEM.location
            .getDslSteamWriter()
            .xsdSchema {
                namespace(listOf(XSI, XLINK, LINK, XBRLI, MODEL, NONNUM, BALANCE_STATEMENT_MEM))
                defaultNamespace(XSD)
                targetNamespace(BALANCE_STATEMENT_MEM)
                import(listOf(XBRLI, MODEL, NONNUM))

                DictContainer.incomingBalance = defaultDomainItem("IncomingBalance")
                DictContainer.outgoingBalance = defaultDomainItem("OutgoingBalance")
                DictContainer.revenueDebit = defaultDomainItem("RevenueDebit")
                DictContainer.revenueCredit = defaultDomainItem("RevenueCredit")
            }
}

fun generateBalanceHierXsd() {
    BALANCE_STATEMENT_HIER.location
            .getDslSteamWriter()
            .xsdSchema {
                namespace(listOf(XSI, XLINK, LINK, XBRLI, MODEL, NONNUM, BALANCE_STATEMENT_HIER))
                defaultNamespace(XSD)
                targetNamespace(BALANCE_STATEMENT_HIER)
                import(listOf(XBRLI, MODEL))

                appinfo {
                    linkbaseRef(LinkbaseEnum.BALANCE_STATEMENT_HIER_DEF, LinkBaseRefType.DEFINITION)
                    defineRoleList(listOf(BS_DOM_IDCO))
                }
            }
}

fun generateExplicitDomainXsd() {
    EXPLICIT_DOMAINS.location
            .getDslSteamWriter()
            .xsdSchema {
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

fun generateBalanceHierDefinition() {
    LinkbaseEnum.BALANCE_STATEMENT_HIER_DEF.relatedPath
            .getDslSteamWriter()
            .linkbase {
                namespace(listOf(XSI, XLINK, LINK))

                arcroleRef(ArcroleRef.DIMENSION_DOMAIN)
                roleRef(BS_DOM_IDCO, dirPath)

                definitionLink(BS_DOM_IDCO) {
                    definitionArc(DOMAIN_MEMBER, DictContainer.balanceStatementDomain, DictContainer.incomingBalance, "1.0")
                    definitionArc(DOMAIN_MEMBER, DictContainer.balanceStatementDomain, DictContainer.revenueDebit, "2.0")
                    definitionArc(DOMAIN_MEMBER, DictContainer.balanceStatementDomain, DictContainer.revenueCredit, "3.0")
                    definitionArc(DOMAIN_MEMBER, DictContainer.balanceStatementDomain, DictContainer.outgoingBalance, "4.0")
                }
            }
}

fun generateDimensionXsd() {
    DIMENSIONS.location
            .getDslSteamWriter()
            .xsdSchema {
                namespace(listOf(XSI, XLINK, LINK, XBRLI, XBRLDT, MODEL, NONNUM, DIMENSIONS))
                defaultNamespace(XSD)
                targetNamespace(DIMENSIONS)
                import(listOf(XBRLI, XBRLDT, MODEL))

                appinfo {
                    linkbaseRef(LinkbaseEnum.DIMENSIONS_DEF, LinkBaseRefType.DEFINITION)
                    defineRoleList(listOf(AG_SET, BS_SET))
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
    LinkbaseEnum.DIMENSIONS_DEF.relatedPath
            .getDslSteamWriter()
            .linkbase {
                namespace(listOf(XSI, XLINK, LINK, XBRLDT))

                arcroleRef(ArcroleRef.DIMENSION_DOMAIN)
                roleRef(BS_DOM_IDCO, dirPath)
                roleRef(AG_TOTAL, dirPath)
                roleRef(AG_SET, dirPath)
                roleRef(BS_SET, dirPath)

                definitionLink(BS_SET) {
                    definitionArc(DIMENSION_DOMAIN, DictContainer.balanceStatementDimension, DictContainer
                            .balanceStatementDomain, "1.0", BS_DOM_IDCO)
                }

                definitionLink(AG_SET) {
                    definitionArc(DIMENSION_DOMAIN, DictContainer.accountGroupDimension, DictContainer
                            .accountGroupDomain, "1.0", AG_TOTAL)
                }


            }
}

fun generateAccountGroupDomXsd() {
    val accountGroups = AccountGroupCollection.getAccountGroups()
    ACCOUNT_GROUP_MEM.location
            .getDslSteamWriter()
            .xsdSchema {
                namespace(listOf(XSI, XLINK, LINK, XBRLI, MODEL, NONNUM, ACCOUNT_GROUP_MEM))
                defaultNamespace(XSD)
                targetNamespace(ACCOUNT_GROUP_MEM)
                import(listOf(XBRLI, MODEL, NONNUM))

                DictContainer.accountGroupTotal = defaultDomainItem("AccountGroupTotal")
                DictContainer.accountGroupBalance = defaultDomainItem("AccountGroupBalance")
                DictContainer.accountGroupBalanceActive = defaultDomainItem("AccountGroupBalanceActive")
                DictContainer.accountGroupBalancePassive = defaultDomainItem("AccountGroupBalancePassive")
                DictContainer.accountGroupTrust = defaultDomainItem("AccountGroupTrust")
                DictContainer.accountGroupTrustActive = defaultDomainItem("AccountGroupTrustActive")
                DictContainer.accountGroupTrustPassive = defaultDomainItem("AccountGroupTrustPassive")
                DictContainer.accountGroupOffbalance = defaultDomainItem("AccountGroupOffbalance")
                DictContainer.accountGroupOffbalanceActive = defaultDomainItem("AccountGroupOffbalanceActive")
                DictContainer.accountGroupOffbalancePassive = defaultDomainItem("AccountGroupOffbalancePassive")
                DictContainer.accountGroupOther = defaultDomainItem("AccountGroupOther")
                DictContainer.accountGroupOtherActive = defaultDomainItem("AccountGroupOtherActive")
                DictContainer.accountGroupOtherPassive = defaultDomainItem("AccountGroupOtherPassive")

                DictContainer.accountXsdElements = accountGroups.map {
                    val xsdElement = xsdElement("Account${it.number}") {
                        periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
                    }
                    AccountXsdElement(it, xsdElement)
                }.toList()
            }
}

val accountGroupNestedRoles = listOf(
        AG_TOTAL,
        AG_BALANCE,
        AG_BALANCE_ACTIVE,
        AG_BALANCE_PASSIVE,
        AG_TRUST,
        AG_TRUST_ACTIVE,
        AG_TRUST_PASSIVE,
        AG_OFFBALANCE,
        AG_OFFBALANCE_ACTIVE,
        AG_OFFBALANCE_PASSIVE,
        AG_OTHER,
        AG_OTHER_ACTIVE,
        AG_OTHER_PASSIVE
)

fun generateAccountGroupHierXsd() {
    ACCOUNT_GROUP_HIER.location
            .getDslSteamWriter()
            .xsdSchema {
                namespace(listOf(XSI, XLINK, LINK, XBRLI, MODEL, NONNUM, ACCOUNT_GROUP_HIER))
                defaultNamespace(XSD)
                targetNamespace(ACCOUNT_GROUP_HIER)
                import(listOf(XBRLI, MODEL))

                appinfo {
                    linkbaseRef(LinkbaseEnum.ACCOUNT_GROUP_HIER_DEF, LinkBaseRefType.DEFINITION)
                    defineRoleList(accountGroupNestedRoles)
                }
            }
}

fun generateAccountGroupHierDefinition() {
    LinkbaseEnum.ACCOUNT_GROUP_HIER_DEF.relatedPath
            .getDslSteamWriter()
            .linkbase {
                namespace(listOf(XSI, XLINK, LINK, XBRLDT, XBRLI))
                arcroleRef(ArcroleRef.DIMENSION_DOMAIN)
                addListOfRoleRef(accountGroupNestedRoles, dirPath)

                definitionLink(AG_TOTAL) {
                    definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupDomain,
                            DictContainer.accountGroupBalance, "1.0", AG_BALANCE)
                    definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupDomain,
                            DictContainer.accountGroupTrust, "2.0", AG_TRUST)
                    definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupDomain,
                            DictContainer.accountGroupOffbalance, "3.0", AG_OFFBALANCE)
                    definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupDomain,
                            DictContainer.accountGroupOther, "4.0", AG_OTHER)
                }

                definitionLink(AG_BALANCE) {
                    definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupBalance,
                            DictContainer.accountGroupBalanceActive, "1.0", AG_BALANCE_ACTIVE)
                    definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupBalance,
                            DictContainer.accountGroupBalancePassive, "2.0", AG_BALANCE_PASSIVE)
                }

                definitionLink(AG_TRUST) {
                    definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupTrust,
                            DictContainer.accountGroupTrustActive, "1.0", AG_TRUST_ACTIVE)
                    definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupTrust,
                            DictContainer.accountGroupTrustPassive, "2.0", AG_TRUST_PASSIVE)
                }

                definitionLink(AG_OFFBALANCE) {
                    definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupOffbalance,
                            DictContainer.accountGroupOffbalanceActive, "1.0", AG_OFFBALANCE_ACTIVE)
                    definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupOffbalance,
                            DictContainer.accountGroupOffbalancePassive, "2.0", AG_OFFBALANCE_PASSIVE)
                }

                definitionLink(AG_OTHER) {
                    definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupOther,
                            DictContainer.accountGroupOtherActive, "1.0", AG_OTHER_ACTIVE)
                    definitionArc(DOMAIN_MEMBER, DictContainer.accountGroupOther,
                            DictContainer.accountGroupOtherPassive, "2.0", AG_OTHER_PASSIVE)
                }

                definitionLink(AG_BALANCE_ACTIVE) {
                    writeArrayOfAccounts(DictContainer.accountGroupBalanceActive, DictContainer.accountXsdElements,
                            Account.Group.BALANCE, Account.Type.ACTIVE)
                }
                definitionLink(AG_BALANCE_PASSIVE) {
                    writeArrayOfAccounts(DictContainer.accountGroupBalancePassive, DictContainer.accountXsdElements,
                            Account.Group.BALANCE, Account.Type.PASSIVE)
                }
                definitionLink(AG_TRUST_ACTIVE) {
                    writeArrayOfAccounts(DictContainer.accountGroupTrustActive, DictContainer.accountXsdElements,
                            Account.Group.TRUST, Account.Type.ACTIVE)
                }
                definitionLink(AG_TRUST_PASSIVE) {
                    writeArrayOfAccounts(DictContainer.accountGroupTrustPassive, DictContainer.accountXsdElements,
                            Account.Group.TRUST, Account.Type.PASSIVE)
                }
                definitionLink(AG_OFFBALANCE_ACTIVE) {
                    writeArrayOfAccounts(DictContainer.accountGroupOffbalanceActive, DictContainer.accountXsdElements,
                            Account.Group.OFFBALANCE, Account.Type.ACTIVE)
                }
                definitionLink(AG_OFFBALANCE_PASSIVE) {
                    writeArrayOfAccounts(DictContainer.accountGroupOffbalancePassive, DictContainer.accountXsdElements,
                            Account.Group.OFFBALANCE, Account.Type.PASSIVE)
                }
                definitionLink(AG_OTHER_ACTIVE) {
                    writeArrayOfAccounts(DictContainer.accountGroupOtherActive, DictContainer.accountXsdElements,
                            Account.Group.OTHER, Account.Type.ACTIVE)
                }
                definitionLink(AG_OTHER_PASSIVE) {
                    writeArrayOfAccounts(DictContainer.accountGroupOtherPassive, DictContainer.accountXsdElements,
                            Account.Group.OTHER, Account.Type.PASSIVE)
                }
            }
}

fun generateMetricXsd() {
    METRIC_MEM.location
            .getDslSteamWriter()
            .xsdSchema {
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

                MetricContainer
                        .assetForeignCurrencyOrPreciousMetals = xsdElement("AssetForeignCurrencyOrPreciousMetals") {
                    periodType(INSTANT); type(MONETARY_ITEM_TYPE); substitutionGroup(ITEM); isNillable()
                }

            }
}

fun generateMetricHierXsd() {
    METRIC_HIER.location
            .getDslSteamWriter()
            .xsdSchema {
                namespace(listOf(XSI, XLINK, LINK, XBRLI, MODEL, METRIC_HIER))
                defaultNamespace(XSD)
                targetNamespace(METRIC_HIER)
                import(listOf(XBRLI, MODEL))

                appinfo {
                    linkbaseRef(LinkbaseEnum.METRIC_HIER_DEF, LinkBaseRefType.DEFINITION)
                    defineRoleList(listOf(MET_ASSET_NF))
                }

            }
}

fun generateMetricDefinition() {
    LinkbaseEnum.METRIC_HIER_DEF.relatedPath
            .getDslSteamWriter()
            .linkbase {
                namespace(listOf(XSI, XLINK, LINK, XBRLDT))

                arcroleRef(ArcroleRef.DIMENSION_DOMAIN)
                roleRef(MET_ASSET_NF, dirPath)

                definitionLink(MET_ASSET_NF) {
                    definitionArc(DIMENSION_DOMAIN, MetricContainer.assetTotal, MetricContainer.assetNationalCurrency, "1.0")
                    definitionArc(DIMENSION_DOMAIN, MetricContainer.assetTotal, MetricContainer.assetForeignCurrencyOrPreciousMetals, "2.0")
                }
            }
}

fun generateForm101Xsd() {
    FORM_101.location
            .getDslSteamWriter()
            .xsdSchema {
                namespace(listOf(XSI, XLINK, LINK, XBRLI, MODEL, FORM_101))
                defaultNamespace(XSD)
                targetNamespace(FORM_101)
                import(listOf(XBRLI, MODEL))

                appinfo {
                    linkbaseRef(LinkbaseEnum.FORM_101_DEF, LinkBaseRefType.DEFINITION)
                    defineRoleList(listOf(MAIN_ROLE_FORM_101))
                }
            }
}

fun generateForm101Definition() {
    LinkbaseEnum.FORM_101_DEF.relatedPath
            .getDslSteamWriter()
            .linkbase {
                namespace(listOf(XSI, XLINK, LINK, XBRLDT))

                arcroleRef(ArcroleRef.DIMENSION_DOMAIN)
                arcroleRef(ArcroleRef.ALL)
                arcroleRef(ArcroleRef.HYPERCUBE_DIMENSION)

                roleRef(MAIN_ROLE_FORM_101, dirPath)
                roleRef(AG_SET, dirPath)
                roleRef(BS_SET, dirPath)
                roleRef(MET_ASSET_NF, dirPath)

                definitionLink(MAIN_ROLE_FORM_101) {
                    definitionArc(HYPERCUBE_DIMENSION, ExternalElemensts.hyp,
                            DictContainer.accountGroupDimension, "1.0", AG_SET)
                    definitionArc(HYPERCUBE_DIMENSION, ExternalElemensts.hyp,
                            DictContainer.balanceStatementDimension, "2.0", BS_SET)
                    definitionArc(HYPERCUBE_ALL, MetricContainer.assetTotal,
                            ExternalElemensts.hyp, "1.0", MET_ASSET_NF)
                }
            }
}