package org.expenny.feature.categorydetails

import androidx.compose.ui.graphics.Color
import org.expenny.core.common.models.StringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.data.field.InputField
import org.expenny.core.ui.theme.ExpennyColor

data class State(
    val toolbarTitle: StringResource = StringResource.fromRes(R.string.add_category_label),
    val showDeleteButton: Boolean = false,
    val nameInput: InputField = InputField(),
    val showDeleteDialog: Boolean = false,
    val colors: List<Color> = listOf(
        ExpennyColor.Red, ExpennyColor.DeepPurple, ExpennyColor.Yellow,
        ExpennyColor.SkyBlue, ExpennyColor.Purple, ExpennyColor.LightGreen,
        ExpennyColor.Pink, ExpennyColor.Blue, ExpennyColor.Teal,
        ExpennyColor.Orange, ExpennyColor.Lime, ExpennyColor.Green,
        ExpennyColor.Brown, ExpennyColor.Gray
    ),
    val icons: List<Int> = listOf(
        R.drawable.ic_cart, R.drawable.ic_dinner, R.drawable.ic_house, R.drawable.ic_basket,
        R.drawable.ic_bag, R.drawable.ic_cat, R.drawable.ic_dog, R.drawable.ic_pet,
        R.drawable.ic_trolley_bag, R.drawable.ic_suitcase, R.drawable.ic_care, R.drawable.ic_study,
        R.drawable.ic_safe, R.drawable.ic_savings, R.drawable.ic_bill, R.drawable.ic_bus,
        R.drawable.ic_coins, R.drawable.ic_money, R.drawable.ic_cheque, R.drawable.ic_costs,
        R.drawable.ic_pizza, R.drawable.ic_cutlery, R.drawable.ic_beer, R.drawable.ic_delivery,
        R.drawable.ic_entertainment, R.drawable.ic_fitness, R.drawable.ic_tram, R.drawable.ic_pill,
        R.drawable.ic_games, R.drawable.ic_digital_store, R.drawable.ic_health, R.drawable.ic_estate,
        R.drawable.ic_key, R.drawable.ic_airplane, R.drawable.ic_family, R.drawable.ic_smile
    ),
    val selectedColor: Color = colors[0],
    val selectedIconResId: Int = icons[0],
)

sealed interface Action {
    class OnNameChange(val name: String) : Action
    class OnColorChange(val color: Color) : Action
    class OnIconChange(val iconResId: Int) : Action
    data object OnDeleteRecordDialogDismiss : Action
    data object OnDeleteRecordDialogConfirm : Action
    data object OnSaveClick : Action
    data object OnDeleteClick : Action
    data object OnBackClick : Action
}

sealed interface Event {
    class ShowMessage(val message: StringResource) : Event
    data object RequestNameInputFocus : Event
    data object NavigateBack : Event
}