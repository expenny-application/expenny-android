package org.expenny.core.data.repository

import androidx.room.withTransaction
import kotlinx.coroutines.flow.*
import org.expenny.core.common.extensions.mapFlatten
import org.expenny.core.common.types.RecordType
import org.expenny.core.common.types.TransactionType
import org.expenny.core.data.mapper.DataMapper.toEntity
import org.expenny.core.data.mapper.DataMapper.toModel
import org.expenny.core.database.ExpennyDatabase
import org.expenny.core.domain.repository.*
import org.expenny.core.model.file.FileCreate
import org.expenny.core.model.record.Record
import org.expenny.core.model.record.RecordCreate
import org.expenny.core.model.record.RecordUpdate
import org.threeten.extra.LocalDateRange
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val database: ExpennyDatabase,
    private val accountRepository: AccountRepository,
    private val localRepository: LocalRepository,
    private val recordLabelRepository: RecordLabelRepository,
    private val recordFileRepository: RecordFileRepository,
    private val fileRepository: FileRepository,
) : RecordRepository {
    private val recordDao = database.recordDao()

    override fun getRecordsDesc(): Flow<List<Record>> {
        return localRepository.getCurrentProfileId()
            .filterNotNull()
            .flatMapLatest { profileId ->
                recordDao.selectAllDesc(profileId)
            }.mapFlatten { toModel() }
    }

    override fun getRecordsDesc(
        labelIds: List<Long>,
        accountIds: List<Long>,
        categoryIds: List<Long>,
        types: List<RecordType>,
        dateRange: LocalDateRange?,
        withoutCategory: Boolean,
    ): Flow<List<Record>> {
        return localRepository.getCurrentProfileId()
            .filterNotNull()
            .flatMapLatest { profileId ->
                recordDao.selectAllDesc(
                    profileId = profileId,
                    labelIds = labelIds,
                    accountIds = accountIds,
                    categoryIds = categoryIds,
                    types = types,
                    dateRange = dateRange,
                    withoutCategory = withoutCategory,
                )
            }.mapFlatten { toModel() }
    }

    override fun getRecord(id: Long): Flow<Record?> {
        return recordDao.select(id).map { it?.toModel() }
    }

    override suspend fun createRecord(data: RecordCreate) {
        return database.withTransaction {
            val recordId = recordDao.insert(data.toEntity())

            recordLabelRepository.createRecordLabels(recordId, data.labelIds)

            data.receiptsUris
                .map { fileRepository.createFile(FileCreate(data.profileId, it)) }
                .also { recordFileRepository.createRecordFiles(recordId, it) }

            when (data) {
                is RecordCreate.Transaction -> {
                    val balanceAmendmentAmount = when (data.type) {
                        TransactionType.Incoming -> data.amount
                        TransactionType.Outgoing -> data.amount.negate()
                    }
                    accountRepository.updateAccountBalance(data.accountId, balanceAmendmentAmount)
                }
                is RecordCreate.Transfer -> {
                    accountRepository.updateAccountBalance(data.accountId, data.amount.negate())
                    accountRepository.updateAccountBalance(data.transferAccountId, data.transferAmount)
                }
            }
        }
    }

    override suspend fun deleteRecords(vararg ids: Long) {
        database.withTransaction {
            ids.forEach { id ->
                val record = recordDao.select(id).first()!!.toModel()

                recordDao.delete(id)
                recordLabelRepository.deleteRecordLabels(id)
                recordFileRepository.deleteRecordFiles(id)

                when (record) {
                    is Record.Transaction -> {
                        val balanceAmendmentAmount = when (record.type) {
                            TransactionType.Incoming -> record.amount.value.negate()
                            TransactionType.Outgoing -> record.amount.value
                        }
                        accountRepository.updateAccountBalance(record.account.id, balanceAmendmentAmount)
                    }
                    is Record.Transfer -> {
                        accountRepository.updateAccountBalance(record.account.id, record.amount.value)
                        accountRepository.updateAccountBalance(record.transferAccount.id, record.transferAmount.value.negate())
                    }
                }
            }
        }
    }

    override suspend fun updateRecord(data: RecordUpdate) {
        database.withTransaction {
            val oldRecord = recordDao.select(data.id).first()!!

            val newRecord = with(data) {
                when (this) {
                    is RecordUpdate.Transaction -> {
                        RecordCreate.Transaction(
                            profileId = oldRecord.profile.profileId,
                            accountId = accountId,
                            labelIds = labelIds,
                            receiptsUris = receiptsUris,
                            description = description,
                            subject = subject,
                            amount = amount,
                            date = date,
                            categoryId = categoryId,
                            type = type
                        )
                    }
                    is RecordUpdate.Transfer -> {
                        RecordCreate.Transfer(
                            profileId = oldRecord.profile.profileId,
                            accountId = accountId,
                            labelIds = labelIds,
                            receiptsUris = receiptsUris,
                            description = description,
                            subject = subject,
                            amount = amount,
                            date = date,
                            transferAmount = transferAmount,
                            transferAccountId = transferAccountId,
                            transferFee = transferFee
                        )
                    }
                }
            }

            deleteRecords(data.id)
            createRecord(newRecord)
        }
    }
}