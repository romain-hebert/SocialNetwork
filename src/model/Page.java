package model;

import java.util.HashSet;
import java.util.Set;

public class Page extends Sommet {
    
    private Set<Utilisateur> admins;
    
    public Page(Set<Sommet> succ, Set<Utilisateur> admins) {
        super(succ);
        this.admins = new HashSet<Utilisateur>(admins);
    }
    
    public Page(Utilisateur admin) {
        super();
    }
    
    // REQUÊTES
    
    public Set<Utilisateur> getAdmins() {
        return admins;
    }
}
