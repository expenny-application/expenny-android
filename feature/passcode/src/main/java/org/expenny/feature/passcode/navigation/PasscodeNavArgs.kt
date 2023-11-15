package org.expenny.feature.passcode.navigation

import org.expenny.feature.passcode.model.PasscodeType

data class PasscodeNavArgs(
    val type: PasscodeType = PasscodeType.Unlock
)