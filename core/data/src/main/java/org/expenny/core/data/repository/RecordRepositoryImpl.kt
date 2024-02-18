package org.expenny.core.data.repository

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.expenny.core.common.extensions.mapFlatten
import org.expenny.core.common.types.TransactionType
import org.expenny.core.data.mapper.DataMapper.toEntity
import org.expenny.core.data.mapper.DataMapper.toModel
import org.expenny.core.database.ExpennyDatabase
import org.expenny.core.domain.repository.AccountRepository
import org.expenny.core.domain.repository.FileRepository
import org.expenny.core.domain.repository.LocalRepository
import org.expenny.core.domain.repository.RecordFileRepository
import org.expenny.core.domain.repository.RecordRepository
import org.expenny.core.model.file.FileCreate
import org.expenny.core.model.record.Record
import org.expenny.core.model.record.RecordCreate
import org.expenny.core.model.record.RecordUpdate
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val database: ExpennyDatabase,
    private val accountRepository: AccountRepository,
    private val localRepository: LocalRepository,
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

    override fun getRecordsAsc(): Flow<List<Record>> {
        return localRepository.getCurrentProfileId()
            .filterNotNull()
            .flatMapLatest { profileId ->
                recordDao.selectAllAsc(profileId)
            }.mapFlatten { toModel() }
    }

    override fun getRecord(id: Long): Flow<Record?> {
        return recordDao.selectById(id).map { it?.toModel() }
    }

    override suspend fun createRecord(data: RecordCreate) {
        return database.withTransaction {
            val recordId = recordDao.insert(data.toEntity())

            data.attachments
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
                val record = recordDao.selectById(id).first()!!.toModel()

                recordDao.deleteById(id)
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
            val oldRecord = recordDao.selectById(data.id).first()!!

            val newRecord = with(data) {
                when (this) {
                    is RecordUpdate.Transaction -> {
                        RecordCreate.Transaction(
                            profileId = oldRecord.profile.profileId,
                            accountId = accountId,
                            attachments = attachments,
                            description = description,
                            labels = labels,
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
                            attachments = attachments,
                            description = description,
                            labels = labels,
                            amount = amount,
                            date = date,
                            transferAmount = transferAmount,
                            transferAccountId = transferAccountId,
                        )
                    }
                }
            }

            deleteRecords(data.id)
            createRecord(newRecord)
        }
    }
}