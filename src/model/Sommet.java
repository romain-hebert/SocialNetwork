package model;

import java.util.HashSet;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import util.Contract;

public abstract class Sommet {

    private Set<Sommet> successors;
    
    private final String name;

    public Sommet(String name) {
        Contract.checkCondition(name != null);
        successors = new HashSet<Sommet>();
        this.name = name;
    }

    // REQUÊTES
    
    public Set<Sommet> getSuccessors() {
        return new HashSet<Sommet>(successors);
    }
    
    public String getName() {
        return name;
    }
    
    // COMMANDES
    
    public boolean addSuccessor(Sommet s)
            throws OperationNotSupportedException {
        return successors.add(s);
    }
    
    public boolean removeSuccessor(Sommet s)
            throws OperationNotSupportedException {
        return successors.remove(s);
    }
    
    // OUTILS
    
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Sommet) {
            Sommet that = (Sommet) other;
            result = that.canEquals(this) && (this.name == that.name)
                    && (this.successors.equals(that.successors));
        }
        return result;
    }

    public boolean canEquals(Object other) {
        return (other instanceof Sommet);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 17;
        result = prime * result
                + (successors == null ? 0 : successors.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        return result;
    }
}
