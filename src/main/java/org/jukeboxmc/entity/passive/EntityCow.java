package org.jukeboxmc.entity.passive;

import org.jukeboxmc.entity.EntityLiving;
import org.jukeboxmc.entity.EntityType;
import org.jukeboxmc.entity.ai.goal.DoNothingGoal;
import org.jukeboxmc.entity.ai.goal.LookAtRandomGoal;
import org.jukeboxmc.entity.ai.goal.MoveRandomGoal;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class EntityCow extends EntityLiving {

    public EntityCow() {
        this.addAIGoals(
                new DoNothingGoal( this, 3, 5, 0.40F ),
                new MoveRandomGoal( this, 6, 0.60F ),
                new LookAtRandomGoal( this, 1, 3, 0.10F )
        );
    }

    @Override
    public String getName() {
        return "Cow";
    }

    @Override
    public float getWidth() {
        return 0.9f;
    }

    @Override
    public float getHeight() {
        return 1.3f;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.COW;
    }

}
