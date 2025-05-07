package fr.ubx.poo.ubgarden.game.go.decor;

import fr.ubx.poo.ubgarden.game.Position;
import fr.ubx.poo.ubgarden.game.go.personage.Gardener;

public class Door extends Decor {
    private boolean closed = true;
    public Door(Position position) {
        super(position);
    }
    private boolean prev = false;

    public boolean isPrev() {
        return prev;
    }

    public void setPrev(boolean prev) {
        this.prev = prev;
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
