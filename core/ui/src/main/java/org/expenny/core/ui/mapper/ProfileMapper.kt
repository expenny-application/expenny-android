package org.expenny.core.ui.mapper

import org.expenny.core.common.extensions.join
import org.expenny.core.model.profile.Profile
import org.expenny.core.ui.data.ui.ProfileUi
import javax.inject.Inject

class ProfileMapper @Inject constructor() {

    operator fun invoke(model: Profile): ProfileUi {
        return with(model) {
            ProfileUi(
                id = id,
                name = name,
                currency = currencyUnit.code,
                displayName = model.name.join(currencyUnit.code)
            )
        }
    }

    operator fun invoke(model: List<Profile>): List<ProfileUi> {
        return model.map { invoke(it) }
    }
}