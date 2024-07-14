package org.expenny.core.data.mapper

import org.expenny.core.common.extensions.invert
import org.expenny.core.common.extensions.toLocalDateTimeSystemDefault
import org.expenny.core.common.extensions.toOffsetDateTimeSystemDefault
import org.expenny.core.common.types.IntervalType
import org.expenny.core.common.types.LocalAccountType
import org.expenny.core.common.types.RecordType
import org.expenny.core.common.types.TransactionType
import org.expenny.core.common.utils.Constants.CURRENCY_RATE_SCALE
import org.expenny.core.database.model.embedded.AccountEmbedded
import org.expenny.core.database.model.embedded.BudgetGroupEmbedded
import org.expenny.core.database.model.embedded.BudgetEmbedded
import org.expenny.core.database.model.embedded.CategoryEmbedded
import org.expenny.core.database.model.embedded.RecordEmbedded
import org.expenny.core.database.model.embedded.SettlementCurrencyEmbedded
import org.expenny.core.database.model.entity.AccountEntity
import org.expenny.core.database.model.entity.BudgetGroupEntity
import org.expenny.core.database.model.entity.BudgetEntity
import org.expenny.core.database.model.entity.CategoryEntity
import org.expenny.core.database.model.entity.FileEntity
import org.expenny.core.database.model.entity.ProfileEntity
import org.expenny.core.database.model.entity.RecordEntity
import org.expenny.core.database.model.entity.SettlementCurrencyEntity
import org.expenny.core.model.account.Account
import org.expenny.core.model.account.AccountCreate
import org.expenny.core.model.account.AccountUpdate
import org.expenny.core.model.budgetgroup.BudgetGroup
import org.expenny.core.model.budgetgroup.BudgetGroupCreate
import org.expenny.core.model.budget.Budget
import org.expenny.core.model.budget.BudgetCreate
import org.expenny.core.model.budget.BudgetUpdate
import org.expenny.core.model.category.Category
import org.expenny.core.model.category.CategoryCreate
import org.expenny.core.model.category.CategoryUpdate
import org.expenny.core.model.currency.Currency
import org.expenny.core.model.currency.CurrencyAmount
import org.expenny.core.model.currency.CurrencyCreate
import org.expenny.core.model.currency.CurrencyUnit
import org.expenny.core.model.currency.CurrencyUpdate
import org.expenny.core.model.file.FileCreate
import org.expenny.core.model.institution.Institution
import org.expenny.core.model.institution.InstitutionRequisition
import org.expenny.core.model.institution.InstitutionRequisitionStatus
import org.expenny.core.model.profile.Profile
import org.expenny.core.model.profile.ProfileCreate
import org.expenny.core.model.record.Record
import org.expenny.core.model.record.RecordCreate
import org.expenny.core.network.dto.GoCardlessInstitutionDto
import org.expenny.core.network.dto.GoCardlessRequisitionDto
import java.time.OffsetDateTime

object DataMapper {

    internal fun AccountEmbedded.toModel(): Account {
        return Account(
            id = account.accountId,
            profile = accountProfile.toModel(),
            name = account.name,
            description = account.description,
            type = LocalAccountType.valueOf(account.type),
            currency = accountCurrency.toModel(),
            startBalance = CurrencyAmount(
                currency = accountCurrency.toModel(),
                amountValue = account.startBalance
            ),
            totalBalance = CurrencyAmount(
                currency = accountCurrency.toModel(),
                amountValue = account.totalBalance
            ),
            createdAt = account.createdAt.toLocalDateTimeSystemDefault()
        )
    }

    internal fun ProfileEntity.toModel(): Profile {
        return Profile(
            id = profileId,
            name = name,
            currencyUnit = java.util.Currency.getInstance(currencyCode).toModel()
        )
    }

    internal fun BudgetEmbedded.toModel(): Budget {
        return Budget(
            id = budget.budgetId,
            profile = profile.toModel(),
            category = category.toModel(),
            limitValue = budget.limitValue,
            startDate = budget.startDate,
            endDate = budget.endDate,
        )
    }

