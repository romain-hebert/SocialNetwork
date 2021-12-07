package tests;

import java.util.Arrays;
import java.util.HashSet;

import javax.naming.OperationNotSupportedException;

import model.Graphe;
import model.Page;
import model.Utilisateur;

public class Tests1 {

    public static void main(String[] args)
            throws OperationNotSupportedException {
        Graphe g = new Graphe();
        Utilisateur hebert = new Utilisateur("Hebert", "Romain", 23);
        Page trololo = new Page("Trololo",
                new HashSet<Utilisateur>(
                        Arrays.asList(new Utilisateur[]{hebert})));
        Utilisateur marybrasse = new Utilisateur("Marybrasse", "Thomas", 22);
        Utilisateur selmi = new Utilisateur("Selmi", "Carla", 60);
//        marybrasse.addSuccessor(hebert);
//        marybrasse.addSuccessor(trololo);
        g.addNode(hebert);
        g.addNode(trololo);
        g.addNode(marybrasse);
        g.addNode(selmi);
        
        System.out.println("----------------------------------");
        System.out.println(g.getNodes());
        
        System.out.println(g.addArc(hebert, trololo));
//        System.out.println(g.addArc(trololo, hebert));
        System.out.println(g.addArc(marybrasse, selmi));
        System.out.println("----------------------------------");
        System.out.println(g.getNodes());
        System.out.println(g.addArc(marybrasse, trololo));
        System.out.println(g.addArc(selmi, trololo));
        System.out.println(g.addArc(hebert, selmi));
        System.out.println("----------------------------------");
        System.out.println(g.toString());
        System.out.println(g.getNbUsersAndPages().toString());
        System.out.println(g.meanUserAge());
        System.out.println(g.getNodesSortedByDegree());
        System.out.println(g.getNodesSortedByName());
        System.out.println(g.computePageRank());
        System.out.println(hebert.getSuccessors().size());
    }

}
