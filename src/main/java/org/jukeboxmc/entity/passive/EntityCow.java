package org.jukeboxmc.entity.passive;

import org.jukeboxmc.entity.EntityLiving;
import org.jukeboxmc.entity.EntityType;
import org.jukeboxmc.entity.ai.goal.DoNothingGoal;
import org.jukeboxmc.entity.ai.goal.LookAtRandomGoal;
import org.jukeboxmc.entity.ai.goal.MoveRandomGoal;

/**
 * @author LucGamesYT, Kaooot
 * @version 1.0
 */
public class EntityCow extends EntityLiving {

    public EntityCow() {
        this.addAIGoals(
                new DoNothingGoal( this, 1, 2, 0.20F ),
                new MoveRandomGoal( this, 10, 0.70F ),
                new LookAtRandomGoal( this, 1, 3, 0.30F )
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