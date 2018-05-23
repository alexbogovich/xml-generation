package io.github.alexbogovich.xml.generation.model

import io.github.alexbogovich.xml.generation.shared.SharedConstants.initDate
import io.github.alexbogovich.xml.generation.shared.SharedConstants.taxonomyPath

enum class InternalTaxonomyRole(val id: String,
                                val roleUri: String,
                                val taxomomyPath: String,
                                val defLink: Boolean = true,
                                val labelLink: Boolean = false) {
    BS_DOM_IDCO("role_groupIncDebCreOut", "http://$taxonomyPath/dict/dom/balanceStatement/groupIncDebCreOut",
            NamespaceEnum.BALANCE_STATEMENT_HIER.location),
    BS_SET("role_balanceStatementSet", "http://$taxonomyPath/dict/dim/balanceStatementSet",
            NamespaceEnum.DIMENSIONS.location),
    AG_SET("role_accountGroupSet", "http://$taxonomyPath/dict/dim/accountGroupSet",
            NamespaceEnum.DIMENSIONS.location),
    AG_TOTAL("role_accountGroupTotal", "http://$taxonomyPath/dict/dom/accountGroup/total",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),

    AG_BALANCE("role_accountGroupBalance", "http://$taxonomyPath/dict/dom/accountGroup/balance",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),
    AG_BALANCE_ACTIVE("role_accountGroupBalanceActive",
            "http://$taxonomyPath/dict/dom/accountGroup/balance/active",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),
    AG_BALANCE_PASSIVE("role_accountGroupBalancePassive", "http://$taxonomyPath/dict/dom/accountGroup/balance/passive",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),

    AG_TRUST("role_accountGroupTrust", "http://$taxonomyPath/dict/dom/accountGroup/trust",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),
    AG_TRUST_ACTIVE("role_accountGroupTrustActive", "http://$taxonomyPath/dict/dom/accountGroup/trust/active",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),
    AG_TRUST_PASSIVE("role_accountGroupTrustPassive", "http://$taxonomyPath/dict/dom/accountGroup/trust/passive",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),

    AG_OFFBALANCE("role_accountGroupOffbalance", "http://$taxonomyPath/dict/dom/accountGroup/offbalance",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),
    AG_OFFBALANCE_ACTIVE("role_accountGroupOffbalanceActive", "http://$taxonomyPath/dict/dom/accountGroup/offbalance/active",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),
    AG_OFFBALANCE_PASSIVE("role_accountGroupOffbalancePassive", "http://$taxonomyPath/dict/dom/accountGroup/offbalance/passive",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),

    AG_OTHER("role_accountGroupOther", "http://$taxonomyPath/dict/dom/accountGroup/other",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),
    AG_OTHER_ACTIVE("role_accountGroupOtherActive", "http://$taxonomyPath/dict/dom/accountGroup/other/active",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),
    AG_OTHER_PASSIVE("role_accountGroupOtherPassive", "http://$taxonomyPath/dict/dom/accountGroup/other/passive",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),

    MET_ASSET_NF("role_assetOfNationalAndForeignCurrency", "http://$taxonomyPath/dict/met/assetOfNationalAndForeignCurrency",
            NamespaceEnum.METRIC_HIER.location),

    MAIN_ROLE_FORM_101("role_form_101", "http://$taxonomyPath/rep/$initDate/tab/form_101",
            NamespaceEnum.FORM_101.location),

    NONE("", "", "")
}