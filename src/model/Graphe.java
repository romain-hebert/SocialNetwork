package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.Observable;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.OperationNotSupportedException;

public class Graphe extends Observable {
    
    private static final int PAGERANK_ITERATIONS = 100;
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
                Integer succCompar = ((Integer) s1.getSuccessors().size())
                        .compareTo(s2.getSuccessors().size());
                if (succCompar != 0) {
                    return succCompar;
                }
                return s1.getName().compareTo(s2.getName());
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
        return (int) Math.round((double) sum / cmpt);
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
    
    public boolean addNode(Sommet s) {
        if (!sommets.containsKey(s)) {
            sommets.put(s, 1.);
            setChanged();
            notifyObservers();
            return true;
        }
        return false;
    }
    
    public boolean removeNode(Sommet s) {
        if (sommets.remove(s) != null) {
            setChanged();
            notifyObservers();
            return true;
        }
        return false;
    }
    
    public boolean addArc(Sommet s1, Sommet s2)
            throws OperationNotSupportedException {
        if (sommets.keySet().contains(s1) && sommets.keySet().contains(s2)) {
            if (s1.addSuccessor(s2)) {
                setChanged();
                notifyObservers();
                return true;
            }
        }
        return false;
    }
    
    public boolean removeArc(Sommet s1, Sommet s2)
            throws OperationNotSupportedException {
        if (sommets.keySet().contains(s1) && sommets.keySet().contains(s2)) {
            // s1 a s2 comme successeur, il est donc un Utilisateur.
            if (s1.removeSuccessor(s2)) {
                setChanged();
                notifyObservers();
                return true;
            }
        }
        return false;
    }
    
    public void save() {
        
    }
    
    public void load() {
        

        setChanged();
        notifyObservers();
    }
    
    public void computePageRank() {
        int i = 0;
        for (Sommet s : new HashSet<Sommet>(sommets.keySet())) {
            sommets.put(s, 1.);
        }
        while (i <= PAGERANK_ITERATIONS) {
            for (Sommet s : new HashSet<Sommet>(sommets.keySet())) {
                sommets.put(s, pr(s));
                i++;
            }
        }
        setChanged();
        notifyObservers();
    }
    
    private double pr(Sommet s) {
        double sum = 0;
        for (Sommet s2 : enteringNodes(s)) {
            sum += (sommets.get(s2) != 1.) ? sommets.get(s2) : pr(s2)
                    / s2.getSuccessors().size();
        }
        return 0.15 / getNodeNb() + 0.85 * sum;
    }
    
    private Set<Sommet> enteringNodes(Sommet s) {
        Set<Sommet> en = new HashSet<Sommet>();
        for (Sommet s2 : sommets.keySet()) {
            if (s2.getSuccessors().contains(s)) {
                en.add(s2);
            }
        }
        return en;
    }
    
    @Override
    public String toString() {
        String str = "";
        for (Entry<Sommet, Double> s : sommets.entrySet()) {
            str += (s.getValue() != 1. ? "PR: " + s.getValue() + "; " : "")
                    + s.getKey().toString()
                    + "\n";
        }
        return str;
    }
}
