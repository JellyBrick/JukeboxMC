package org.jukeboxmc.player.skin

import java.util.UUID
import lombok.ToString

/**
 * @author LucGamesYT
 * @version 1.0
 */
@ToString
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
        val animationDataList: MutableList<AnimationData> = ArrayList<AnimationData>()
        for (animation in skinAnimations) {
            animationDataList.add(
                AnimationData(
                    ImageData.of(
                        animation.getImage().width,
                        animation.getImage().height,
                        animation.getImage().data
                    ), AnimatedTextureType.values().get(animation.getType()), animation.getFrames()
                )
            )
        }
        val personaPieceDataList: MutableList<PersonaPieceData> = ArrayList<PersonaPieceData>()
        for (piece in personaPieces) {
            personaPieceDataList.add(
                PersonaPieceData(
                    piece.getPieceId(),
                    piece.getPieceType(),
                    piece.getPackId(),
                    piece!!.isDefault,
                    piece.productId
                )
            )
        }
        val personaPieceTintList: MutableList<PersonaPieceTintData> = ArrayList<PersonaPieceTintData>()
        for (pieceTint in personaPieceTints) {
            personaPieceTintList.add(PersonaPieceTintData(pieceTint.getPieceType(), pieceTint.getColors()))
        }
        return SerializedSkin.builder()
            .skinId(skinId)
            .playFabId(playFabId)
            .geometryName(geometryName)
            .skinResourcePatch(resourcePatch)
            .skinData(ImageData.of(skinData.getWidth(), skinData.getHeight(), skinData.getData()))
            .animations(animationDataList)
            .capeData(ImageData.of(capeData.getWidth(), capeData.getHeight(), capeData.getData()))
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
            skin.skinId = serializedSkin.getSkinId()
            skin.playFabId = serializedSkin.getPlayFabId()
            skin.geometryName = serializedSkin.getGeometryName()
            skin.setResourcePatch(serializedSkin.getSkinResourcePatch())
            skin.skinData = Image(
                serializedSkin.getSkinData().getWidth(),
                serializedSkin.getSkinData().getHeight(),
                serializedSkin.getSkinData().getImage()
            )
            val skinAnimations: MutableList<SkinAnimation?> = ArrayList()
            for (animation in serializedSkin.getAnimations()) {
                val image = Image(
                    animation.getImage().getWidth(),
                    animation.getImage().getHeight(),
                    animation.getImage().getImage()
                )
                skinAnimations.add(
                    SkinAnimation(
                        image,
                        animation.getTextureType().ordinal,
                        animation.getFrames(),
                        animation.getExpressionType().ordinal
                    )
                )
            }
            skin.skinAnimations = skinAnimations
            skin.setCapeData(
                Image(
                    serializedSkin.getCapeData().getWidth(),
                    serializedSkin.getCapeData().getHeight(),
                    serializedSkin.getCapeData().getImage()
                )
            )
            skin.setGeometryData(serializedSkin.getGeometryData())
            skin.setAnimationData(serializedSkin.getAnimationData())
            skin.isPremium = serializedSkin.isPremium()
            skin.isPersona = serializedSkin.isPersona()
            skin.isCapeOnClassic = serializedSkin.isCapeOnClassic()
            skin.setCapeId(serializedSkin.getCapeId())
            skin.fullSkinId = serializedSkin.getFullSkinId()
            skin.armSize = serializedSkin.getArmSize()
            skin.skinColor = serializedSkin.getSkinColor()
            val personaPieces: MutableList<PersonaPiece?> = ArrayList()
            for (personaPiece in serializedSkin.getPersonaPieces()) {
                personaPieces.add(
                    PersonaPiece(
                        personaPiece.getId(),
                        personaPiece.getType(),
                        personaPiece.getPackId(),
                        personaPiece.getProductId(),
                        personaPiece.isDefault()
                    )
                )
            }
            skin.personaPieces = personaPieces
            val pieceTints: MutableList<PersonaPieceTint?> = ArrayList()
            for (tintColor in serializedSkin.getTintColors()) {
                pieceTints.add(PersonaPieceTint(tintColor.getType(), tintColor.getColors()))
            }
            skin.personaPieceTints = pieceTints
            return skin
        }
    }
}