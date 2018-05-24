package io.github.alexbogovich.xml.generation.shared

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.github.alexbogovich.xml.generation.model.*
import io.github.alexbogovich.xml.generation.model.NamespaceEnum.*
import java.io.File
import java.io.FileReader
import java.net.URI

object DictContainer {
    lateinit var incomingBalance: XsdElement
    lateinit var outgoingBalance: XsdElement
    lateinit var revenueDebit: XsdElement
    lateinit var revenueCredit: XsdElement
    lateinit var balanceStatementDomain: XsdElement
    lateinit var accountGroupDomain: XsdElement
    lateinit var balanceStatementDimension: XsdElement
    lateinit var accountGroupDimension: XsdElement
    lateinit var accountXsdElements: List<AccountXsdElement>
    lateinit var accountGroupTotal: XsdElement
    lateinit var accountGroupBalance: XsdElement
    lateinit var accountGroupBalanceActive: XsdElement
    lateinit var accountGroupBalancePassive: XsdElement
    lateinit var accountGroupTrust: XsdElement
    lateinit var accountGroupTrustActive: XsdElement
    lateinit var accountGroupTrustPassive: XsdElement
    lateinit var accountGroupOffbalance: XsdElement
    lateinit var accountGroupOffbalanceActive: XsdElement
    lateinit var accountGroupOffbalancePassive: XsdElement
    lateinit var accountGroupOther: XsdElement
    lateinit var accountGroupOtherActive: XsdElement
    lateinit var accountGroupOtherPassive: XsdElement


    override fun toString(): String {
        return "DictContainer = { incomingBalance = $incomingBalance\n" +
                "outgoingBalance = $outgoingBalance\n" +
                "revenueDebit = $revenueDebit\n" +
                "revenueCredit = $revenueCredit\n" +
                "balanceStatementDomain = $balanceStatementDomain\n" +
                "accountGroupDomain = $accountGroupDomain}"
    }
}

class LocationContainer {
    val locationMap: MutableMap<String, Location> = hashMapOf()
}

object AccountGroupCollection {
    fun getAccountGroups(): List<Account> {
        val gson = GsonBuilder().setPrettyPrinting().create()
        var accountGroupList: List<Account> = listOf()

        val resource = javaClass.classLoader.getResource("Accounts.json")
        FileReader(File(resource.toURI())).use {
            accountGroupList = gson.fromJson(it, object : TypeToken<List<Account>>() {}.type)
        }

        return accountGroupList
    }
}

object MetricContainer {
    lateinit var assetTotal: XsdElement
    lateinit var assetNationalCurrency: XsdElement
    lateinit var assetForeignCurrencyOrPreciousMetals: XsdElement
}

object SharedConstants {
    const val initDate = "2018-05-23"
    const val taxonomyPath = "www.cbr-prototype.com/xbrl/fin"
}

object ExternalElemensts {
    val hyp = ExternalXsdElement("hyp", "hyp", MODEL.location, MODEL.link)
}