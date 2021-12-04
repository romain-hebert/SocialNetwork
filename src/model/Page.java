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
    
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Page) {
            Page that = (Page) other;
            result = super.equals(that)
                    && (this.admins.equals(that.admins));
        }
        return result;
    }

    @Override
    public boolean canEquals(Object other) {
        return (other instanceof Page);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + (admins == null ? 0 : admins.hashCode());
        return result;
    }
}
