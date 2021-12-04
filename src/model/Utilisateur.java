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
    
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Utilisateur) {
            Utilisateur that = (Utilisateur) other;
            result = super.equals(that)
                    && (this.firstName == that.firstName)
                    && (this.age == that.age);
        }
        return result;
    }
    
    @Override
    public boolean canEquals(Object other) {
        return (other instanceof Utilisateur);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + (firstName == null ? 0 : firstName.hashCode());
        result = prime * result + age;
        return result;
    }
}
