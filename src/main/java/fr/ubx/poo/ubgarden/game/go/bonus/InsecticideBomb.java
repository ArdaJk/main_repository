package fr.ubx.poo.ubgarden.game.go.bonus;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.decor.Decor;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public class InsecticideBomb extends Bonus {
    private boolean used = false;
    public InsecticideBomb(Position position, Decor decor) {
        super(position, decor);
    }

    @Override
    public void pickUpBy(Gardener gardener) {
        gardener.pickUp(this);
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    @Override
    public void update(long now) {
        if (used) {
            this.remove();
        }
    }

    public boolean isUsed() {
        return used;
    }
}
