package model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utilisateur extends Sommet {
    
    public static final String USER_PATTERN = NAME_PATTERN + "," + NAME_PATTERN
            + "," + "\\d+:" + SUCCESSORS_PATTERN + "*$";
    
    private String firstName;
    private int age;
    
    public Utilisateur(String name, String firstName, int age) {
        super(name);
        this.firstName = firstName;
        this.age = age;
    }
    
    // REQUETES
    
    public String getFirstName() {
        return firstName;
    }
    
    public int getAge() {
        return age;
    }
    
    // OUTILS
    
    public void save(BufferedWriter bw) throws IOException {
        bw.write(getName() + "," + firstName + "," + age + ":");
        if (!getSuccessors().isEmpty()) {
            List<Sommet> l = new ArrayList<Sommet>(getSuccessors());
            bw.write(l.get(0).getName());
            for (int i = 1; i < l.size(); ++i) {
                bw.write(";" + l.get(i).getName());
            }
        }
    }
    
    @Override
    public String toString() {
        String str = "(U)" + getFirstName() + " " + getName()
                + " " + age + "ans";
        if (!getSuccessors().isEmpty()) {
            str += ":";
            for (Sommet s : getSuccessors()) {
                str += " " + s.getName();
            }
        }
        return str;
    }
}
