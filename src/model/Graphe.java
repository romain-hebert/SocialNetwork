package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

public class Graphe {
    
    private Map<Sommet, Double> sommets;
    
    public Graphe() {
        sommets = new HashMap<Sommet, Double>();
    }
    
    // REQUÊTES
    
    // Le nombre de sommets.
    public int getNodeNb() {
        return sommets.size();
    }
    
    public int getArcNb() {
        int res = 0;
        for (Sommet s : sommets.keySet()) {
            res += s.getSuccessors().size();
        }
        return res;
    }
    
    public Set<Sommet> getNodes() {
        return sommets.keySet();
    }
    
    public Set<Sommet> getNodesSortedByName() {
        NavigableSet<Sommet> n = new TreeSet<Sommet>(new Comparator<Sommet>() {
            @Override
            public int compare(Sommet s1, Sommet s2) {
                return s1.getName().compareTo(s2.getName());
            }
        });
        
        n.addAll(sommets.keySet());
        
        return n;
    }
    
    public Set<Sommet> getNodesSortedByDegree() {
        NavigableSet<Sommet> d = new TreeSet<Sommet>(new Comparator<Sommet>() {
            @Override
            public int compare(Sommet s1, Sommet s2) {
                return s1.getSuccessors().size() - s2.getSuccessors().size();
            }
        });
        
        d.addAll(sommets.keySet());
        
        return d;
    }
    
    public Set<List<Sommet>> getArcs() {
        Set<List<Sommet>> arcs = new HashSet<List<Sommet>>();
        
        for (Sommet s : sommets.keySet()) {
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
        for (Sommet s : sommets.keySet()) {
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
        for (Sommet s : sommets.keySet()) {
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
        for (Sommet s : sommets.keySet()) {
            if (s instanceof Utilisateur) {
                sum += ((Utilisateur) s).getAge();
                cmpt++;
            }
        }
        return sum / cmpt;
    }
    
    public Set<Utilisateur> getAllAdmins() {
        Set<Utilisateur> adm = new HashSet<Utilisateur>();
        for (Sommet s : sommets.keySet()) {
            if (s instanceof Page) {
                adm.addAll(((Page) s).getAdmins());
            }
        }
        return adm;
    }
    
    // COMMANDES
    
    public void addNode(Sommet s) {
        sommets.put(s, 1.);
    }
    
    public void removeNode(Sommet s) {
        sommets.remove(s);
    }
    
    public void addArc(Sommet s1, Sommet s2) {
        if (sommets.keySet().contains(s1) && sommets.keySet().contains(s2) 
                && s1 instanceof Utilisateur) {
            ((Utilisateur) s1).addSuccessor(s2);
        }
    }
    
    public void removeArc(Sommet s1, Sommet s2) {
        if (sommets.keySet().contains(s1) && sommets.keySet().contains(s2) 
                && s1.getSuccessors().contains(s2)) {
            // s1 a s2 comme successeur, il est donc un Utilisateur.
            ((Utilisateur) s1).removeSuccessor(s2);
        }
    }
    
    public void save() {
        
    }
    
    public void load() {
        
    }
    
    public void computePageRank() {
        for (int i = 0; i <= 100; ++i) {
            for (Sommet s : sommets.keySet()) {
                sommets.put(s, pr(s));
            }
        }
    }
    
    private double pr(Sommet s) {
        double sum = 0;
        for (Sommet s2 : s.getSuccessors()) {
            sum += pr(s2) / s2.getSuccessors().size();
        }
        return 0.15 / getNodeNb() + 0.85 * sum;
    }
}
