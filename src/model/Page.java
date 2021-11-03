package model;

import java.util.Set;

public class Page extends Sommet {
    
    private Utilisateur admin;
    
    public Page(Set<Sommet> succ, Utilisateur admin) {
        super(succ);
        this.admin = admin;
    }
    public Page(Utilisateur admin) {
        super();
    }
    
    // REQUÊTES
    
    public Utilisateur getAdmin() {
        return admin;
    }
}
