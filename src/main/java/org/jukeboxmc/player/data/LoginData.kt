package org.jukeboxmc.player.data

import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.crypto.factories.DefaultJWSVerifierFactory
import com.nimbusds.jose.proc.JWSVerifierFactory
import com.nimbusds.jose.shaded.json.parser.ParseException
import com.nimbusds.jwt.SignedJWT
import org.cloudburstmc.protocol.bedrock.packet.LoginPacket
import org.cloudburstmc.protocol.bedrock.util.EncryptionUtils
import org.jukeboxmc.player.info.Device
import org.jukeboxmc.player.info.DeviceInfo
import org.jukeboxmc.player.info.UIProfile
import org.jukeboxmc.player.skin.Image
import org.jukeboxmc.player.skin.PersonaPiece
import org.jukeboxmc.player.skin.PersonaPieceTint
import org.jukeboxmc.player.skin.Skin
import org.jukeboxmc.player.skin.SkinAnimation
import java.nio.charset.StandardCharsets
import java.security.PublicKey
import java.util.Base64
import java.util.UUID

/**
 * @author LucGamesYT
 * @version 1.0
 */
class LoginData(loginPacket: LoginPacket) {
    var isXboxAuthenticated = false
        private set
    lateinit var displayName: String
        private set
    lateinit var xuid: String
        private set
    lateinit var uuid: UUID
        private set
    lateinit var deviceInfo: DeviceInfo
        private set
    lateinit var languageCode: String
        private set
    lateinit var gameVersion: String
        private set
    lateinit var skin: Skin
        private set

    init {
        decodeChainData(loginPacket.chain)
        decodeSkinData(loginPacket.extra)
    }

    private fun decodeChainData(chainData: List<SignedJWT>) {
        isXboxAuthenticated = try {
            verifyChains(chainData)
        } catch (e: JOSEException) {
            false
        } catch (e: ParseException) {
            false
        }
        chainData.forEach {
            val chainMap = decodeToken(it)
            if (chainMap.containsKey("extraData")) {
                val extraData = chainMap["extraData"] as Map<*, *>
                displayName = extraData["displayName"] as String
                uuid = UUID.fromString(extraData["identity"] as String)
                xuid = extraData["XUID"] as String
            }
        }
    }

    private fun decodeSkinData(skinData: SignedJWT) {
        val skinMap = decodeToken(skinData)
        if (skinMap.containsKey("DeviceModel") && skinMap.containsKey("DeviceId") &&
            skinMap.containsKey("ClientRandomId") && skinMap.containsKey("DeviceOS") && skinMap.containsKey("GuiScale")
        ) {
            val deviceModel = skinMap["DeviceModel"] as String
            val deviceId = skinMap["DeviceId"] as String
            val clientId = skinMap["ClientRandomId"] as Long
            val deviceOS = skinMap["DeviceOS"] as Long
            val uiProfile = skinMap["UIProfile"] as Long
            deviceInfo = DeviceInfo(
                deviceModel,
                deviceId,
                clientId,
                Device.getDevice(deviceOS.toInt()),
                UIProfile.getById(uiProfile.toInt()),
            )
        }
        if (skinMap.containsKey("LanguageCode")) {
            languageCode = skinMap["LanguageCode"] as String
        }
        if (skinMap.containsKey("GameVersion")) {
            gameVersion = skinMap["GameVersion"] as String
        }
        skin = Skin()
        if (skinMap.containsKey("SkinId")) {
            skin.skinId = skinMap["SkinId"] as String
        }
        if (skinMap.containsKey("SkinResourcePatch")) {
            skin.setResourcePatch(
                String(
                    Base64.getDecoder().decode(skinMap["SkinResourcePatch"] as String),
                    StandardCharsets.UTF_8,
                ),
            )
        }
        if (skinMap.containsKey("SkinGeometryData")) {
            skin.setGeometryData(
                String(
                    Base64.getDecoder().decode(skinMap["SkinGeometryData"] as String),
                    StandardCharsets.UTF_8,
                ),
            )
        }
        if (skinMap.containsKey("AnimationData")) {
            skin.setAnimationData(
                String(
                    Base64.getDecoder().decode(skinMap["AnimationData"] as String),
                    StandardCharsets.UTF_8,
                ),
            )
        }
        if (skinMap.containsKey("CapeId")) {
            skin.setCapeId(skinMap["CapeId"] as String)
        }
        if (skinMap.containsKey("SkinColor")) {
            skin.skinColor = skinMap["SkinColor"] as String
        }
        if (skinMap.containsKey("ArmSize")) {
            skin.armSize = skinMap["ArmSize"] as String
        }
        if (skinMap.containsKey("PlayFabID")) {
            skin.playFabId = skinMap["PlayFabID"] as String
        }
        skin.skinData = getImage(skinMap, "Skin")
        skin.setCapeData(getImage(skinMap, "Cape"))
        if (skinMap.containsKey("PremiumSkin")) {
            skin.isPremium = skinMap["PremiumSkin"] as Boolean
        }
        if (skinMap.containsKey("PersonaSkin")) {
            skin.isPersona = skinMap["PersonaSkin"] as Boolean
        }
        if (skinMap.containsKey("CapeOnClassicSkin")) {
            skin.isCapeOnClassic = skinMap["CapeOnClassicSkin"] as Boolean
        }
        if (skinMap.containsKey("AnimatedImageData")) {
            val array = skinMap["AnimatedImageData"] as List<*>
            for (jsonElement in array) {
                skin.skinAnimations.add(getSkinAnimationData(jsonElement as Map<*, *>))
            }
        }
        if (skinMap.containsKey("PersonaPieces")) {
            val array = skinMap["PersonaPieces"] as List<*>
            for (jsonElement in array) {
                skin.personaPieces.add(getPersonaPiece(jsonElement as Map<*, *>))
            }
        }
        if (skinMap.containsKey("PieceTintColors")) {
            val array = skinMap["PieceTintColors"] as List<*>
            for (jsonElement in array) {
                skin.personaPieceTints.add(getPersonaPieceTint(jsonElement as Map<*, *>))
            }
        }
    }

