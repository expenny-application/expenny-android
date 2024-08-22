package org.expenny.core.domain.usecase.institution

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import org.expenny.core.common.utils.Constants.IS_GOCARDLESS_SDK_ENABLED_CONFIG_KEY
import org.expenny.core.datastore.BuildConfig
import javax.inject.Inject

class GetInstitutionSyncEnabledUseCase @Inject constructor() {

    operator fun invoke(): Boolean {
        return BuildConfig.GOCARDLESS_SECRET_ID.isNotBlank() &&
            BuildConfig.GOCARDLESS_SECRET_KEY.isNotBlank() &&
            Firebase.remoteConfig.getBoolean(IS_GOCARDLESS_SDK_ENABLED_CONFIG_KEY)
    }
}