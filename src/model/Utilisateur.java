package model;

public class Utilisateur extends Sommet {
    
    private String firstName;
    private int age;
    
    public Utilisateur(String name, String firstName,int age) {
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
    
    // COMMANDES
    
    public void addSuccessor(Sommet s) {
        successors.add(s);
    }
    
    public void removeSuccessor(Sommet s) {
        successors.remove(s);
    }
}
