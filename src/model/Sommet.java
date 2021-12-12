package model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import util.Contract;

public abstract class Sommet {

    private Set<Sommet> successors;
    private final String name;
    
    public static final String NAME_PATTERN = "[^$:;]+";
    public static final String SUCCESSORS_PATTERN = NAME_PATTERN + "(;" 
            + NAME_PATTERN + ")*";

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
    
    public abstract void save(BufferedWriter bw) throws IOException;
    
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        
        if (other instanceof Sommet) {
            Sommet that = (Sommet) other;
            result = this.name.equals(that.name);
        }
        
        return result;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 17;
        result = prime * result + (name == null ? 0 : name.hashCode());
        return result;
    }
}