    @Throws(JOSEException::class, ParseException::class)
    private fun verifyChains(chains: List<SignedJWT>): Boolean {
        var lastKey: PublicKey? = null
        var mojangKeyVerified = false
        for (jws in chains) {
            if (!mojangKeyVerified) {
                mojangKeyVerified = verify(EncryptionUtils.getMojangPublicKey(), jws)
            }
            if (lastKey != null && !verify(lastKey, jws)) {
                throw JOSEException("Unable to verify key in chain.")
            }
            val payload: Map<String, Any> = jws.payload.toJSONObject()
            val base64key = payload["identityPublicKey"] as String? ?: throw RuntimeException("No key found")
            lastKey = EncryptionUtils.generateKey(base64key)
        }
        return mojangKeyVerified
    }

    private fun decodeToken(token: SignedJWT): Map<String, *> {
        return token.payload.toJSONObject()
    }

    private fun getImage(skinMap: Map<String, *>, name: String): Image? {
        if (skinMap.containsKey(name + "Data")) {
            val skinImage = Base64.getDecoder().decode(skinMap[name + "Data"] as String)
            return if (skinMap.containsKey(name + "ImageHeight") && skinMap.containsKey(name + "ImageWidth")) {
                val width = skinMap[name + "ImageWidth"] as Long
                val height = skinMap[name + "ImageHeight"] as Long
                Image(width.toInt(), height.toInt(), skinImage)
            } else {
                Image.getImage(skinImage)
            }
        }
        return Image(0, 0, ByteArray(0))
    }

    private fun getSkinAnimationData(animationData: Map<*, *>): SkinAnimation {
        val data = Base64.getDecoder().decode(animationData["Image"] as String)
        val width = animationData["ImageWidth"] as Int
        val height = animationData["ImageHeight"] as Int
        val frames = animationData["Frames"] as Float
        val type = animationData["Type"] as Int
        val expression = animationData["AnimationExpression"] as Int
        return SkinAnimation(Image(width, height, data), type, frames, expression)
    }

    private fun getPersonaPiece(personaPiece: Map<*, *>): PersonaPiece {
        val pieceId = personaPiece["PieceId"] as String
        val pieceType = personaPiece["PieceType"] as String
        val packId = personaPiece["PackId"] as String
        val productId = personaPiece["ProductId"] as String
        val isDefault = personaPiece["IsDefault"] as Boolean
        return PersonaPiece(pieceId, pieceType, packId, productId, isDefault)
    }

    private fun getPersonaPieceTint(personaPiceTint: Map<*, *>): PersonaPieceTint {
        val pieceType = personaPiceTint["PieceType"] as String
        return PersonaPieceTint(pieceType, personaPiceTint["Colors"] as List<String>)
    }

    companion object {
        private val JWS_VERIFIER_FACTORY: JWSVerifierFactory = DefaultJWSVerifierFactory()

        @Throws(JOSEException::class)
        private fun verify(publicKey: PublicKey, jwsObject: JWSObject): Boolean {
            return jwsObject.verify(JWS_VERIFIER_FACTORY.createJWSVerifier(jwsObject.header, publicKey))
        }
    }
}
