package model;

import java.util.HashSet;
import java.util.Set;

import util.Contract;

public class Page extends Sommet {
    
    private Set<Utilisateur> admins;
    
    public Page(String name) {
        super(name);
        admins = new HashSet<Utilisateur>();
    }
    
    // REQUÊTES
    
    public Set<Utilisateur> getAdmins() {
        return admins;
    }
    
    public void addAdmin(Utilisateur u) {
        Contract.checkCondition(u != null);
        admins.add(u);
    }
    
    public void removeAdmin(Utilisateur u) {
        Contract.checkCondition(u != null);
        admins.remove(u);
    }
}
