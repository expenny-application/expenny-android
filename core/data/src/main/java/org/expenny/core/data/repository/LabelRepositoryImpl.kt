package org.expenny.core.data.repository

import androidx.room.withTransaction
import kotlinx.coroutines.flow.*
import org.expenny.core.common.extensions.mapFlatten
import org.expenny.core.data.mapper.DataMapper.toEntity
import org.expenny.core.data.mapper.DataMapper.toModel
import org.expenny.core.database.ExpennyDatabase
import org.expenny.core.domain.repository.LabelRepository
import org.expenny.core.domain.repository.LocalRepository
import org.expenny.core.model.label.Label
import org.expenny.core.model.label.LabelCreate
import org.expenny.core.model.label.LabelUpdate
import javax.inject.Inject

class LabelRepositoryImpl @Inject constructor(
    private val database: ExpennyDatabase,
    private val localRepository: LocalRepository
): LabelRepository {
    private val labelDao = database.labelDao()
    private val recordLabelDao = database.recordLabelDao()

    override fun getLabelsFlow(): Flow<List<Label>> {
        return localRepository.getCurrentProfileId()
            .filterNotNull()
            .flatMapLatest { profileId -> labelDao.selectByProfileId(profileId) }
            .mapFlatten { toModel() }
    }

    override suspend fun getLabel(id: Long): Label? {
        return labelDao.select(id)?.toModel()
    }

    override suspend fun createLabel(label: LabelCreate): Long {
        return labelDao.insert(label.toEntity())
    }

    override suspend fun updateLabel(label: LabelUpdate) {
        labelDao.update(label.toEntity())
    }

    override suspend fun deleteLabel(id: Long) {
        database.withTransaction {
            recordLabelDao.deleteAllByLabelId(id)
            labelDao.delete(id)
        }
    }
}