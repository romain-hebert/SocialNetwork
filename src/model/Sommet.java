package model;

import java.util.HashSet;
import java.util.Set;

import util.Contract;

public abstract class Sommet {

    protected Set<Sommet> successors;
    
    private String name;

    public Sommet(Set<Sommet> succ) {
        Contract.checkCondition(succ != null);

        successors = succ;
    }

    public Sommet() {
        this(new HashSet<Sommet>());
    }

    // REQUÊTES
    
    public Set<Sommet> getSuccessors() {
        return new HashSet<Sommet>(successors);
    }
    
    public String getName() {
        return name;
    }
}
