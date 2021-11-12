package model;

import java.util.Set;

public class Utilisateur extends Sommet {
    
    private int age;
    
    public Utilisateur(Set<Sommet> succ, int age) {
        super(succ);
        this.age = age;
    }
    
    public Utilisateur(int age) {
        super();
        this.age = age;
    }
    
    // COMMANDES
    
    void addSuccessor(Sommet s) {
        successors.add(s);
    }
    
    void removeSuccessor(Sommet s) {
        successors.remove(s);
    }
}
