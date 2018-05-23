package io.github.alexbogovich.xml.generation

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.github.alexbogovich.xml.generation.extension.*
import io.github.alexbogovich.xml.generation.model.*
import io.github.alexbogovich.xml.generation.model.ArcRole.*
import io.github.alexbogovich.xml.generation.model.NamespaceEnum.*
import io.github.alexbogovich.xml.generation.model.XbrlPeriodAttr.*
import io.github.alexbogovich.xml.generation.model.XbrlPeriodType.*
import io.github.alexbogovich.xml.generation.model.XbrlSubstitutionGroup.*
import io.github.alexbogovich.xml.writer.dsl.DslXMLStreamWriter
import shared.AccountGroupCollection
import shared.DictContainer
import java.io.FileOutputStream
import java.io.FileReader
import java.nio.file.Path
import java.nio.file.Paths
import javax.xml.stream.XMLOutputFactory

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


    generateDefinitionBalanceStatmentHier()
    generateDefaultAccountGroupHierDefinition()
    generateDimensionDefinition()

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
            substitutionGroup(ITEM); type(EXPLICIT_DOMAIN_TYPE); periodType(INSTANT); isAbstract(); isNillable();
        }
        DictContainer.accountGroupDomain = xsdElement("AccountGroupDomain") {
            substitutionGroup(ITEM); type(EXPLICIT_DOMAIN_TYPE); periodType(INSTANT); isAbstract(); isNillable();
        }
    }
}

