package org.jukeboxmc.entity.passive;

import com.nukkitx.protocol.bedrock.data.entity.EntityData;
import com.nukkitx.protocol.bedrock.data.entity.EntityFlag;
import org.jukeboxmc.entity.EntityLiving;
import org.jukeboxmc.entity.EntityType;
import org.jukeboxmc.entity.ai.goal.DoNothingGoal;
import org.jukeboxmc.entity.ai.goal.LookAtRandomGoal;
import org.jukeboxmc.entity.ai.goal.MoveRandomGoal;

/**
 * @author Kaooot
 * @version 1.0
 */
public class EntityChicken extends EntityLiving {

    public EntityChicken() {
        this.metadata.setByte( EntityData.IS_BUOYANT, (byte) 0 );

        this.addAIGoals(
                new DoNothingGoal( this, 1, 2, 0.10F ),
                new MoveRandomGoal( this, 10, 0.70F ),
                new LookAtRandomGoal( this, 1, 3, 0.30F )
        );
    }

    @Override
    public String getName() {
        return "Chicken";
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 0.8f;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.CHICKEN;
    }
}