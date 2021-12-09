

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.naming.OperationNotSupportedException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import model.Graphe;
import model.Page;
import model.Sommet;
import model.Utilisateur;

public class SocialNetwork {
    
    private JFrame mainFrame;
    private Graphe model;
    private JTextArea graphTextArea;
    private JTextArea requestTextArea;
    private JButton addNodeButton;
    private JButton removeNodeButton;
    private JButton addArcButton;
    private JButton removeArcButton;
    private JLabel stateLabel;
    
    private static final int TEXTAREA_ROWS = 15;
    private static final int TEXTAREA_COL = 40;
    
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
        graphTextArea = new JTextArea(TEXTAREA_ROWS, TEXTAREA_COL);
        graphTextArea.setEditable(false);
        requestTextArea = new JTextArea(TEXTAREA_ROWS, TEXTAREA_COL);
        requestTextArea.setEditable(false);
        addNodeButton = new JButton("Ajouter");
        removeNodeButton = new JButton("Retirer");
        addArcButton = new JButton("Ajouter");
        removeArcButton = new JButton("Retirer");
        stateLabel = new JLabel("Graphe vide");
    }
    
    private void placeComponents() {
        JPanel p = new JPanel(new GridLayout(0, 1)); {
            JPanel q = new JPanel(); {
                JPanel r = new JPanel(new BorderLayout()); {
                    r.add(new JScrollPane(graphTextArea), BorderLayout.CENTER);
                    r.add(stateLabel, BorderLayout.SOUTH);
                }
                q.add(r);
                q.setBorder(BorderFactory.createTitledBorder(
                        "État du graphe"));
            }
            p.add(q);
            q = new JPanel(); {
                q.add(new JScrollPane(requestTextArea));
                q.setBorder(BorderFactory.createTitledBorder(
                        "Resultat de la requête"));
            }
            p.add(q);
            p.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }
        mainFrame.add(p, BorderLayout.CENTER);
        
        p = new JPanel(); {
            JPanel q = new JPanel(new GridLayout(0, 1)); {
                // Panel Sommets
                JPanel r = new JPanel(); {
                    JPanel s = new JPanel(new GridLayout(0, 2)); {
                        s.add(addNodeButton);
                        s.add(removeNodeButton);
                    }
                    r.setBorder(BorderFactory.createTitledBorder("Sommets"));
                    r.add(s);
                }
                q.add(r);
                // Panel Arcs
                r = new JPanel(); {
                    JPanel s = new JPanel(new GridLayout(0, 2)); {
                        s.add(addArcButton);
                        s.add(removeArcButton);
                    }
                    r.setBorder(BorderFactory.createTitledBorder("Arcs"));
                    r.add(s);
                }
                q.add(r);
                r = new JPanel(); {
                    
                }
                q.add(r);
            }
            p.add(q);
        }
        mainFrame.add(p, BorderLayout.WEST);
    }
    
    private void createController() {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        ((Observable) model).addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                refresh();
            }
        });
        
        addNodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String request = "Ajouter un sommet";
                // Demander le type du nouveau sommet
                String type = (String) JOptionPane.showInputDialog(
                        null,
                        "Selectionnez le type de sommet.",
                        request,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[] {"Utilisateur", "Page"},
                        null);
                if (type == null) {
                    return;
                }
                
                if (type.equals("Utilisateur")) {
                    String name = textFieldDialog(request,
                            "Nom de l'utilisateur");
                    if (name == null) {
                        return;
                    }
                    
                    String firstName = textFieldDialog(request,
                            "Prénom de l'utilisateur");
                    if (firstName == null) {
                        return;
                    }
                    
                    String age = textFieldDialog(request,
                            "Age de l'utilisateur");
                    if (age == null) {
                        return;
                    }
                    
                    boolean res = false;
                    try {
                        res = model.addNode(new Utilisateur(name,
                                firstName, Integer.parseInt(age)));
                    } catch (NumberFormatException ex) {
                        // Rien
                    }
                    requete(request, res);
                    
                } else if (type.equals("Page")) {
                    String name = textFieldDialog(request,
                            "Nom de la page");
                    if (name == null) {
                        return;
                    }
                    
                    String creator = textFieldDialog(request,
                            "Nom du créateur");
                    if (creator == null) {
                        return;
                    }

                    boolean res = model.addNode(new Page(name,
                            (Utilisateur) model.getNodeFromName(creator)));
                    requete(request, res);
                }
            }
        });
        
        removeNodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String request = "Retirer un sommet";
                String name = textFieldDialog(request, "Nom du sommet");
                if (name == null) {
                    return;
                }
                
                boolean res = model.removeNode(model.getNodeFromName(name));
                requete(request, res);
            }
        });
        
        addArcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String request = "Ajout d'arc";
                String name1 = textFieldDialog(request,
                        "Nom du premier sommet");
                if (name1 == null) {
                    return;
                }
                
                String name2 = textFieldDialog(request,
                        "Nom du deuxième sommet");
                if (name2 == null) {
                    return;
                }
                
                boolean res = false;
                try {
                    res = model.addArc(model.getNodeFromName(name1),
                            model.getNodeFromName(name2));
                } catch (OperationNotSupportedException e1) {
                    // Rien
                }
                requete(request, res);
            }
        });
        
        removeArcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String request = "Retirage d'arc";
                String name1 = textFieldDialog(request,
                        "Nom du premier sommet");
                if (name1 == null) {
                    return;
                }
                
                String name2 = textFieldDialog(request,
                        "Nom du deuxième sommet");
                if (name2 == null) {
                    return;
                }
                
                boolean res = false;
                try {
                    res = model.removeArc(model.getNodeFromName(name1),
                            model.getNodeFromName(name2));
                } catch (OperationNotSupportedException e1) {
                    // Rien
                }
                requete(request, res);
            }
        });
        
        
    }
    
    private String textFieldDialog(String request, String field) {
        return (String) JOptionPane.showInputDialog(null,
                field,
                request,
                JOptionPane.QUESTION_MESSAGE);
    }
    
    private void requete(String request, boolean result) {
        if (!result) {
            requestTextArea.setText(request + ": Échec");
            JOptionPane.showMessageDialog(null,
                    request + ": Échec", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            requestTextArea.setText(request + ": Réussi");
        }
    }
    
    private void display() {
        refresh();
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
    
    private void refresh() {
        graphTextArea.setText(model.toString());
        List<Integer> l = model.getNbUsersAndPages();
        stateLabel.setText(model.getNodeNb() + " sommets ("
                + l.get(0) + " utilisateur(s) & " + l.get(1) + " page(s)), "
                        + model.getArcNb() + " arcs");
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
