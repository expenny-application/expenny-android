package org.expenny.core.ui.data.ui

sealed interface RecordUi {
    val key: Any
    val date: String

    class Header(
        override val date: String,
        val dateAmount: AmountUi,
    ) : RecordUi {
        override val key: Any = date
    }

    sealed interface Item : RecordUi {
        val id: Long
        val description: String
        val postedAmount: AmountUi
        val labels: List<LabelUi>
        val receiptsCount: Int
        val account: String

        data class Transaction(
            override val key: Any,
            override val date: String,
            override val id: Long,
            override val description: String,
            override val postedAmount: AmountUi,
            override val labels: List<LabelUi>,
            override val receiptsCount: Int,
            override val account: String,
            val category: CategoryUi?,
        ) : Item

        data class Transfer(
            override val key: Any,
            override val date: String,
            override val id: Long,
            override val description: String,
            override val postedAmount: AmountUi,
            override val labels: List<LabelUi>,
            override val receiptsCount: Int,
            override val account: String,
            val transferAccount: String,
            val clearedAmount: AmountUi,
            val isConversionApplied: Boolean,
        ) : Item
    }
}