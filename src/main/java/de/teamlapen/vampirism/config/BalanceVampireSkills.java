package de.teamlapen.vampirism.config;

import de.teamlapen.lib.lib.config.BalanceValues;
import de.teamlapen.lib.lib.config.DefaultBoolean;
import de.teamlapen.lib.lib.config.DefaultDouble;
import de.teamlapen.lib.lib.config.DefaultInt;
import de.teamlapen.vampirism.VampirismMod;

import java.io.File;

/**
 * Holds balance values related to vampire skills
 */
public class BalanceVampireSkills extends BalanceValues {


    @DefaultDouble(value = -0.3, comment = "The sundamage is multiplied with (value+1)")
    public double SUNDAMAGE_REDUCTION1;


    @DefaultDouble(value = -0.4, comment = "The blood exhaustion is multiplied with (value+1)")
    public double BLOOD_THIRST_REDUCTION1;

    @DefaultDouble(value = 1, comment = "The bite damage is multiplied with (value+1)")
    public double BITE_DAMAGE_MULT;

    @DefaultDouble(value = 0.25, comment = "The maximal health percentage for a sword finisher kill")
    public double SWORD_FINISHER_MAX_HEALTH_PERC;

    @DefaultInt(value = 5, name = "poisonous_bite_duration", minValue = 0, maxValue = 100, comment = "If the players bite is poisonous this specifies the duration (in sec)")
    public int POISONOUS_BITE_DURATION;

    @DefaultInt(value = 1, minValue = -1, comment = "Compare value to potion effect amplifier (-1 is normal)")
    public int JUMP_BOOST;

    @DefaultDouble(value = 0.15, comment = "The max movementspeed is multiplied with (value+1)")
    public double SPEED_BOOST;

    @DefaultInt(value = 1600, minValue = 5, comment = "How far the blood vision reaches (Squared distance, so for 20 blocks enter 400)")
    public int BLOOD_VISION_DISTANCE_SQUARED;

    @DefaultBoolean(value = false, comment = "Disables the effect of 'Avoided by creepers' if it causes problems. Still can be unlocked though.")
    public boolean DISABLE_AVOIDED_BY_CREEPERS;

    public BalanceVampireSkills(File directory) {
        super("vampire_player_skills", directory);
    }

    @Override
    protected boolean shouldUseAlternate() {
        return VampirismMod.isRealism();
    }
}