    internal fun BudgetGroupEmbedded.toModel(): BudgetGroup {
        return if (group.intervalType != null) {
            BudgetGroup.Periodic(
                id = group.budgetGroupId,
                profile = profile.toModel(),
                currency = currency.toModel(),
                intervalType = IntervalType.valueOf(group.intervalType!!),
                budgets = budgets.map { it.toModel() }
            )
        } else {
            BudgetGroup.Onetime(
                id = group.budgetGroupId,
                profile = profile.toModel(),
                currency = currency.toModel(),
                name = group.name!!,
                dateRange = group.dateRange!!,
                budgets = budgets.map { it.toModel() }
            )
        }
    }

    internal fun SettlementCurrencyEmbedded.toModel(): Currency {
        val profile = currencyProfile.toModel()
        val currencyUnit = java.util.Currency.getInstance(currency.code).toModel()
        val baseToQuoteRate = currency.baseToQuoteRate.setScale(CURRENCY_RATE_SCALE)
        val quoteToBaseRate = baseToQuoteRate.invert()

        return Currency(
            id = currency.currencyId,
            profile = profile,
            unit = currencyUnit,
            baseToQuoteRate = baseToQuoteRate,
            quoteToBaseRate = quoteToBaseRate,
            isMain = currencyProfile.currencyCode == currency.code,
            isSubscribedToUpdates = currency.isSubscribedToRateUpdates,
            updatedAt = currency.updatedAt.toLocalDateTimeSystemDefault()
        )
    }

    internal fun java.util.Currency.toModel(): CurrencyUnit {
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
                    attachments = attachments.map { it.file.uri },
                    description = record.description,
                    date = record.date.toLocalDateTimeSystemDefault(),
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
                    attachments = attachments.map { it.file.uri },
                    description = record.description,
                    date = record.date.toLocalDateTimeSystemDefault(),
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
                    attachments = attachments.map { it.file.uri },
                    description = record.description,
                    date = record.date.toLocalDateTimeSystemDefault(),
                    amount = CurrencyAmount(
                        currency = account.accountCurrency.toModel(),
                        amountValue = record.amount
                    ),
                    transferAmount = CurrencyAmount(
                        currency = transferAccount!!.accountCurrency.toModel(),
                        amountValue = record.transferAmount!!,
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
            totalBalance = totalBalance,
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

    internal fun BudgetCreate.toEntity(): BudgetEntity {
        return BudgetEntity(
            profileId = profileId,
            categoryId = categoryId,
            limitValue = limitValue,
            startDate = startDate,
            endDate = endDate,
        )
    }

    internal fun BudgetUpdate.toEntity(): BudgetEntity.Update {
        return BudgetEntity.Update(
            budgetId = id,
            limitValue = limitValue,
            endDate = endDate,
        )
    }

    internal fun BudgetGroupCreate.toEntity(): BudgetGroupEntity {
        return BudgetGroupEntity(
            profileId = profileId,
            currencyId = currencyId,
            name = name,
            intervalType = intervalType?.name,
            dateRange = dateRange
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
            totalBalance = totalBalance,
            createdAt = OffsetDateTime.now(),
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
                    date = date.toOffsetDateTimeSystemDefault(),
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
                    date = date.toOffsetDateTimeSystemDefault(),
                    transferAccountId = transferAccountId,
                    transferAmount = transferAmount,
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
            updatedAt = OffsetDateTime.now()
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

    internal fun GoCardlessInstitutionDto.toModel(): Institution {
        return Institution(
            id = id,
            name = name,
            bic = bic,
            historyDays = transactionTotalDays.toInt(),
            logoUrl = logoUrl,
            countryCodes = countries
        )
    }

    internal fun GoCardlessRequisitionDto.toModel(): InstitutionRequisition {
        return InstitutionRequisition(
            id = id,
            status = when (status) {
                "CR" -> InstitutionRequisitionStatus.Created
                "GC" -> InstitutionRequisitionStatus.GivingConsent
                "UA" -> InstitutionRequisitionStatus.UndergoingAuthentication
                "RJ" -> InstitutionRequisitionStatus.Rejected
                "SA" -> InstitutionRequisitionStatus.SelectingAccount
                "GA" -> InstitutionRequisitionStatus.GrantingAccess
                "LN" -> InstitutionRequisitionStatus.Linked
                "EX" -> InstitutionRequisitionStatus.Expired
                else -> null
            },
            redirectBaseUrl = redirectUrl,
            url = url,
            accounts = accounts
        )
    }
}