fun getLinkBaseRefPath(linkbase: LinkbaseEnum, path: Path): String {
    val linkbasePath = Paths.get(dirStringPath).resolve(linkbase.relatedPath)
    println("calculate related path from $linkbasePath to $path")
    val result = path.parent.relativize(linkbasePath)
    println("result = $result")
    return result.toString()
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

fun Path.createIfNotExist() {
    toFile().run {
        if (!exists()) {
            if (!parentFile.exists()) parentFile.mkdirs()
            println("create ${toAbsolutePath()}")
            createNewFile()
        }
    }
}

fun generateAccountGroupDomXsd() {

    val accountGroups = AccountGroupCollection.getAccountGroups()

    println("accountGroups = $accountGroups")

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

fun generateDefinitionForAccountXsd() {

    val gson = GsonBuilder().setPrettyPrinting().create()
    var personList: List<Account> = listOf()

    FileReader("C:\\staff\\Accounts.json").use {
        personList = gson.fromJson(it, object : TypeToken<List<Account>>() {}.type)
    }

//    val out = System.out
    val out = FileOutputStream("C:\\staff\\account-definition.xml")
    val writer = DslXMLStreamWriter(XMLOutputFactory.newInstance().createXMLStreamWriter(out, "UTF-8"))

    writer.linkbase {
        namespace("xsd", "http://www.w3.org/2001/XMLSchema")
        namespace("link", "http://www.xbrl.org/2003/linkbase")
        namespace("xbrldt", "http://xbrl.org/2005/xbrldt")
        namespace("xbrli", "http://www.xbrl.org/2003/instance")
        namespace("xlink", "http://www.w3.org/1999/xlink")

        writeAllArcroleRef()

        val allAccountsRole = roleRef("account.xsd#allAccounts",
                "http://www.cbr-prototype.com/xbrl/fin/list/account/allAccounts")
        val balanceAccountRole = roleRef("account.xsd#balanceAccounts",
                "http://www.cbr-prototype.com/xbrl/fin/list/account/balanceAccounts")
        val activeBalanceAccountRole = roleRef("account.xsd#activeBalanceAccounts",
                "http://www.cbr-prototype.com/xbrl/fin/list/account/activeBalanceAccounts")
        val passiveBalanceAccountRole = roleRef("account.xsd#passiveBalanceAccounts",
                "http://www.cbr-prototype.com/xbrl/fin/list/account/passiveBalanceAccounts")

        val trustAccountRole = roleRef("account.xsd#trustAccounts",
                "http://www.cbr-prototype.com/xbrl/fin/list/account/trustAccounts")
        val activeTrustAccountRole = roleRef("account.xsd#activeTrustAccounts",
                "http://www.cbr-prototype.com/xbrl/fin/list/account/activeTrustAccounts")
        val passiveTrustAccountRole = roleRef("account.xsd#passiveTrustAccounts",
                "http://www.cbr-prototype.com/xbrl/fin/list/account/passiveTrustAccounts")

        val offBalanceAccountRole = roleRef("account.xsd#offBalanceAccounts",
                "http://www.cbr-prototype.com/xbrl/fin/list/account/offBalanceAccounts")
        val activeOffBalanceAccountRole = roleRef("account.xsd#activeOffBalanceAccounts",
                "http://www.cbr-prototype.com/xbrl/fin/list/account/activeOffBalanceAccounts")
        val passiveOffBalanceAccountRole = roleRef("account.xsd#passiveOffBalanceAccounts",
                "http://www.cbr-prototype.com/xbrl/fin/list/account/passiveOffBalanceAccounts")

        val otherAccountRole = roleRef("account.xsd#otherAccounts",
                "http://www.cbr-prototype.com/xbrl/fin/list/account/otherAccounts")
        val activeOtherAccountRole = roleRef("account.xsd#activeOtherAccounts",
                "http://www.cbr-prototype.com/xbrl/fin/list/account/activeOtherAccounts")
        val passiveOtherAccountRole = roleRef("account.xsd#passiveOtherAccounts",
                "http://www.cbr-prototype.com/xbrl/fin/list/account/passiveOtherAccounts")

        definitionLink(allAccountsRole, "allAccountsLink") {
            val accountDimension = location("account.xsd#account-list_AccountDimension", "AccountDimension")
            val balance = location("account.xsd#account-list_BalanceAccountDomain", "BalanceAccountDomain")
            val trust = location("account.xsd#account-list_TrustAccountDomain", "TrustAccountDomain")
            val offBalance = location("account.xsd#account-list_OffBalanceAccountDomain",
                    "OffBalanceAccountDomain")
            val other = location("account.xsd#account-list_OtherAccountDomain", "OtherAccountDomain")

            definitionArc(DIMENSION_DOMAIN, accountDimension, balance, "1.0", balanceAccountRole)
            definitionArc(DIMENSION_DOMAIN, accountDimension, trust, "2.0", trustAccountRole)
            definitionArc(DIMENSION_DOMAIN, accountDimension, offBalance, "3.0", offBalanceAccountRole)
            definitionArc(DIMENSION_DOMAIN, accountDimension, other, "4.0", otherAccountRole)
        }

        definitionLink(balanceAccountRole, "balanceAccounts") {
            val balance = location("account.xsd#account-list_BalanceAccountDomain", "BalanceAccountDomain")
            val active = location("account.xsd#account-list_ActiveBalanceAccountDomain", "ActiveBalanceAccountDomain")
            val passive = location("account.xsd#account-list_PassiveBalanceAccountDomain", "PassiveBalanceAccountDomain")

            definitionArc(DOMAIN_MEMBER, balance, active, "1.0", activeBalanceAccountRole)
            definitionArc(DOMAIN_MEMBER, balance, passive, "2.0", passiveBalanceAccountRole)
        }

        definitionLink(trustAccountRole, "trustAccount") {
            val trust = location("account.xsd#account-list_TrustAccountDomain", "TrustAccountDomain")
            val active = location("account.xsd#account-list_ActiveTrustAccountDomain", "ActiveTrustAccountDomain")
            val passive = location("account.xsd#account-list_PassiveTrustAccountDomain", "PassiveTrustAccountDomain")

            definitionArc(DOMAIN_MEMBER, trust, active, "1.0", activeTrustAccountRole)
            definitionArc(DOMAIN_MEMBER, trust, passive, "2.0", passiveTrustAccountRole)
        }

        definitionLink(offBalanceAccountRole, "offBalanceAccounts") {
            val offBalance = location("account.xsd#account-list_OffBalanceAccountDomain", "OffBalanceAccountDomain")
            val active = location("account.xsd#account-list_ActiveOffBalanceAccountDomain", "ActiveOffBalanceAccountDomain")
            val passive = location("account.xsd#account-list_PassiveOffBalanceAccountDomain", "PassiveOffBalanceAccountDomain")

            definitionArc(DOMAIN_MEMBER, offBalance, active, "1.0", activeOffBalanceAccountRole)
            definitionArc(DOMAIN_MEMBER, offBalance, passive, "2.0", passiveOffBalanceAccountRole)
        }

        definitionLink(otherAccountRole, "otherAccounts") {
            val other = location("account.xsd#account-list_OtherAccountDomain", "OtherAccountDomain")
            val active = location("account.xsd#account-list_ActiveOtherAccountDomain", "ActiveOtherAccountDomain")
            val passive = location("account.xsd#account-list_PassiveOtherAccountDomain", "PassiveOtherAccountDomain")

            definitionArc(DOMAIN_MEMBER, other, active, "1.0", activeOtherAccountRole)
            definitionArc(DOMAIN_MEMBER, other, passive, "2.0", passiveOtherAccountRole)
        }

        definitionLink(activeBalanceAccountRole, "activeBalanceAccounts") {
            val domain = location("account.xsd#account-list_ActiveBalanceAccountDomain", "ActiveBalanceAccountDomain")
            writeArrayOfAccounts(domain, personList, Account.Group.BALANCE, Account.Type.ACTIVE)
        }

        definitionLink(passiveBalanceAccountRole, "passiveBalanceAccounts") {
            val domain = location("account.xsd#account-list_PassiveBalanceAccountDomain", "PassiveBalanceAccountDomain")
            writeArrayOfAccounts(domain, personList, Account.Group.BALANCE, Account.Type.PASSIVE)
        }

        definitionLink(activeTrustAccountRole, "activeTrustAccounts") {
            val domain = location("account.xsd#account-list_ActiveTrustAccountDomain", "ActiveTrustAccountDomain")
            writeArrayOfAccounts(domain, personList, Account.Group.TRUST, Account.Type.ACTIVE)
        }

        definitionLink(passiveTrustAccountRole, "passiveTrustAccounts") {
            val domain = location("account.xsd#account-list_PassiveTrustAccountDomain", "PassiveTrustAccountDomain")
            writeArrayOfAccounts(domain, personList, Account.Group.TRUST, Account.Type.PASSIVE)
        }

        definitionLink(activeOffBalanceAccountRole, "ActiveOffBalanceAccountDomain") {
            val domain = location("account.xsd#account-list_ActiveOffBalanceAccountDomain", "ActiveOffBalanceAccountDomain")
            writeArrayOfAccounts(domain, personList, Account.Group.OFFBALANCE, Account.Type.ACTIVE)
        }

        definitionLink(passiveOffBalanceAccountRole, "passiveOffBalanceAccounts") {
            val domain = location("account.xsd#account-list_PassiveOffBalanceAccountDomain", "PassiveOffBalanceAccountDomain")
            writeArrayOfAccounts(domain, personList, Account.Group.OFFBALANCE, Account.Type.PASSIVE)
        }

        definitionLink(activeOtherAccountRole, "activeOtherAccounts") {
            val domain = location("account.xsd#account-list_ActiveOtherAccountDomain", "ActiveOtherAccountDomain")
            writeArrayOfAccounts(domain, personList, Account.Group.OTHER, Account.Type.ACTIVE)
        }

        definitionLink(passiveOtherAccountRole, "passiveOtherAccounts") {
            val domain = location("account.xsd#account-list_PassiveOtherAccountDomain", "PassiveOtherAccountDomain")
            writeArrayOfAccounts(domain, personList, Account.Group.OTHER, Account.Type.PASSIVE)
        }
    }
}