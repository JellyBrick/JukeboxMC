package org.jukeboxmc.entity

import com.nukkitx.math.vector.Vector3f
import com.nukkitx.protocol.bedrock.packet.MoveEntityAbsolutePacket
import org.apache.commons.math3.util.FastMath
import org.jukeboxmc.Server
import org.jukeboxmc.math.Location
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import java.util.Random

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class EntityMoveable : Entity() {
    protected var ySize = 0f
    open var gravity = 0.08f
        protected set
    open var drag = 0.02f
        protected set
    var isCollidedVertically = false
        protected set
    var isCollidedHorizontally = false
        protected set
    var isCollided = false
        protected set
    override var fallDistance: Float = 0f
    protected fun checkObstruction(x: Float, y: Float, z: Float) {
        if (location.world?.getCollisionCubes(this, boundingBox, false)?.size == 0) {
            return
        }
        val i = FastMath.floor(x.toDouble()).toInt()
        val j = FastMath.floor(y.toDouble()).toInt()
        val k = FastMath.floor(z.toDouble()).toInt()
        val diffX = x - i
        val diffY = y - j
        val diffZ = z - k
        if (!this.world!!.getBlock(i, j, k, 0, location.dimension).isTransparent) {
            val flag = this.world!!.getBlock(i - 1, j, k, 0, location.dimension).isTransparent
            val flag1 = this.world!!.getBlock(i + 1, j, k, 0, location.dimension).isTransparent
            val flag2 = this.world!!.getBlock(i, j - 1, k, 0, location.dimension).isTransparent
            val flag3 = this.world!!.getBlock(i, j + 1, k, 0, location.dimension).isTransparent
            val flag4 = this.world!!.getBlock(i, j, k - 1, 0, location.dimension).isTransparent
            val flag5 = this.world!!.getBlock(i, j, k + 1, 0, location.dimension).isTransparent
            var direction = -1
            var limit = 9999.0
            if (flag) {
                limit = diffX.toDouble()
                direction = 0
            }
            if (flag1 && 1 - diffX < limit) {
                limit = (1 - diffX).toDouble()
                direction = 1
            }
            if (flag2 && diffY < limit) {
                limit = diffY.toDouble()
                direction = 2
            }
            if (flag3 && 1 - diffY < limit) {
                limit = (1 - diffY).toDouble()
                direction = 3
            }
            if (flag4 && diffZ < limit) {
                limit = diffZ.toDouble()
                direction = 4
            }
            if (flag5 && 1 - diffZ < limit) {
                direction = 5
            }
            val force = Random().nextFloat() * 0.2f + 0.1f
            if (direction == 0) {
                velocity = velocity.subtract(force, 0f, 0f)
                return
            }
            if (direction == 1) {
                velocity.setX(force)
                return
            }
            if (direction == 2) {
                velocity = velocity.subtract(0f, force, 0f)
                return
            }
            if (direction == 3) {
                velocity.setY(force)
                return
            }
            if (direction == 4) {
                velocity = velocity.subtract(0f, 0f, force)
                return
            }
            if (direction == 5) {
                velocity.setZ(force)
            }
        }
    }

    protected fun move(velocity: Vector) {
        if (velocity.getX() == 0f && velocity.getY() == 0f && velocity.getZ() == 0f) {
            return
        }
        ySize *= 0.4.toFloat()
        val movX = velocity.getX()
        val movY = velocity.getY()
        val movZ = velocity.getZ()
        val list =
            this.world!!.getCollisionCubes(
                this,
                boundingBox.addCoordinates(velocity.getX(), velocity.getY(), velocity.getZ()),
                false,
            )
        for (bb in list) {
            velocity.setX(bb.calculateYOffset(boundingBox, velocity.getY()))
        }
        boundingBox.offset(0f, velocity.getY(), 0f)
        for (bb in list) {
            velocity.setX(bb.calculateXOffset(boundingBox, velocity.getX()))
        }
        boundingBox.offset(velocity.getX(), 0f, 0f)
        for (bb in list) {
            velocity.setZ(bb.calculateZOffset(boundingBox, velocity.getZ()))
        }
        boundingBox.offset(0f, 0f, velocity.getZ())
        location.setX((boundingBox.minX + boundingBox.maxX) / 2)
        location.setY(boundingBox.minY - ySize)
        location.setZ((boundingBox.minZ + boundingBox.maxZ) / 2)
        val fromChunk = lastLocation!!.chunk!!
        val toChunk = location.chunk!!
        if (fromChunk.x != toChunk.x || fromChunk.z != toChunk.z) {
            fromChunk.removeEntity(this)
            toChunk.addEntity(this)
        }
        checkGroundState(movX, movY, movZ, velocity.getX(), velocity.getY(), velocity.getZ())
        updateFallState(movY)
        if (movX != velocity.getX()) {
            velocity.setX(0f)
        }
        if (movY != velocity.getY()) {
            velocity.setY(0f)
        }
        if (movZ != velocity.getZ()) {
            velocity.setZ(0f)
        }
    }

    protected fun updateFallState(movY: Float) {
        if (isOnGround) {
            if (this.fallDistance > 0) {
                fall()
            }
            this.fallDistance = 0f
        } else if (movY < 0) {
            this.fallDistance -= movY.toInt()
        }
    }

    protected fun checkGroundState(movX: Float, movY: Float, movZ: Float, dx: Float, dy: Float, dz: Float) {
        isCollidedVertically = movY != dy
        isCollidedHorizontally = movX != dx || movZ != dz
        isCollided = isCollidedHorizontally || isCollidedVertically
        isOnGround = movY != dy && movY < 0
    }

    protected fun updateMovement() {
        val diffPosition =
            (location.getX() - lastLocation!!.getX()) * (location.getX() - lastLocation!!.getX()) + (location.getY() - lastLocation!!.getY()) * (location.getY() - lastLocation!!.getY()) + (location.getZ() - lastLocation!!.getZ()) * (location.getZ() - lastLocation!!.getZ())
        val diffRotation =
            (location.yaw - lastLocation!!.yaw) * (location.yaw - lastLocation!!.yaw) + (location.pitch - lastLocation!!.pitch) * (location.pitch - lastLocation!!.pitch)
        val diffMotion =
            (velocity.getX() - lastVector.getX()) * (velocity.getX() - lastVector.getX()) + (velocity.getY() - lastVector.getY()) * (velocity.getY() - lastVector.getY()) + (velocity.getZ() - lastVector.getZ()) * (velocity.getZ() - lastVector.getZ())
        if (diffPosition > 0.0001 || diffRotation > 1.0) {
            lastLocation!!.setX(location.getX())
            lastLocation!!.setY(location.getY())
            lastLocation!!.setZ(location.getZ())
            lastLocation!!.yaw = location.yaw
            lastLocation!!.pitch = location.pitch
            sendEntityMovePacket(
                Location(
                    location.world,
                    location.getX(),
                    location.getY() + 0,
                    location.getZ(),
                    location.yaw,
                    location.pitch,
                    location.dimension,
                ),
                isOnGround,
            )
        }
        if (diffMotion > 0.0025 || diffMotion > 0.0001 && getVelocity().squaredLength() <= 0.0001) {
            lastVector.setX(velocity.getX())
            lastVector.setY(velocity.getY())
            lastVector.setZ(velocity.getZ())
            this.setVelocity(velocity, true)
        }
    }

    private fun sendEntityMovePacket(location: Location, onGround: Boolean) {
        val moveEntityAbsolutePacket = MoveEntityAbsolutePacket()
        moveEntityAbsolutePacket.runtimeEntityId = entityId
        moveEntityAbsolutePacket.isTeleported = false
        moveEntityAbsolutePacket.isOnGround = onGround
        moveEntityAbsolutePacket.position = location.toVector3f()
        moveEntityAbsolutePacket.rotation = Vector3f.from(location.pitch, location.yaw, location.yaw)
        Server.instance.broadcastPacket(moveEntityAbsolutePacket)
    }

    open fun onCollideWithPlayer(player: Player) {}
    fun getySize(): Float {
        return ySize
    }
}
