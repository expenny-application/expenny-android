package org.expenny.feature.accounts.overview.view

import android.graphics.Typeface
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.axisLabelComponent
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.chart.layout.fullWidth
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.compose.chart.scroll.ChartScrollSpec
import com.patrykandpatrick.vico.compose.component.overlayingComponent
import com.patrykandpatrick.vico.compose.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.component.shape.shader.color
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.DefaultAlpha
import com.patrykandpatrick.vico.core.DefaultColors
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.dimensions.HorizontalDimensions
import com.patrykandpatrick.vico.core.chart.insets.Insets
import com.patrykandpatrick.vico.core.chart.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.chart.layout.HorizontalLayout
import com.patrykandpatrick.vico.core.component.marker.MarkerComponent
import com.patrykandpatrick.vico.core.component.shape.DashedShape
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.cornered.Corner
import com.patrykandpatrick.vico.core.component.shape.cornered.CorneredShape
import com.patrykandpatrick.vico.core.component.shape.cornered.MarkerCorneredShape
import com.patrykandpatrick.vico.core.component.shape.cornered.RoundedCornerTreatment
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.context.MeasureContext
import com.patrykandpatrick.vico.core.extension.copyColor
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import kotlinx.collections.immutable.ImmutableList
import org.expenny.core.common.types.AccountTrendType
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennySegmentedTabRow
import org.expenny.core.ui.extensions.label
import org.expenny.core.ui.components.ExpennyCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
internal fun AccountOverviewChartCard(
    modifier: Modifier = Modifier,
    chartValue: String?,
    chartModelProducer: CartesianChartModelProducer,
    chartScrollSpec: ChartScrollSpec,
    trendTypes: ImmutableList<AccountTrendType>,
    trendType: AccountTrendType,
    onTrendTypeChange: (AccountTrendType) -> Unit
) {
    ExpennyCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            TrendValue(value = chartValue)
            TrendChart(
                modelProducer = chartModelProducer,
                chartScrollSpec = chartScrollSpec
            )
            Spacer(modifier = Modifier.height(32.dp))
            TrendTypeFilter(
                trendTypes = trendTypes,
                currentTrendType = trendType,
                onChange = onTrendTypeChange
            )
        }
    }
}

@Composable
internal fun TrendTypeFilter(
    modifier: Modifier = Modifier,
    trendTypes: ImmutableList<AccountTrendType>,
    currentTrendType: AccountTrendType,
    onChange: (AccountTrendType) -> Unit
) {
    ExpennySegmentedTabRow(
        modifier = modifier.height(40.dp),
        tabs = trendTypes.map { it.label },
        selectedTabIndex = trendTypes.indexOf(currentTrendType),
        onTabSelect = {
            onChange(trendTypes[it])
        }
    )
}

@Composable
private fun TrendValue(
    modifier: Modifier = Modifier,
    value: String?,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(R.string.current_value_label),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value ?: stringResource(R.string.na_label),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun TrendChart(
    modifier: Modifier = Modifier,
    modelProducer: CartesianChartModelProducer,
    chartScrollSpec: ChartScrollSpec
) {
    ProvideChartStyle(rememberAccountOverviewChartStyle()) {
        val lineChartLayer = rememberLineCartesianLayer()
        CartesianChartHost(
            modifier = Modifier.fillMaxWidth(),
            modelProducer = modelProducer,
            chartScrollSpec = chartScrollSpec,
            marker = rememberMarker(),
            horizontalLayout = HorizontalLayout.fullWidth(
                scalableStartPadding = 24.dp,
                scalableEndPadding = 32.dp
            ),
            chart = rememberCartesianChart(
                lineChartLayer,
                startAxis = rememberStartAxis(
                    guideline = null,
                    itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = { 6 })
                ),
                bottomAxis = rememberBottomAxis(
                    guideline = null,
                    label = axisLabelComponent(),
                    valueFormatter = { x, _, _ ->
                        LocalDate.ofEpochDay(x.toLong()).format(DateTimeFormatter.ofPattern("d MMM"))
                    },
                    itemPlacer = remember {
                        AxisItemPlacer.Horizontal.default(spacing = 2)
                    }
                )
            )
        )
    }
}

@Composable
private fun rememberAccountOverviewChartStyle(): ChartStyle {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val lineColors = listOf(MaterialTheme.colorScheme.primary)
    val chartColors = if (isSystemInDarkTheme) DefaultColors.Dark else DefaultColors.Light

    return remember(lineColors, isSystemInDarkTheme) {
        ChartStyle(
            marker = ChartStyle.Marker(),
            elevationOverlayColor = Color(chartColors.elevationOverlayColor),
            axis = ChartStyle.Axis(
                axisLabelColor = Color(chartColors.axisLabelColor),
                axisGuidelineColor = Color(chartColors.axisGuidelineColor),
                axisLineColor = Color(chartColors.axisLineColor),
            ),
            columnLayer = ChartStyle.ColumnLayer(columns = emptyList()),
            lineLayer = ChartStyle.LineLayer(
                lines = lineColors.map { lineColor ->
                    LineCartesianLayer.LineSpec(
                        shader = DynamicShaders.color(lineColor),
                        backgroundShader = DynamicShaders.fromBrush(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    lineColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_START),
                                    lineColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_END),
                                )
                            )
                        )
                    )
                }
            )
        )
    }
}

