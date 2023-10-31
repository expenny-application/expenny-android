package org.expenny.core.database.model.embedded

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.expenny.core.database.model.crossref.RecordFileCrossRef
import org.expenny.core.database.model.crossref.RecordLabelCrossRef
import org.expenny.core.database.model.entity.*

data class RecordEmbedded(
    @Embedded
    val record: RecordEntity,

    @Relation(
        parentColumn = "profileId",
        entityColumn = "profileId",
    )
    val profile: ProfileEntity,

    @Relation(
        entity = AccountEntity::class,
        parentColumn = "accountId",
        entityColumn = "accountId",
    )
    val account: AccountEmbedded,

    @Relation(
        entity = AccountEntity::class,
        parentColumn = "transferAccountId",
        entityColumn = "accountId",
    )
    val transferAccount: AccountEmbedded?,

    @Relation(
        entity = CategoryEntity::class,
        parentColumn = "categoryId",
        entityColumn = "categoryId",
    )
    val category: CategoryEmbedded?,

    @Relation(
        entity = LabelEntity::class,
        parentColumn = "recordId",
        entityColumn = "labelId",
        associateBy = Junction(RecordLabelCrossRef::class)
    )
    val labels: List<LabelEmbedded>,

    @Relation(
        entity = FileEntity::class,
        parentColumn = "recordId",
        entityColumn = "fileId",
        associateBy = Junction(RecordFileCrossRef::class)
    )
    val receipts: List<FileEmbedded>
)