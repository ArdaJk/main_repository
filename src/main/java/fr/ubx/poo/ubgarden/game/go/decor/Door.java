package fr.ubx.poo.ubgarden.game.go.decor;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public class Door extends Decor {
    private boolean closed = true;
    public Door(Position position) {
        super(position);
    }
    private boolean isPassed = false;

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean passed) {
        isPassed = passed;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    @Override
    public boolean walkableBy(Gardener gardener) {
        if (isClosed()) {
            return false;
        } else {
            return true;
        }
    }
}
