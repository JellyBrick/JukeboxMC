package org.jukeboxmc.entity.ai;

import org.jukeboxmc.entity.Entity;
import org.jukeboxmc.entity.ai.goal.Goal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public abstract class EntityAI extends Entity {

    private final List<Goal> goals = new ArrayList<>();

    private Goal currentGoal;

    public void addAIGoals( Goal... goals ) {
        this.goals.addAll( Arrays.asList( goals ) );
    }

    public void clearGoals() {
        this.goals.clear();
    }

    public void removeGoal( Goal goal ) {
        this.goals.remove( goal );
    }

    @Override
    public void update( long currentTick ) {
        super.update( currentTick );

        if ( this.currentGoal == null ) {
            this.currentGoal = this.nextGoal();

            if ( this.currentGoal == null ) {
                return;
            }

            this.currentGoal.start();
        }

        if ( this.currentGoal != null ) {
            if ( this.currentGoal.hasEnded() ) {
                this.currentGoal.end();
                this.currentGoal = null;
                return;
            }

            this.currentGoal.tick();
        }
    }

    protected Goal nextGoal() {
        for ( Goal goal : this.goals ) {
            if ( goal.canStart() ) {
                return goal;
            }
        }

        return null;
    }

}
