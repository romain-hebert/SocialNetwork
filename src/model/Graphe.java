package model;

import java.util.HashSet;
import java.util.Set;

import util.Contract;

public class Graphe {
    
    Set<Sommet> sommets;
    
    public Graphe(Set<Sommet> sommets) {
        Contract.checkCondition(sommets != null);
        
        this.sommets = sommets;
    }
    
    public Graphe() {
        this(new HashSet<Sommet>());
    }
    
}
