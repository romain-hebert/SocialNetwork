package model;

public class Utilisateur extends Sommet {
    
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
    
    @Override
    public String toString() {
        String str = "(U)" + getFirstName() + " " + getName()
                + ", " + age + "ans";
        if (!getSuccessors().isEmpty()) {
            str += ":";
            for (Sommet s : getSuccessors()) {
                str += " " + s.getName();
            }
        }
        return str;
    }
}
