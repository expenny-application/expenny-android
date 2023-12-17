package org.expenny.core.data.mapper

import org.expenny.core.common.extensions.invert
import org.expenny.core.common.types.AccountType
import org.expenny.core.common.types.RecordType
import org.expenny.core.common.types.TransactionType
import org.expenny.core.common.utils.Constants.CURRENCY_RATE_SCALE
import org.expenny.core.database.model.embedded.AccountEmbedded
import org.expenny.core.database.model.embedded.CategoryEmbedded
import org.expenny.core.database.model.embedded.RecordEmbedded
import org.expenny.core.database.model.embedded.SettlementCurrencyEmbedded
import org.expenny.core.database.model.entity.AccountEntity
import org.expenny.core.database.model.entity.CategoryEntity
import org.expenny.core.database.model.entity.FileEntity
import org.expenny.core.database.model.entity.ProfileEntity
import org.expenny.core.database.model.entity.RecordEntity
import org.expenny.core.database.model.entity.SettlementCurrencyEntity
import org.expenny.core.model.account.Account
import org.expenny.core.model.account.AccountCreate
import org.expenny.core.model.account.AccountUpdate
import org.expenny.core.model.category.Category
import org.expenny.core.model.category.CategoryCreate
import org.expenny.core.model.category.CategoryUpdate
import org.expenny.core.model.currency.Currency
import org.expenny.core.model.currency.CurrencyAmount
import org.expenny.core.model.currency.CurrencyCreate
import org.expenny.core.model.currency.CurrencyUnit
import org.expenny.core.model.currency.CurrencyUpdate
import org.expenny.core.model.file.FileCreate
import org.expenny.core.model.profile.Profile
import org.expenny.core.model.profile.ProfileCreate
import org.expenny.core.model.record.Record
import org.expenny.core.model.record.RecordCreate
import org.expenny.core.model.record.RecordUpdate
import java.time.LocalDateTime

object DataMapper {

    internal fun AccountEmbedded.toModel(): Account {
        return Account(
            id = account.accountId,
            profile = accountProfile.toModel(),
            name = account.name,
            description = account.description,
            type = AccountType.valueOf(account.type),
            currency = accountCurrency.toModel(),
            startBalance = CurrencyAmount(
                currency = accountCurrency.toModel(),
                amountValue = account.startBalance
            ),
            totalBalance = CurrencyAmount(
                currency = accountCurrency.toModel(),
                amountValue = account.totalBalance
            ),
        )
    }

    internal fun ProfileEntity.toModel(): Profile {
        return Profile(
            id = profileId,
            name = name,
            currencyUnit = android.icu.util.Currency.getInstance(currencyCode).toModel()
        )
    }

    internal fun SettlementCurrencyEmbedded.toModel(): Currency {
        val profile = currencyProfile.toModel()
        val currencyUnit = android.icu.util.Currency.getInstance(currency.code).toModel()
        val baseToQuoteRate = currency.baseToQuoteRate.setScale(CURRENCY_RATE_SCALE)
        val quoteToBaseRate = baseToQuoteRate.invert()

        return Currency(
            id = currency.currencyId,
            profile = profile,
            unit = currencyUnit,
            baseToQuoteRate = baseToQuoteRate,
            quoteToBaseRate = quoteToBaseRate,
            isMain = currencyProfile.currencyCode == currency.code,
            isSubscribedToRateUpdates = currency.isSubscribedToRateUpdates,
            updatedAt = currency.updatedAt
        )
    }

    internal fun android.icu.util.Currency.toModel(): CurrencyUnit {
        return CurrencyUnit(
            id = numericCode.toLong(),
            name = displayName,
            code = currencyCode,
            scale = defaultFractionDigits
        )
    }

    internal fun CategoryEmbedded.toModel(): Category {
        return Category(
            id = category.categoryId,
            profile = categoryProfile.toModel(),
            name = category.name,
            iconResName = category.iconResName,
            colorArgb = category.colorArgb
        )
    }

