package org.expenny.core.ui.data

sealed interface RecordUi {
    val key: Any
    val date: String

    class Header(
        override val date: String,
        val amountSum: AmountUi,
    ) : RecordUi {
        override val key: Any = date
    }

    data class Item(
        val id: Long,
        val title: String?,
        val subtitle: String,
        val description: String,
        val labels: List<String>,
        val attachmentsCount: Int,
        val amount: AmountUi,
        val transferAmount: AmountUi? = null,
        val category: CategoryUi? = null,
        override val date: String
    ) : RecordUi {
        override val key: Any = id
    }
}