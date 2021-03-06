package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Observable;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.OperationNotSupportedException;

import util.Contract;

public class Graphe extends Observable {
    
    private static final int PAGERANK_ITERATIONS = 100;
    private static final int SHORTPATH_INIT = 10000000;

    //TODO
    private static final String LINE_PATTERN =
            Utilisateur.USER_PATTERN + "|" + Page.PAGE_PATTERN + "$";
    
    private Set<Sommet> sommets;
    private Map<Sommet, Double> pageRank;
    
    public Graphe() {
        sommets = new HashSet<Sommet>();
    }
    
    // REQUÊTES
    
    // Le nombre de sommets.
    public int getNodeNb() {
        return sommets.size();
    }
    
    // Le nombre d'arcs
    public int getArcNb() {
        int res = 0;
        for (Sommet s : sommets) {
            res += s.getSuccessors().size();
        }
        return res;
    }
    
    // Les sommets du graphe
    public Set<Sommet> getNodes() {
        return sommets;
    }
    
    // Les sommets du graphe triés pas ordre alphabetique
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
    
    // Les sommets du graphe triés par ordre de croissant de degrés sortant
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
        
        d.addAll(sommets);
        return d;
    }
    
    // Récupérer tous les arcs du graphe
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
    
    // Récupérer un sommet à partir de son nom
    public Sommet getNodeFromName(String name) {
        for (Sommet s : sommets) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        
        return null;
    }
    
    // Le nombre d'utilisateurs et de pages
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
    
    // L'age moyen des utilisateurs
    public int meanUserAge() {
        int sum = 0;
        int cmpt = 0;
        for (Sommet s : sommets) {
            if (s instanceof Utilisateur) {
                sum += ((Utilisateur) s).getAge();
                cmpt++;
            }
        }
        return (int) Math.round((double) sum / cmpt);
    }
    
    // Récupérer tous les administrateurs de pages
    public Set<Utilisateur> getAllAdmins() {
        Set<Utilisateur> adm = new HashSet<Utilisateur>();
        for (Sommet s : sommets) {
            if (s instanceof Page) {
                adm.addAll(((Page) s).getAdmins());
            }
        }
        return adm;
    }
    
    // Calcul du page rank
    public Map<Sommet, Double> computePageRank() {
        pageRank = new HashMap<Sommet, Double>();
        for (Sommet s : sommets) {
            pageRank.put(s, 1.);
        }
        int i = 0;
        while (i <= PAGERANK_ITERATIONS) {
            for (Sommet s : sommets) {
                pageRank.put(s, pr(s));
            }
            i++;
        }
        return pageRank;
    }
    
    private double pr(Sommet s) {
        double sum = 0;
        for (Sommet s2 : enteringNodes(s)) {
            sum += pageRank.get(s2) / s2.getSuccessors().size();
        }
        return 0.15 / getNodeNb() + 0.85 * sum;
    }
    
    private Set<Sommet> enteringNodes(Sommet s) {
        Set<Sommet> en = new HashSet<Sommet>();
        for (Sommet s2 : sommets) {
            if (s2.getSuccessors().contains(s)) {
                en.add(s2);
            }
        }
        return en;
    }
    
    // Calcul du plus court chemin depuis un sommet source vers tous les autres
    //      sommets du graphe
    public Map<Sommet, Integer> shortestPath(Sommet s) {
        Map<Sommet, Integer> spMap = new HashMap<Sommet, Integer>();
        for (Sommet s1 : sommets) {
            spMap.put(s1, SHORTPATH_INIT);
        }
        spMap.put(s, 0);
        
        Set<Sommet> p = new HashSet<Sommet>(sommets);
        while (!p.isEmpty()) {
            Sommet u = null;
            for (Sommet u1 : p) {
                if (u == null) {
                    u = u1;
                } else if (spMap.get(u1) < spMap.get(u)) {
                    u = u1;
                }
            }
            p.remove(u);
            for (Sommet v : u.getSuccessors()) {
                Integer alt = spMap.get(u) + 1;
                if (alt <= spMap.get(v)) {
                    spMap.put(v, alt);
                }
            }
        }
        
        return spMap;
    }
    
    // COMMANDES
    
    // Ajouter un sommet au graphe
    public boolean addNode(Sommet s) {
        if (sommets.add(s)) {
            setChanged();
            notifyObservers();
            return true;
        }
        return false;
    }
    
    // Retirer un sommet du graphe
    public boolean removeNode(Sommet s) {
        if (sommets.remove(s)) {
            for (Sommet s1 : enteringNodes(s)) {
                try {
                    s1.removeSuccessor(s);
                } catch (OperationNotSupportedException e) {
                  
                }
            }
            
            setChanged();
            notifyObservers();
            return true;
        }
        return false;
    }
    
    // Ajouter un arc entre deux sommets
    public boolean addArc(Sommet s1, Sommet s2)
            throws OperationNotSupportedException {
        
        Contract.checkCondition(sommets.contains(s1) && sommets.contains(s2));
        
        if (s1.addSuccessor(s2)) {
            setChanged();
            notifyObservers();
            return true;
        }
        return false;
    }
    
    // Retirer un arc entre deux sommets
    public boolean removeArc(Sommet s1, Sommet s2)
            throws OperationNotSupportedException {
        
        Contract.checkCondition(sommets.contains(s1) && sommets.contains(s2));
        
        if (s1.removeSuccessor(s2)) {
            setChanged();
            notifyObservers();
            return true;
        }
        return false;
    }
    
    // Sauvegarder le graphe dans un fichier
    public void save(File f) throws IOException {
        Contract.checkCondition(f != null);
        
        Writer fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);
        
        for (Sommet s : sommets) {
            s.save(bw);
            bw.newLine();
        }
        
        if (bw != null) {
            bw.close();
        }
        if (fw != null) {
            fw.close();
        }
    }
    
    // Charger un graphe depuis un fichier
    // NON FONCTIONNELLE
    public void load(File f) throws IOException, Exception {
        Contract.checkCondition(f != null);
        
        Reader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        
        String line = br.readLine();
        while (line != null) {
            Pattern pu = Pattern.compile(Utilisateur.USER_PATTERN);
            Pattern pp = Pattern.compile(Page.PAGE_PATTERN);
            if (pu.matcher(line).matches()) {
                String[] lr = line.split(",:;");
                Utilisateur u = new Utilisateur(lr[0], lr[1],
                        Integer.valueOf(lr[2]));
                System.out.println("ouille");
                addNode(u);
            } else if (pp.matcher(line).matches()) {
                String[] lr = line.split(",:;");
                Utilisateur u = (Utilisateur) getNodeFromName(lr[1]);
                Page p = new Page(lr[0], u);
                addNode(p);
            } else {
                fr.close();
                br.close();
                System.out.println("aie");
                throw new Exception();
            }
            
            
        }
        
        if (br != null) {
            br.close();
        }
        if (fr != null) {
            fr.close();
        }
        setChanged();
        notifyObservers();
    }

    @Override
    public String toString() {
        String str = "";
        for (Sommet s : sommets) {
            str += s.toString() + "\n";
        }
        return str;
    }
}
