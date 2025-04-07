package fr.ubx.poo.ubgarden.game.go;

import fr.ubx.poo.ubgarden.game.go.decor.*;

public interface WalkVisitor {

    /**
     * Determines whether the visitor can walk on the given {@link Decor}.
     *
     * @param decor the decor to evaluate
     * @return true if the visitor can walk on the decor, false by default
     */

    //We can implement it here directly because the visitor can never walk on this objects
    default boolean canWalkOn(Decor decor) {
        return !(decor instanceof Tree) && !(decor instanceof NestWasp) && !(decor instanceof NestHornet) && !(decor instanceof Flowers);
    }
    // TODO
}