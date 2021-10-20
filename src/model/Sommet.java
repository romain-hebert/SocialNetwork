package model;

import java.util.ArrayList;
import java.util.List;

import util.Contract;

public abstract class Sommet {

    protected List<Sommet> successors;

    /**
     * 
     * @param succ
     * 
     * @pre succ != null
     */
    public Sommet(List<Sommet> succ) {
        Contract.checkCondition(succ != null);

        successors = succ;
    }

    public Sommet() {
        this(new ArrayList<Sommet>());
    }

    List<Sommet> getSuccessors() {
        return successors.subList(0, successors.size() - 1);
    }

}
