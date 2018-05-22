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
import java.io.FileOutputStream
import java.io.FileReader
import java.nio.file.Path
import java.nio.file.Paths
import javax.xml.stream.XMLOutputFactory


fun main(args: Array<String>) {
    generateBalanceDomXsd()
//    generateDefinitionForAccountXsd()
}


fun generateBalanceDomXsd() {
    val balanceStatementXsdPath: Path = Paths.get("/home/alex/Documents/balanceDom.xsd")
    println("create ${balanceStatementXsdPath.toAbsolutePath()}")
    val balanceStatementXsdWriter = DslXMLStreamWriter(balanceStatementXsdPath)
    balanceStatementXsdWriter.xsdSchema {
        namespace(listOf(XSI, XLINK, LINK, XBRLI, MODEL, NONNUM))
        defaultNamespace(XSD)
        targetNamespace(XSD)
        import(listOf(XBRLI, MODEL, NONNUM))

        val incomingBalance = xsdElement("IncomingBalance") {
            periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
        }

        val outgoingBalance = xsdElement("OutgoingBalance") {
            periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
        }

        val revenueDebit = xsdElement("RevenueDebit") {
            periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
        }

        val revenueCredit = xsdElement("RevenueCredit") {
            periodType(INSTANT); type(DOMAIN_ITEM_TYPE); substitutionGroup(ITEM); isNillable(); isAbstract()
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