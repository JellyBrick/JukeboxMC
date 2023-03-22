package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import com.nukkitx.protocol.bedrock.data.SoundEvent
import org.apache.commons.math3.util.FastMath
import org.jukeboxmc.Server
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.data.UpdateReason
import org.jukeboxmc.event.block.BlockFromToEvent
import org.jukeboxmc.event.block.BlockLiquidFlowEvent
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.util.Utils

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class BlockLiquid : Block {
    var adjacentSources = 0
    private val CAN_FLOW_DOWN: Byte = 1
    private val CAN_FLOW: Byte = 0
    private val BLOCKED: Byte = -1
    private val flowCostVisited: MutableMap<Long, Byte> = HashMap()

    constructor(identifier: Identifier?) : super(identifier)
    constructor(identifier: Identifier?, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun onUpdate(updateReason: UpdateReason): Long {
        if (updateReason == UpdateReason.NORMAL) {
            if (usesWaterLogging() && layer > 0) {
                val layer0 = location.world!!.getBlock(location, 0)
                if (layer0.type == BlockType.AIR) {
                    location.world!!.setBlock(
                        location,
                        create<Block>(BlockType.AIR),
                        1,
                        location.dimension,
                        false,
                    )
                    location.world?.setBlock(location, this, 0, location.dimension, false)
                } else if (layer0 is Waterlogable && ((layer0 as Waterlogable).waterLoggingLevel <= 0 || (layer0 as Waterlogable).waterLoggingLevel == 1 && liquidDepth > 0)) {
                    location.world?.setBlock(
                        location,
                        create<Block>(BlockType.AIR),
                        1,
                        location.dimension,
                        true,
                    )
                }
            }
            checkForHarden()
            location.world?.scheduleBlockUpdate(location, tickRate.toLong())
            return 0
        } else if (updateReason == UpdateReason.SCHEDULED) {
            var decay = getFlowDecay(this)
            val multiplier = flowDecayPerBlock
            if (decay > 0) {
                var smallestFlowDecay = -100
                adjacentSources = 0
                smallestFlowDecay = getSmallestFlowDecay(
                    location.world!!.getBlock(
                        location.x.toInt(),
                        location.y.toInt(),
                        location.z.toInt() - 1,
                        0,
                        location.dimension,
                    ),
                    smallestFlowDecay,
                )
                smallestFlowDecay = getSmallestFlowDecay(
                    location.world!!.getBlock(
                        location.x.toInt(),
                        location.y.toInt(),
                        location.z.toInt() + 1,
                        0,
                        location.dimension,
                    ),
                    smallestFlowDecay,
                )
                smallestFlowDecay = getSmallestFlowDecay(
                    location.world!!.getBlock(
                        location.y.toInt() - 1,
                        location.y.toInt(),
                        location.z.toInt(),
                        0,
                        location.dimension,
                    ),
                    smallestFlowDecay,
                )
                smallestFlowDecay = getSmallestFlowDecay(
                    location.world!!.getBlock(
                        location.x.toInt() + 1,
                        location.y.toInt(),
                        location.z.toInt(),
                        0,
                        location.dimension,
                    ),
                    smallestFlowDecay,
                )
                var newDecay = smallestFlowDecay + multiplier
                if (newDecay >= 8 || smallestFlowDecay < 0) {
                    newDecay = -1
                }
                val topFlowDecay = getFlowDecay(
                    location.world!!.getBlock(
                        location.x.toInt(),
                        location.y.toInt() + 1,
                        location.z.toInt(),
                        0,
                        location.dimension,
                    ),
                )
                if (topFlowDecay >= 0) {
                    newDecay = topFlowDecay or 0x08
                }
                if (adjacentSources >= 2 && this is BlockWater) {
                    var bottomBlock = location.world!!.getBlock(
                        location.x.toInt(),
                        location.y.toInt() - 1,
                        location.z.toInt(),
                        0,
                        location.dimension,
                    )
                    if (bottomBlock.isSolid) {
                        newDecay = 0
                    } else if (bottomBlock is BlockWater && bottomBlock.liquidDepth == 0) {
                        newDecay = 0
                    } else {
                        bottomBlock = bottomBlock.location.world!!.getBlock(location, 1)
                        if (bottomBlock is BlockWater && bottomBlock.liquidDepth == 0) {
                            newDecay = 0
                        }
                    }
                }
                if (newDecay != decay) {
                    decay = newDecay
                    val decayed = decay < 0
                    val to: Block
                    to = if (decayed) {
                        create<Block>(BlockType.AIR)
                    } else {
                        getBlock(decay)
                    }
                    val event = BlockFromToEvent(this, to)
                    Server.instance.pluginManager.callEvent(event)
                    if (!event.isCancelled) {
                        location.world!!.setBlock(location, event.blockTo, layer, location.dimension, true)
                        if (!decayed) {
                            location.world!!.scheduleBlockUpdate(location, tickRate.toLong())
                        }
                    }
                }
            }
            if (decay >= 0) {
                val bottomBlock = location.world!!.getBlock(
                    location.x.toInt(),
                    location.y.toInt() - 1,
                    location.z.toInt(),
                    0,
                    location.dimension,
                )
                flowIntoBlock(bottomBlock, decay or 0x08)
                if (decay == 0 || !(if (usesWaterLogging()) bottomBlock.canWaterloggingFlowInto() else bottomBlock.canBeFlowedInto())) {
                    val adjacentDecay: Int
                    adjacentDecay = if (decay >= 8) {
                        1
                    } else {
                        decay + multiplier
                    }
                    if (adjacentDecay < 8) {
                        val flags = optimalFlowDirections
                        if (flags[0]) {
                            flowIntoBlock(
                                location.world!!.getBlock(
                                    location.x.toInt() - 1,
                                    location.y.toInt(),
                                    location.z.toInt(),
                                    0,
                                    location.dimension,
                                ),
                                adjacentDecay,
                            )
                        }
                        if (flags[1]) {
                            flowIntoBlock(
                                location.world!!.getBlock(
                                    location.x.toInt() + 1,
                                    location.y.toInt(),
                                    location.z.toInt(),
                                    0,
                                    location.dimension,
                                ),
                                adjacentDecay,
                            )
                        }
                        if (flags[2]) {
                            flowIntoBlock(
                                location.world!!.getBlock(
                                    location.x.toInt(),
                                    location.y.toInt(),
                                    location.z.toInt() - 1,
                                    0,
                                    location.dimension,
                                ),
                                adjacentDecay,
                            )
                        }
                        if (flags[3]) {
                            flowIntoBlock(
                                location.world!!.getBlock(
                                    location.x.toInt(),
                                    location.y.toInt(),
                                    location.z.toInt() + 1,
                                    0,
                                    location.dimension,
                                ),
                                adjacentDecay,
                            )
                        }
                    }
                }
                checkForHarden()
            }
        }
        return 0
    }

    abstract fun getBlock(liquidDepth: Int): BlockLiquid
    protected open fun checkForHarden() {}
    override fun canBeFlowedInto(): Boolean {
        return true
    }

    open fun usesWaterLogging(): Boolean {
        return false
    }

    open val flowDecayPerBlock: Int
        get() = 1

    protected fun getFlowDecay(block: Block): Int {
        if (block.type != this.type) {
            val layer1 = block.location.world!!.getBlock(location, 1)
            return if (layer1.type != this.type) {
                -1
            } else {
                (layer1 as BlockLiquid).liquidDepth
            }
        }
        return (block as BlockLiquid).liquidDepth
    }

    private fun getSmallestFlowDecay(block: Block, decay: Int): Int {
        var blockDecay = getFlowDecay(block)
        if (blockDecay < 0) {
            return decay
        } else if (blockDecay == 0) {
            ++adjacentSources
        } else if (blockDecay >= 8) {
            blockDecay = 0
        }
        return if (decay >= 0 && blockDecay >= decay) decay else blockDecay
    }

    protected fun canFlowInto(block: Block): Boolean {
        if (usesWaterLogging()) {
            if (block.canWaterloggingFlowInto()) {
                val blockLayer1 = location.world!!.getBlock(location, 1)
                return !(block is BlockLiquid && block.liquidDepth == 0) && !(blockLayer1 is BlockLiquid && blockLayer1.liquidDepth == 0)
            }
        }
        return block.canBeFlowedInto() && !(block is BlockLiquid && block.liquidDepth == 0)
    }

    protected open fun flowIntoBlock(block: Block, newFlowDecay: Int) {
        var block = block
        if (canFlowInto(block) && block !is BlockLiquid) {
            if (usesWaterLogging()) {
                val layer1 = location.world!!.getBlock(location, 1)
                if (layer1 is BlockLiquid) {
                    return
                }
                if (block is Waterlogable && (block as Waterlogable).waterLoggingLevel > 1) {
                    block = layer1
                }
            }
            val event = BlockLiquidFlowEvent(block, this, newFlowDecay)
            Server.instance.pluginManager.callEvent(event)
            if (!event.isCancelled) {
                if (block.layer == 0 && block.type != BlockType.AIR) {
                    // TODO DROP ITEM IF LIQUID IS COLLIDED
                    // this.location.getWorld().breakBlock( null, block.location, block.getType() == BlockType.WEB ? Item.create( ItemType.WOODEN_SWORD ) : null );
                }
                location.world!!.setBlock(block.location, getBlock(newFlowDecay), block.layer)
                location.world!!.scheduleBlockUpdate(block.location, tickRate.toLong())
            }
        }
    }

    private val optimalFlowDirections: BooleanArray
        private get() {
            val flowCost = intArrayOf(
                1000,
                1000,
                1000,
                1000,
            )
            var maxCost = 4 / flowDecayPerBlock
            for (j in 0..3) {
                var x = location.x.toInt()
                val y = location.y.toInt()
                var z = location.z.toInt()
                if (j == 0) {
                    --x
                } else if (j == 1) {
                    ++x
                } else if (j == 2) {
                    --z
                } else {
                    ++z
                }
                val block = location.world!!.getBlock(x, y, z, 0, location.dimension)
                if (!canFlowInto(block)) {
                    flowCostVisited[Utils.blockHash(x, y, z)] = BLOCKED
                } else if (if (usesWaterLogging()) {
                        location.world!!.getBlock(x, y - 1, z, 0, location.dimension)
                            .canWaterloggingFlowInto()
                    } else {
                        location.world!!.getBlock(x, y - 1, z, 0, location.dimension)
                            .canBeFlowedInto()
                    }
                ) {
                    flowCostVisited[Utils.blockHash(x, y, z)] = CAN_FLOW_DOWN
                    maxCost = 0
                    flowCost[j] = maxCost
                } else if (maxCost > 0) {
                    flowCostVisited[Utils.blockHash(x, y, z)] = CAN_FLOW
                    flowCost[j] = calculateFlowCost(x, y, z, 1, maxCost, j xor 0x01, j xor 0x01)
                    maxCost = FastMath.min(maxCost, flowCost[j])
                }
            }
            flowCostVisited.clear()
            var minCost = Double.MAX_VALUE
            for (i in 0..3) {
                val d = flowCost[i].toDouble()
                if (d < minCost) {
                    minCost = d
                }
            }
            val isOptimalFlowDirection = BooleanArray(4)
            for (i in 0..3) {
                isOptimalFlowDirection[i] = flowCost[i].toDouble() == minCost
            }
            return isOptimalFlowDirection
        }

    private fun calculateFlowCost(
        blockX: Int,
        blockY: Int,
        blockZ: Int,
        accumulatedCost: Int,
        maxCost: Int,
        originOpposite: Int,
        lastOpposite: Int,
    ): Int {
        var cost = 1000
        for (j in 0..3) {
            if (j == originOpposite || j == lastOpposite) {
                continue
            }
            var x = blockX
            var z = blockZ
            if (j == 0) {
                --x
            } else if (j == 1) {
                ++x
            } else if (j == 2) {
                --z
            } else {
                ++z
            }
            val hash = Utils.blockHash(x, blockY, z)
            if (!flowCostVisited.containsKey(hash)) {
                val blockSide = location.world!!.getBlock(x, blockY, z, 0, location.dimension)
                if (!canFlowInto(blockSide)) {
                    flowCostVisited[hash] = BLOCKED
                } else if (if (usesWaterLogging()) {
                        location.world!!.getBlock(x, blockY - 1, z, 0, location.dimension)
                            .canWaterloggingFlowInto()
                    } else {
                        location.world!!.getBlock(x, blockY - 1, z, 0, location.dimension)
                            .canBeFlowedInto()
                    }
                ) {
                    flowCostVisited[hash] = CAN_FLOW_DOWN
                } else {
                    flowCostVisited[hash] = CAN_FLOW
                }
            }
            val status = flowCostVisited[hash]!!
            if (status == BLOCKED) {
                continue
            } else if (status == CAN_FLOW_DOWN) {
                return accumulatedCost
            }
            if (accumulatedCost >= maxCost) {
                continue
            }
            val realCost = calculateFlowCost(x, blockY, z, accumulatedCost + 1, maxCost, originOpposite, j xor 0x01)
            if (realCost < cost) {
                cost = realCost
            }
        }
        return cost
    }

    fun liquidCollide(result: Block) {
        val event = BlockFromToEvent(this, result)
        location.world!!.server.pluginManager.callEvent(event)
        if (event.isCancelled) {
            return
        }
        location.world!!.setBlock(location, event.blockTo, 0)
        location.world!!.playSound(location.add(0.5f, 0.5f, 0.5f), SoundEvent.FIZZ)
    }

    open val tickRate: Int
        get() = 10

    fun setLiquidDepth(value: Int): BlockLiquid {
        return setState("liquid_depth", value)
    }

    val liquidDepth: Int
        get() = if (stateExists("liquid_depth")) getIntState("liquid_depth") else 0
}
