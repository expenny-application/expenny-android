package org.expenny.core.data.work

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import org.expenny.core.database.ExpennyDatabase
import org.expenny.core.database.model.entity.SettlementCurrencyEntity
import org.expenny.core.domain.repository.CurrencyRateRepository
import org.expenny.core.model.currency.CurrencyRate
import org.expenny.core.model.resource.ResourceResult
import org.expenny.core.resources.R
import timber.log.Timber

@HiltWorker
class CurrencyRateUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val database: ExpennyDatabase,
    private val currencyRateRepository: CurrencyRateRepository
) : CoroutineWorker(context, workerParameters) {
    private val settlementCurrencyDao = database.currencyDao()

    // Name of Notification Channel for verbose notifications of background work
    val VERBOSE_NOTIFICATION_CHANNEL_NAME = "Verbose WorkManager Notifications"
    val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION = "Shows notifications whenever work starts"
    val NOTIFICATION_TITLE = "WorkRequest Starting"
    val CHANNEL_ID = "VERBOSE_NOTIFICATION"
    val NOTIFICATION_ID = 1

    companion object {
        val TAG = CurrencyRateUpdateWorker::class.java.simpleName
    }

    override suspend fun doWork(): Result {
        val profileCurrencyCodeToQuoteCurrenciesMap = settlementCurrencyDao.selectAll()
            .first()
            .filter { it.currency.isSubscribedToRateUpdates }
            .groupBy({ it.currencyProfile.currencyCode }, { it.currency })

        val entitiesToUpdate = profileCurrencyCodeToQuoteCurrenciesMap.flatMap { (baseCurrencyCode, quoteCurrencies) ->
            val quoteCurrenciesCodes = quoteCurrencies.map { it.code }
            val currencyRates = getLatestRates(baseCurrencyCode, quoteCurrenciesCodes)

            if (currencyRates.isNotEmpty()) {
                return@flatMap quoteCurrencies.mapNotNull {
                    currencyRates.getRateOrNull(it.code)?.let { rate ->
                        SettlementCurrencyEntity.Update(
                            currencyId = it.currencyId,
                            baseToQuoteRate = rate.rate,
                            isSubscribedToRateUpdates = it.isSubscribedToRateUpdates
                        )
                    }
                }
            }

            Timber.w("Couldn't get rates updates: $quoteCurrenciesCodes")
            return@flatMap emptyList()
        }

        return Result.success()
    }

    private fun List<CurrencyRate>.getRateOrNull(quoteCurrencyCode: String): CurrencyRate? {
        return firstOrNull { it.quoteCurrencyUnit.code == quoteCurrencyCode }
    }

    private suspend fun getLatestRates(base: String, quotes: List<String>): List<CurrencyRate> {
        return currencyRateRepository.getLatestRatesFlow(base, quotes)
            .filterIsInstance<ResourceResult.Success<List<CurrencyRate>>>()
            .first().data.orEmpty()
    }

    @SuppressLint("MissingPermission")
    private fun makeStatusNotification(message: String, context: Context) {
        val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
        val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description

        // Add the channel
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)

        // Create the notification
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))

        // Show the notification
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }
}