package model;

import java.util.Set;

import javax.naming.OperationNotSupportedException;

import util.Contract;

public class Page extends Sommet {
    
    private Set<Utilisateur> admins;
    
    public Page(String name, Set<Utilisateur> admins) {
        super(name);
        Contract.checkCondition(admins != null && !admins.isEmpty());
        this.admins = admins;
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
    
//    @Override
//    public boolean equals(Object other) {
//        boolean result = false;
//        if (other instanceof Page) {
//            Page that = (Page) other;
//            result = super.equals(that)
//                    && (this.admins.equals(that.admins));
//        }
//        return result;
//    }
//
//    @Override
//    public boolean canEquals(Object other) {
//        return (other instanceof Page);
//    }
//    
//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = super.hashCode();
//        for (Utilisateur a : admins) {
//            result = prime * result + a.getName().hashCode();
//        }
//        return result;
//    }
}
