package org.jukeboxmc.player.data

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.crypto.factories.DefaultJWSVerifierFactory
import com.nimbusds.jose.proc.JWSVerifierFactory
import com.nukkitx.protocol.bedrock.packet.LoginPacket
import com.nukkitx.protocol.bedrock.util.EncryptionUtils
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
        decodeChainData(loginPacket.chainData.toString())
        decodeSkinData(loginPacket.skinData.toString())
    }

    private fun decodeChainData(chainData: String) {
        val map = GSON.fromJson<Map<String, List<String>>>(chainData, MutableMap::class.java)
        if (map.isEmpty() || !map.containsKey("chain") || map["chain"]!!.isEmpty()) {
            return
        }
        val chains = map["chain"]!!
        isXboxAuthenticated = try {
            verifyChains(chains)
        } catch (e: Exception) {
            false
        }
        chains.forEach { chain ->
            val chainMap = decodeToken(chain)
            if (chainMap.has("extraData")) {
                val extraData = chainMap["extraData"].asJsonObject
                displayName = extraData["displayName"].asString
                uuid = UUID.fromString(extraData["identity"].asString)
                xuid = extraData["XUID"].asString
            }
        }
    }

    private fun decodeSkinData(skinData: String) {
        val skinMap = decodeToken(skinData)
        if (skinMap.has("DeviceModel") && skinMap.has("DeviceId") &&
            skinMap.has("ClientRandomId") && skinMap.has("DeviceOS") && skinMap.has("GuiScale")
        ) {
            val deviceModel = skinMap["DeviceModel"].asString
            val deviceId = skinMap["DeviceId"].asString
            val clientId = skinMap["ClientRandomId"].asLong
            val deviceOS = skinMap["DeviceOS"].asInt
            val uiProfile = skinMap["UIProfile"].asInt
            deviceInfo = DeviceInfo(
                deviceModel,
                deviceId,
                clientId,
                Device.getDevice(deviceOS)!!,
                UIProfile.getById(uiProfile)!!,
            )
        }
        if (skinMap.has("LanguageCode")) {
            languageCode = skinMap["LanguageCode"].asString
        }
        if (skinMap.has("GameVersion")) {
            gameVersion = skinMap["GameVersion"].asString
        }
        skin = Skin()
        if (skinMap.has("SkinId")) {
            skin.skinId = (skinMap["SkinId"].asString)
        }
        if (skinMap.has("SkinResourcePatch")) {
            skin.setResourcePatch(
                String(
                    Base64.getDecoder().decode(skinMap["SkinResourcePatch"].asString),
                    StandardCharsets.UTF_8,
                ),
            )
        }
        if (skinMap.has("SkinGeometryData")) {
            skin.setGeometryData(
                String(
                    Base64.getDecoder().decode(skinMap["SkinGeometryData"].asString),
                    StandardCharsets.UTF_8,
                ),
            )
        }
        if (skinMap.has("AnimationData")) {
            skin.setAnimationData(
                String(
                    Base64.getDecoder().decode(skinMap["AnimationData"].asString),
                    StandardCharsets.UTF_8,
                ),
            )
        }
        if (skinMap.has("CapeId")) {
            skin.setCapeId(skinMap["CapeId"].asString)
        }
        if (skinMap.has("SkinColor")) {
            skin.skinColor = (skinMap["SkinColor"].asString)
        }
        if (skinMap.has("ArmSize")) {
            skin.armSize = (skinMap["ArmSize"].asString)
        }
        if (skinMap.has("PlayFabID")) {
            skin.playFabId = (skinMap["PlayFabID"].asString)
        }
        skin.skinData = (getImage(skinMap, "Skin"))
        skin.setCapeData(getImage(skinMap, "Cape"))
        if (skinMap.has("PremiumSkin")) {
            skin.isPremium = (skinMap["PremiumSkin"].asBoolean)
        }
        if (skinMap.has("PersonaSkin")) {
            skin.isPersona = (skinMap["PersonaSkin"].asBoolean)
        }
        if (skinMap.has("CapeOnClassicSkin")) {
            skin.isCapeOnClassic = (skinMap["CapeOnClassicSkin"].asBoolean)
        }
        if (skinMap.has("AnimatedImageData")) {
            val array = skinMap["AnimatedImageData"].asJsonArray
            for (jsonElement in array) {
                skin.skinAnimations.add(getSkinAnimationData(jsonElement.asJsonObject))
            }
        }
        if (skinMap.has("PersonaPieces")) {
            val array = skinMap["PersonaPieces"].asJsonArray
            for (jsonElement in array) {
                skin.personaPieces.add(getPersonaPiece(jsonElement.asJsonObject))
            }
        }
        if (skinMap.has("PieceTintColors")) {
            val array = skinMap["PieceTintColors"].asJsonArray
            for (jsonElement in array) {
                skin.personaPieceTints.add(getPersonaPieceTint(jsonElement.asJsonObject))
            }
        }
    }

    @Throws(Exception::class)
    private fun verifyChains(chains: List<String>): Boolean {
        var lastKey: PublicKey? = null
        var mojangKeyVerified = false
        for (chain in chains) {
            val jws: JWSObject = JWSObject.parse(chain)
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

    private fun decodeToken(token: String): JsonObject {
        val tokenSplit = token.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        require(tokenSplit.size >= 2) { "Invalid token length" }
        return GSON.fromJson(
            String(
                Base64.getDecoder().decode(
                    tokenSplit[1],
                ),
                StandardCharsets.UTF_8,
            ),
            JsonObject::class.java,
        )
    }

    private fun getImage(skinMap: JsonObject, name: String): Image? {
        if (skinMap.has(name + "Data")) {
            val skinImage = Base64.getDecoder().decode(skinMap[name + "Data"].asString)
            return if (skinMap.has(name + "ImageHeight") && skinMap.has(name + "ImageWidth")) {
                val width = skinMap[name + "ImageWidth"].asInt
                val height = skinMap[name + "ImageHeight"].asInt
                Image(width, height, skinImage)
            } else {
                Image.getImage(skinImage)
            }
        }
        return Image(0, 0, ByteArray(0))
    }

    private fun getSkinAnimationData(animationData: JsonObject): SkinAnimation {
        val data = Base64.getDecoder().decode(animationData["Image"].asString)
        val width = animationData["ImageWidth"].asInt
        val height = animationData["ImageHeight"].asInt
        val frames = animationData["Frames"].asFloat
        val type = animationData["Type"].asInt
        val expression = animationData["AnimationExpression"].asInt
        return SkinAnimation(Image(width, height, data), type, frames, expression)
    }

    private fun getPersonaPiece(personaPiece: JsonObject): PersonaPiece {
        val pieceId = personaPiece["PieceId"].asString
        val pieceType = personaPiece["PieceType"].asString
        val packId = personaPiece["PackId"].asString
        val productId = personaPiece["ProductId"].asString
        val isDefault = personaPiece["IsDefault"].asBoolean
        return PersonaPiece(pieceId, pieceType, packId, productId, isDefault)
    }

    private fun getPersonaPieceTint(personaPiceTint: JsonObject): PersonaPieceTint {
        val pieceType = personaPiceTint["PieceType"].asString
        val colors: MutableList<String> = ArrayList()
        for (element in personaPiceTint["Colors"].asJsonArray) {
            colors.add(element.asString)
        }
        return PersonaPieceTint(pieceType, colors)
    }

    companion object {
        private val GSON = Gson()
        private val JWS_VERIFIER_FACTORY: JWSVerifierFactory = DefaultJWSVerifierFactory()

        @Throws(JOSEException::class)
        private fun verify(publicKey: PublicKey, jwsObject: JWSObject): Boolean {
            return jwsObject.verify(JWS_VERIFIER_FACTORY.createJWSVerifier(jwsObject.header, publicKey))
        }
    }
}
