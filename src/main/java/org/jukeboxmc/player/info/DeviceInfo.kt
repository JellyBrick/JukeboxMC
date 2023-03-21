package org.jukeboxmc.player.info

import lombok.ToString

/**
 * @author LucGamesYT
 * @version 1.0
 */
@ToString
class DeviceInfo(
    val deviceName: String,
    val deviceId: String,
    val clientId: Long,
    val device: Device,
    val uIProfile: UIProfile
)