@Composable
private fun rememberMarker(labelPosition: MarkerComponent.LabelPosition = MarkerComponent.LabelPosition.Top): Marker {
    val labelBackgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh
    val labelBackground = remember(labelBackgroundColor) {
        ShapeComponent(labelBackgroundShape, labelBackgroundColor.toArgb()).setShadow(
            radius = LABEL_BACKGROUND_SHADOW_RADIUS,
            dy = LABEL_BACKGROUND_SHADOW_DY,
            applyElevationOverlay = true
        )
    }
    val label = rememberTextComponent(
        background = labelBackground,
        lineCount = LABEL_LINE_COUNT,
        padding = labelPadding,
        typeface = Typeface.MONOSPACE
    )
    val indicatorInnerComponent = rememberShapeComponent(Shapes.pillShape, MaterialTheme.colorScheme.surface)
    val indicatorCenterComponent = rememberShapeComponent(Shapes.pillShape, Color.White)
    val indicatorOuterComponent = rememberShapeComponent(Shapes.pillShape, Color.White)
    val indicator = overlayingComponent(
        outer = indicatorOuterComponent,
        inner = overlayingComponent(
            outer = indicatorCenterComponent,
            inner = indicatorInnerComponent,
            innerPaddingAll = indicatorInnerAndCenterComponentPaddingValue
        ),
        innerPaddingAll = indicatorCenterAndOuterComponentPaddingValue
    )
    val guideline = rememberLineComponent(
        MaterialTheme.colorScheme.onSurface.copy(GUIDELINE_ALPHA),
        guidelineThickness,
        guidelineShape,
    )
    return remember(label, labelPosition, indicator, guideline) {
        object : MarkerComponent(label, labelPosition, indicator, guideline) {
            init {
                indicatorSizeDp = INDICATOR_SIZE_DP
                onApplyEntryColor = { entryColor ->
                    indicatorOuterComponent.color = entryColor.copyColor(
                        INDICATOR_OUTER_COMPONENT_ALPHA
                    )
                    with(indicatorCenterComponent) {
                        color = entryColor
                        setShadow(radius = INDICATOR_CENTER_COMPONENT_SHADOW_RADIUS, color = entryColor)
                    }
                }
            }

            override fun getInsets(
                context: MeasureContext,
                outInsets: Insets,
                horizontalDimensions: HorizontalDimensions,
            ) {
                with(context) {
                    outInsets.top =
                        (SHADOW_RADIUS_MULTIPLIER * LABEL_BACKGROUND_SHADOW_RADIUS - LABEL_BACKGROUND_SHADOW_DY).pixels
                    if (labelPosition == LabelPosition.AroundPoint) return
                    outInsets.top += label.getHeight(context) + labelBackgroundShape.tickSizeDp.pixels
                }
            }
        }
    }
}

private const val LABEL_BACKGROUND_SHADOW_RADIUS = 4f
private const val LABEL_BACKGROUND_SHADOW_DY = 2f
private const val LABEL_LINE_COUNT = 1
private const val GUIDELINE_ALPHA = .2f
private const val INDICATOR_SIZE_DP = 36f
private const val INDICATOR_OUTER_COMPONENT_ALPHA = 32
private const val INDICATOR_CENTER_COMPONENT_SHADOW_RADIUS = 12f
private const val GUIDELINE_DASH_LENGTH_DP = 8f
private const val GUIDELINE_GAP_LENGTH_DP = 4f
private const val SHADOW_RADIUS_MULTIPLIER = 1.3f

private val labelBackgroundShape = MarkerCorneredShape(
    CorneredShape(
        Corner.Relative(20, RoundedCornerTreatment),
        Corner.Relative(20, RoundedCornerTreatment),
        Corner.Relative(20, RoundedCornerTreatment),
        Corner.Relative(20, RoundedCornerTreatment),
    )
)
private val labelHorizontalPaddingValue = 8.dp
private val labelVerticalPaddingValue = 4.dp
private val labelPadding = dimensionsOf(labelHorizontalPaddingValue, labelVerticalPaddingValue)
private val indicatorInnerAndCenterComponentPaddingValue = 5.dp
private val indicatorCenterAndOuterComponentPaddingValue = 10.dp
private val guidelineThickness = 2.dp
private val guidelineShape = DashedShape(Shapes.pillShape, GUIDELINE_DASH_LENGTH_DP, GUIDELINE_GAP_LENGTH_DP)