    internal fun RecordEmbedded.toModel(): Record {
        return when (record.type) {
            RecordType.Expense -> {
                Record.Transaction(
                    id = record.recordId,
                    profile = profile.toModel(),
                    category = category?.toModel(),
                    account = account.toModel(),
                    labels = record.labels,
                    receipts = receipts.map { it.file.uri },
                    description = record.description,
                    date = record.date,
                    type = TransactionType.Outgoing,
                    amount = CurrencyAmount(
                        currency = account.accountCurrency.toModel(),
                        amountValue = record.amount
                    )
                )
            }
            RecordType.Income -> {
                Record.Transaction(
                    id = record.recordId,
                    profile = profile.toModel(),
                    category = category?.toModel(),
                    account = account.toModel(),
                    labels = record.labels,
                    receipts = receipts.map { it.file.uri },
                    description = record.description,
                    date = record.date,
                    type = TransactionType.Incoming,
                    amount = CurrencyAmount(
                        currency = account.accountCurrency.toModel(),
                        amountValue = record.amount
                    )
                )
            }
            RecordType.Transfer -> {
                Record.Transfer(
                    id = record.recordId,
                    profile = profile.toModel(),
                    transferAccount = transferAccount!!.toModel(),
                    account = account.toModel(),
                    labels = record.labels,
                    receipts = receipts.map { it.file.uri },
                    description = record.description,
                    date = record.date,
                    amount = CurrencyAmount(
                        currency = account.accountCurrency.toModel(),
                        amountValue = record.amount
                    ),
                    transferAmount = CurrencyAmount(
                        currency = transferAccount!!.accountCurrency.toModel(),
                        amountValue = record.transferAmount!!,
                    ),
                    fee = CurrencyAmount(
                        currency = account.accountCurrency.toModel(),
                        amountValue = record.transferFee!!
                    )
                )
            }
        }
    }

    internal fun CurrencyUpdate.toEntity(): SettlementCurrencyEntity.Update {
        return SettlementCurrencyEntity.Update(
            currencyId = id,
            baseToQuoteRate = baseToQuoteRate,
            isSubscribedToRateUpdates = isSubscribedToRateUpdates
        )
    }

    internal fun AccountUpdate.toEntity(): AccountEntity.Update {
        return AccountEntity.Update(
            accountId = id,
            currencyId = currencyId,
            name = name,
            type = type.name,
            description = description,
            startBalance = startBalance,
            totalBalance = totalBalance
        )
    }

    internal fun CategoryUpdate.toEntity(): CategoryEntity.Update {
        return CategoryEntity.Update(
            categoryId = id,
            name = name,
            iconResName = iconResName,
            colorArgb = colorArgb
        )
    }

    internal fun AccountCreate.toEntity(): AccountEntity {
        return AccountEntity(
            profileId = profileId,
            currencyId = currencyId,
            name = name,
            type = type.name,
            description = description,
            startBalance = startBalance,
            totalBalance = totalBalance
        )
    }

    internal fun ProfileCreate.toEntity(): ProfileEntity {
        return ProfileEntity(
            name = name,
            currencyCode = currencyCode,
        )
    }

    internal fun RecordCreate.toEntity(): RecordEntity {
        return when (this) {
            is RecordCreate.Transaction -> {
                RecordEntity(
                    type = recordType,
                    profileId = profileId,
                    categoryId = categoryId,
                    accountId = accountId,
                    amount = amount,
                    description = description,
                    labels = labels,
                    date = date,
                )
            }
            is RecordCreate.Transfer -> {
                RecordEntity(
                    type = recordType,
                    profileId = profileId,
                    accountId = accountId,
                    amount = amount,
                    description = description,
                    labels = labels,
                    date = date,
                    transferAccountId = transferAccountId,
                    transferAmount = transferAmount,
                    transferFee = transferFee,
                )
            }
        }
    }

    internal fun CurrencyCreate.toEntity(): SettlementCurrencyEntity {
        return SettlementCurrencyEntity(
            profileId = profileId,
            code = code,
            baseToQuoteRate = baseToQuoteRate,
            isSubscribedToRateUpdates = isSubscribedToRateUpdates,
            updatedAt = LocalDateTime.now()
        )
    }

    internal fun CategoryCreate.toEntity(): CategoryEntity {
        return CategoryEntity(
            profileId = profileId,
            name = name,
            iconResName = iconResName,
            colorArgb = colorArgb
        )
    }

    internal fun FileCreate.toEntity(): FileEntity {
        return FileEntity(
            profileId = profileId,
            uri = uri
        )
    }
}