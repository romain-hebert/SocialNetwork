package model;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SocialNetwork {
    
    JFrame mainFrame;
    Graphe model;
    
    public SocialNetwork() {
        createModel();
        createView();
        placeComponents();
        createController();
    }
    

    private void createModel() {
        model = new Graphe();
    }

    private void createView() {
        mainFrame = new JFrame("Social Network");
    }
    
    private void placeComponents() {
        // TODO Auto-generated method stub
        
    }
    
    private void createController() {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        ((Observable) model).addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                // TODO Auto-generated method stub
                
            }
        });;
    }
    private void display() {
        refresh();
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
    
    private void refresh() {
        
    }
    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                new SocialNetwork().display();
            }
        });
    }

}
