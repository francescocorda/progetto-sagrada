package it.polimi.ingsw.Model.Cards.Patterns;

import java.io.Serializable;

public class Cell implements Serializable {
    private Restriction restriction;
    private boolean exceptionRestriction;
    private boolean exceptionPosition;

    public Cell() {
        this.restriction = Restriction.ANSI_WHITE;
        this.exceptionRestriction = false;
        this.exceptionPosition = false;
    }

    public Cell(Cell cell) {
        this.restriction = cell.getRestriction();
        this.exceptionRestriction = cell.getExceptionRestriction();
        this.exceptionPosition = cell.getExceptionPosition();
    }

    public void setRestriction(Restriction restriction) {
        this.restriction = restriction;
    }
    public Restriction getRestriction() {
        return restriction;
    }

    public void setExceptionRestriction(boolean exception) {
        this.exceptionRestriction = exception;
    }
    public boolean getExceptionRestriction() {
        return exceptionRestriction;
    }

    public void setExceptionPosition(boolean exception) {
        this.exceptionPosition = exception;
    }
    public boolean getExceptionPosition() {
        return exceptionPosition;
    }

    public void dump() {
        System.out.println(toString());
    }

    public String toString() {
        String escape = getRestriction().escape();
        if (escape.compareTo("\u2680") + 1> 0) {
            return Restriction.ANSI_WHITE.escape() + "[" + escape + "]" + Restriction.RESET;
        } else {
            return escape + "[" + "\uD83E\uDD76" + "]" + Restriction.RESET;
        }
    }
}