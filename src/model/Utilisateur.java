package model;

import java.util.List;

public class Utilisateur extends Sommet {
    
    public Utilisateur(List<Sommet> succ) {
        super(succ);
    }
    
    public Utilisateur() {
        super();
    }
    
}
