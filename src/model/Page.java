package model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import util.Contract;

public class Page extends Sommet {
    
    private Set<Utilisateur> admins;
    
    public static final String PAGE_PATTERN = NAME_PATTERN + ":"
            + SUCCESSORS_PATTERN + "$";
    
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
        Contract.checkCondition(u != null && admins.size() != 1);
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
    public void save(BufferedWriter bw) throws IOException {
        bw.write(getName() + ":");
        List<Sommet> l = new ArrayList<Sommet>(admins);
        bw.write(l.get(0).getName());
        for (int i = 1; i < l.size(); ++i) {
            bw.write(";" + l.get(i).getName());
        }
    }
    
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
