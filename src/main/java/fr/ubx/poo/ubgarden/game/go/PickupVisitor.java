package fr.ubx.poo.ubgarden.game.go;

import fr.ubx.poo.ubgarden.game.go.bonus.EnergyBoost;
import fr.ubx.poo.ubgarden.game.go.bonus.Bonus;

public interface PickupVisitor {
    /**
     * Called when visiting and picking up an EnergyBoost or a poisoned apple.
     *
     * @param bonus the apple to be picked up
     */
    default void pickUp(Bonus bonus) {
    }

}
