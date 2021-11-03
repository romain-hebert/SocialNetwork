package model;

import java.util.Set;

public class Utilisateur extends Sommet {
    
    public Utilisateur(Set<Sommet> succ) {
        super(succ);
    }
    
    public Utilisateur() {
        super();
    }
    
    // COMMANDES
    
    void addSuccessor(Sommet s) {
        successors.add(s);
    }
    
    void removeSuccessor(Sommet s) {
        successors.remove(s);
    }
}
