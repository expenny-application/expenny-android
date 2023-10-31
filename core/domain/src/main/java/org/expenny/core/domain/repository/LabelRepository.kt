package org.expenny.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.model.label.Label
import org.expenny.core.model.label.LabelCreate
import org.expenny.core.model.label.LabelUpdate

interface LabelRepository {

    fun getLabelsFlow(): Flow<List<Label>>

    suspend fun getLabel(id: Long): Label?

    suspend fun updateLabel(label: LabelUpdate)

    suspend fun createLabel(label: LabelCreate): Long

    suspend fun deleteLabel(id: Long)
}