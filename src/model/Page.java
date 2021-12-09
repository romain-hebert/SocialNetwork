package model;

import java.util.HashSet;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import util.Contract;

public class Page extends Sommet {
    
    private Set<Utilisateur> admins;
    
    public Page(String name, Utilisateur creator) {
        super(name);
        Contract.checkCondition(creator != null);
        admins = new HashSet<Utilisateur>();
        admins.add(creator);
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
    
    // COMMANDES
    
    @Override
    public boolean addSuccessor(Sommet s)
            throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }
    
    @Override
    public boolean removeSuccessor(Sommet s)
            throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }
    
    // OUTILS
    
    @Override
    public String toString() {
        String str = "(P)" + getName();
        if (!admins.isEmpty()) {
            str += ":";
            for (Utilisateur s : admins) {
                str += " " + s.getName();
            }
        }
        return str;
    }
}
