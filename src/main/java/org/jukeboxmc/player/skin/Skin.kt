package org.jukeboxmc.player.skin

import com.nukkitx.protocol.bedrock.data.skin.AnimatedTextureType
import com.nukkitx.protocol.bedrock.data.skin.AnimationData
import com.nukkitx.protocol.bedrock.data.skin.ImageData
import com.nukkitx.protocol.bedrock.data.skin.PersonaPieceData
import com.nukkitx.protocol.bedrock.data.skin.PersonaPieceTintData
import com.nukkitx.protocol.bedrock.data.skin.SerializedSkin
import java.util.UUID

/**
 * @author LucGamesYT
 * @version 1.0
 */
class Skin {
    var skinId: String? = null
    private var resourcePatch: String? = null
    var geometryName = ""
    private var geometryData: String? = null
    private var animationData: String? = null
    private var capeId: String? = null
    var fullSkinId = UUID.randomUUID().toString()
    var skinColor = "#0"
    var armSize = "wide"
    var playFabId = ""
    var skinData: Image? = null
    private var capeData: Image? = null
    var isPremium = false
    var isPersona = false
    var isCapeOnClassic = false
    var isTrusted = false
    var isPrimaryUser = true
    var skinAnimations: MutableList<SkinAnimation?> = ArrayList()
    var personaPieces: MutableList<PersonaPiece?> = ArrayList()
    var personaPieceTints: MutableList<PersonaPieceTint?> = ArrayList()
    fun getResourcePatch(): String {
        return if (resourcePatch == null) "" else resourcePatch!!
    }

    fun setResourcePatch(resourcePatch: String?) {
        this.resourcePatch = resourcePatch
    }

    fun getGeometryData(): String {
        return if (geometryData == null) "" else geometryData!!
    }

    fun setGeometryData(geometryData: String?) {
        this.geometryData = geometryData
    }

    fun getAnimationData(): String {
        return if (animationData == null) "" else animationData!!
    }

    fun setAnimationData(animationData: String?) {
        this.animationData = animationData
    }

    fun getCapeId(): String {
        return if (capeId == null) "" else capeId!!
    }

    fun setCapeId(capeId: String?) {
        this.capeId = capeId
    }

    fun getCapeData(): Image {
        return if (capeData != null) capeData!! else Image(0, 0, ByteArray(0))
    }

    fun setCapeData(capeData: Image?) {
        this.capeData = capeData
    }

    fun toNetwork(): SerializedSkin {
        val animationDataList: MutableList<AnimationData> = ArrayList()
        for (animation in skinAnimations) {
            animationDataList.add(
                AnimationData(
                    ImageData.of(
                        animation!!.image.width,
                        animation!!.image.height,
                        animation!!.image.data,
                    ),
                    AnimatedTextureType.values()[animation.type],
                    animation.frames,
                ),
            )
        }
        val personaPieceDataList: MutableList<PersonaPieceData> = ArrayList()
        for (piece in personaPieces) {
            personaPieceDataList.add(
                PersonaPieceData(
                    piece!!.pieceId,
                    piece!!.pieceType,
                    piece!!.packId,
                    piece!!.isDefault,
                    piece!!.productId,
                ),
            )
        }
        val personaPieceTintList: MutableList<PersonaPieceTintData> = ArrayList()
        for (pieceTint in personaPieceTints) {
            personaPieceTintList.add(PersonaPieceTintData(pieceTint!!.pieceType, pieceTint.colors))
        }
        return SerializedSkin.builder()
            .skinId(skinId)
            .playFabId(playFabId)
            .geometryName(geometryName)
            .skinResourcePatch(resourcePatch)
            .skinData(ImageData.of(skinData!!.width, skinData!!.height, skinData!!.data))
            .animations(animationDataList)
            .capeData(ImageData.of(capeData!!.width, capeData!!.height, capeData!!.data))
            .geometryData(geometryData)
            .animationData(animationData)
            .premium(isPremium)
            .persona(isPersona)
            .capeOnClassic(isCapeOnClassic)
            .capeId(capeId)
            .fullSkinId(fullSkinId)
            .armSize(armSize)
            .skinColor(skinColor)
            .personaPieces(personaPieceDataList)
            .tintColors(personaPieceTintList)
            .build()
    }

    companion object {
        const val SINGLE_SKIN_SIZE = 8192
        const val DOUBLE_SKIN_SIZE = 16384
        const val SKIN_128_64_SIZE = 32768
        const val SKIN_128_128_SIZE = 65536
        fun fromNetwork(serializedSkin: SerializedSkin): Skin {
            val skin = Skin()
            skin.skinId = serializedSkin.skinId
            skin.playFabId = serializedSkin.playFabId
            skin.geometryName = serializedSkin.geometryName
            skin.setResourcePatch(serializedSkin.skinResourcePatch)
            skin.skinData = Image(
                serializedSkin.skinData.width,
                serializedSkin.skinData.height,
                serializedSkin.skinData.image,
            )
            val skinAnimations: MutableList<SkinAnimation?> = ArrayList()
            for (animation in serializedSkin.animations) {
                val image = Image(
                    animation.image.width,
                    animation.image.height,
                    animation.image.image,
                )
                skinAnimations.add(
                    SkinAnimation(
                        image,
                        animation.textureType.ordinal,
                        animation.frames,
                        animation.expressionType.ordinal,
                    ),
                )
            }
            skin.skinAnimations = skinAnimations
            skin.setCapeData(
                Image(
                    serializedSkin.capeData.width,
                    serializedSkin.capeData.height,
                    serializedSkin.capeData.image,
                ),
            )
            skin.setGeometryData(serializedSkin.geometryData)
            skin.setAnimationData(serializedSkin.animationData)
            skin.isPremium = serializedSkin.isPremium
            skin.isPersona = serializedSkin.isPersona
            skin.isCapeOnClassic = serializedSkin.isCapeOnClassic
            skin.setCapeId(serializedSkin.capeId)
            skin.fullSkinId = serializedSkin.fullSkinId
            skin.armSize = serializedSkin.armSize
            skin.skinColor = serializedSkin.skinColor
            val personaPieces: MutableList<PersonaPiece?> = ArrayList()
            for (personaPiece in serializedSkin.personaPieces) {
                personaPieces.add(
                    PersonaPiece(
                        personaPiece.id,
                        personaPiece.type,
                        personaPiece.packId,
                        personaPiece.productId,
                        personaPiece.isDefault,
                    ),
                )
            }
            skin.personaPieces = personaPieces
            val pieceTints: MutableList<PersonaPieceTint?> = ArrayList()
            for (tintColor in serializedSkin.tintColors) {
                pieceTints.add(PersonaPieceTint(tintColor.type, tintColor.colors))
            }
            skin.personaPieceTints = pieceTints
            return skin
        }
    }
}
