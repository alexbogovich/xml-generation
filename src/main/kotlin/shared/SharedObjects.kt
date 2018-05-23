package shared

import io.github.alexbogovich.xml.generation.model.Location
import io.github.alexbogovich.xml.generation.model.XsdElement

object DictContainer {
    lateinit var incomingBalance: XsdElement
    lateinit var outgoingBalance: XsdElement
    lateinit var revenueDebit: XsdElement
    lateinit var revenueCredit: XsdElement
    lateinit var balanceStatementDomain: XsdElement
    lateinit var accountGroupDomain: XsdElement
    lateinit var balanceStatementDimension: XsdElement
    lateinit var accountGroupDimension: XsdElement

    override fun toString(): String {
        return "DictContainer = { incomingBalance = $incomingBalance\n" +
                "outgoingBalance = $outgoingBalance\n" +
                "revenueDebit = $revenueDebit\n" +
                "revenueCredit = $revenueCredit\n" +
                "balanceStatementDomain = $balanceStatementDomain\n" +
                "accountGroupDomain = $accountGroupDomain}"
    }
}

object LocationContainer {
    val list: MutableMap<String, Location> = hashMapOf()
}