package model;

import java.util.HashSet;
import java.util.Set;

import util.Contract;

public abstract class Sommet {

    protected Set<Sommet> successors;
    
    private final String name;

    public Sommet(String name) {
        Contract.checkCondition(name != null);
        this.name = name;
    }

    // REQUÊTES
    
    public Set<Sommet> getSuccessors() {
        return new HashSet<Sommet>(successors);
    }
    
    public String getName() {
        return name;
    }
}
