package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

public class Graphe {
    
    private Set<Sommet> sommets;
    
    public Graphe() {
        sommets = new HashSet<Sommet>();
    }
    
    // REQUÊTES
    
    // Le nombre de sommets.
    public int getNodeNb() {
        return sommets.size();
    }
    
    public int getArcNb() {
        int res = 0;
        for (Sommet s : sommets) {
            res += s.getSuccessors().size();
        }
        return res;
    }
    
    public Set<Sommet> getNodes() {
        return new HashSet<Sommet>(sommets);
    }
    
    public Set<Sommet> getNodesSortedByName() {
        NavigableSet<Sommet> n = new TreeSet<Sommet>(new Comparator<Sommet>() {
            @Override
            public int compare(Sommet s1, Sommet s2) {
                return s1.getName().compareTo(s2.getName());
            }
        });
        
        n.addAll(sommets);
        
        return n;
    }
    
    public Set<Sommet> getNodesSortedByDegree() {
        NavigableSet<Sommet> d = new TreeSet<Sommet>(new Comparator<Sommet>() {
            @Override
            public int compare(Sommet s1, Sommet s2) {
                return s1.getSuccessors().size() - s2.getSuccessors().size();
            }
        });
        
        d.addAll(sommets);
        
        return d;
    }
    
    public Set<List<Sommet>> getArcs() {
        Set<List<Sommet>> arcs = new HashSet<List<Sommet>>();
        
        for (Sommet s : sommets) {
            for (Sommet succ : s.getSuccessors()) {
                List<Sommet> l = new ArrayList<Sommet>(2);
                l.add(s);
                l.add(succ);
                arcs.add(l);
            }
        }
        
        return arcs;
    }
    
    public Sommet getNodeFromName(String name) {
        for (Sommet s : sommets) {
            if (s.getName() == name) {
                return s;
            }
        }
        
        return null;
    }
    
    public List<Integer> getNbUsersAndPages() {
        List<Integer> l = new ArrayList<Integer>(2);
        int ucmpt = 0;
        int pcmpt = 0;
        for (Sommet s : sommets) {
            if (s instanceof Utilisateur) {
                ucmpt++;
            } else if (s instanceof Page) {
                pcmpt++;
            }
        }
        l.add(0, ucmpt);
        l.add(1, pcmpt);
        return l;
    }
    
    public int meanUserAge() {
        int sum = 0;
        int cmpt = 0;
        for (Sommet s : sommets) {
            if (s instanceof Utilisateur) {
                sum += ((Utilisateur) s).getAge();
                cmpt++;
            }
        }
        return sum / cmpt;
    }
    
    public Set<Utilisateur> getAllAdmins() {
        Set<Utilisateur> adm = new HashSet<Utilisateur>();
        for (Sommet s : sommets) {
            if (s instanceof Page) {
                adm.addAll(((Page) s).getAdmins());
            }
        }
        return adm;
    }
    
    // COMMANDES
    
    public void addNode(Sommet s) {
        sommets.add(s);
    }
    
    public void removeNode(Sommet s) {
        sommets.remove(s);
    }
    
    public void addArc(Sommet s1, Sommet s2) {
        if (sommets.contains(s1) && sommets.contains(s2) 
                && s1 instanceof Utilisateur) {
            ((Utilisateur) s1).addSuccessor(s2);
        }
    }
    
    public void removeArc(Sommet s1, Sommet s2) {
        if (s1.getSuccessors().contains(s2)) {
            // s1 a s2 comme successeur, il est donc un Utilisateur.
            ((Utilisateur) s1).removeSuccessor(s2);
        }
    }
    
    public void save() {
        
    }
    
    public void load() {
        
    }
}
