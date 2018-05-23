package io.github.alexbogovich.xml.generation.model

import shared.SharedConstants

enum class InternalTaxonomyRole(val id: String,
                                val roleUri: String,
                                val taxomomyPath: String,
                                val defLink: Boolean = true,
                                val labelLink: Boolean = false) {
    BS_DOM_IDCO("role_groupIncDebCreOut",
            "http://www.cbr-prototype.com/xbrl/fin/dict/dom/balanceStatement/groupIncDebCreOut",
            NamespaceEnum.BALANCE_STATEMENT_HIER.location),
    BS_SET("role_balanceStatementSet",
            "http://www.cbr-prototype.com/xbrl/fin/dict/dim/balanceStatementSet",
            NamespaceEnum.DIMENSIONS.location),
    AG_SET("role_accountGroupSet",
            "http://www.cbr-prototype.com/xbrl/fin/dict/dim/accountGroupSet",
            NamespaceEnum.DIMENSIONS.location),
    AG_TOTAL("role_accountGroupTotal",
            "http://www.cbr-prototype.com/xbrl/fin/dict/dom/accountGroup/total",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),

    AG_BALANCE("role_accountGroupBalance",
            "http://www.cbr-prototype.com/xbrl/fin/dict/dom/accountGroup/balance",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),
    AG_BALANCE_ACTIVE("role_accountGroupBalanceActive",
            "http://www.cbr-prototype.com/xbrl/fin/dict/dom/accountGroup/balance/active",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),
    AG_BALANCE_PASSIVE("role_accountGroupBalancePassive",
            "http://www.cbr-prototype.com/xbrl/fin/dict/dom/accountGroup/balance/passive",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),

    AG_TRUST("role_accountGroupTrust",
            "http://www.cbr-prototype.com/xbrl/fin/dict/dom/accountGroup/trust",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),
    AG_TRUST_ACTIVE("role_accountGroupTrustActive",
            "http://www.cbr-prototype.com/xbrl/fin/dict/dom/accountGroup/trust/active",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),
    AG_TRUST_PASSIVE("role_accountGroupTrustPassive",
            "http://www.cbr-prototype.com/xbrl/fin/dict/dom/accountGroup/trust/passive",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),

    AG_OFFBALANCE("role_accountGroupOffbalance",
            "http://www.cbr-prototype.com/xbrl/fin/dict/dom/accountGroup/offbalance",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),
    AG_OFFBALANCE_ACTIVE("role_accountGroupOffbalanceActive",
            "http://www.cbr-prototype.com/xbrl/fin/dict/dom/accountGroup/offbalance/active",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),
    AG_OFFBALANCE_PASSIVE("role_accountGroupOffbalancePassive",
            "http://www.cbr-prototype.com/xbrl/fin/dict/dom/accountGroup/offbalance/passive",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),

    AG_OTHER("role_accountGroupOther",
            "http://www.cbr-prototype.com/xbrl/fin/dict/dom/accountGroup/other",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),
    AG_OTHER_ACTIVE("role_accountGroupOtherActive",
            "http://www.cbr-prototype.com/xbrl/fin/dict/dom/accountGroup/other/active",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),
    AG_OTHER_PASSIVE("role_accountGroupOtherPassive",
            "http://www.cbr-prototype.com/xbrl/fin/dict/dom/accountGroup/other/passive",
            NamespaceEnum.ACCOUNT_GROUP_HIER.location),

    MET_ASSET_NF("role_assetOfNationalAndForeignCurrency",
            "http://www.cbr-prototype.com/xbrl/fin/dict/met/assetOfNationalAndForeignCurrency",
            NamespaceEnum.METRIC_HIER.location),

    MAIN_ROLE_FORM_101("role_form_101",
            "http://www.cbr-prototype.com/xbrl/fin/rep/${SharedConstants.initDate}/tab/form_101",
            NamespaceEnum.FORM_101.location),

    NONE("", "", "")
}