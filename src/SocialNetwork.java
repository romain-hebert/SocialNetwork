

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import javax.naming.OperationNotSupportedException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
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
    private JLabel stateLabel;
    private JButton addNodeButton;
    private JButton removeNodeButton;
    private JButton addArcButton;
    private JButton removeArcButton;
    private JButton sortedByNameButton;
    private JButton sortedByDegreeButton;
    private JButton pageRankButton;
    private JButton shortestPathButton;
    private JButton getAdminsButton;
    private JButton saveButton;
    private JButton loadButton;
    
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
        stateLabel = new JLabel("Graphe vide");
        graphTextArea.setEditable(false);
        requestTextArea = new JTextArea(TEXTAREA_ROWS, TEXTAREA_COL);
        requestTextArea.setEditable(false);
        addNodeButton = new JButton("Ajouter");
        removeNodeButton = new JButton("Retirer");
        addArcButton = new JButton("Ajouter");
        removeArcButton = new JButton("Retirer");
        sortedByNameButton = new JButton("Trié par nom");
        sortedByDegreeButton = new JButton("Trié par degré");
        pageRankButton = new JButton("Page Rank");
        shortestPathButton = new JButton("Chemin + court");
        getAdminsButton = new JButton("Administrateurs");
        saveButton = new JButton("Sauvegarder");
        loadButton = new JButton("Charger");
    }
    
    private void placeComponents() {
        JPanel p = new JPanel(new GridLayout(0, 1)); {
            JPanel q = new JPanel(new BorderLayout()); {
                JPanel r = new JPanel(new BorderLayout()); {
                    r.add(new JScrollPane(graphTextArea), BorderLayout.CENTER);
                    r.add(stateLabel, BorderLayout.SOUTH);
                }
                q.add(r, BorderLayout.CENTER);
                q.setBorder(BorderFactory.createTitledBorder(
                        "État du graphe"));
            }
            p.add(q);
            q = new JPanel(new BorderLayout()); {
                q.add(new JScrollPane(requestTextArea), BorderLayout.CENTER);
                q.setBorder(BorderFactory.createTitledBorder(
                        "Resultat de la requête"));
            }
            p.add(q);
            p.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }
        mainFrame.add(p, BorderLayout.CENTER);
        
        p = new JPanel(new BorderLayout()); {
            JPanel q = new JPanel(new GridLayout(0, 1)); {
                // Panel Sommets
                JPanel r = new JPanel(new GridLayout(0, 1)); {
                    r.add(addNodeButton);
                    r.add(removeNodeButton);
                    r.setBorder(BorderFactory.createTitledBorder("Sommets"));
                }
                q.add(r);
                // Panel Arcs
                r = new JPanel(new GridLayout(0, 1)); {
                    r.add(addArcButton);
                    r.add(removeArcButton);
                    r.setBorder(BorderFactory.createTitledBorder("Arcs"));
                }
                q.add(r);
                // Tris
                r = new JPanel(new GridLayout(0, 1)); {
                    r.add(sortedByNameButton);
                    r.add(sortedByDegreeButton);
                    r.setBorder(BorderFactory.createTitledBorder("Tris"));
                }
                q.add(r);
                // Algos
                r = new JPanel(new GridLayout(0, 1)); {
                    r.add(pageRankButton);
                    r.add(shortestPathButton);
                    r.setBorder(BorderFactory.createTitledBorder("Algos"));
                }
                q.add(r);
            }
            p.add(q, BorderLayout.NORTH);
            
            q = new JPanel(new GridLayout(0, 1)); {
                q.add(saveButton);
                q.add(loadButton);
                q.setBorder(BorderFactory.createTitledBorder("Gestion"));
            }
            p.add(q, BorderLayout.SOUTH);
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
                        mainFrame,
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
        
        sortedByNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = "";
                for (Sommet s : model.getNodesSortedByName()) {
                    str += s.toString() + "\n";
                }
                requestTextArea.setText(str);
            }
        });
        
        sortedByDegreeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = "";
                for (Sommet s : model.getNodesSortedByDegree()) {
                    str += s.toString() + "\n";
                }
                requestTextArea.setText(str);
            }
        });
        
        pageRankButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = "";
                for (Entry<Sommet, Double> en
                        : model.computePageRank().entrySet()) {
                    str += en.getKey().getName() + "=" + en.getValue() + "\n";
                }
                requestTextArea.setText(str);
            }
        });
        
        shortestPathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String request = "Chemin le plus court";
                String name = textFieldDialog(request,
                        "Choisir le sommet source");
                if (name == null) {
                    return;
                }
                
                Sommet s = model.getNodeFromName(name);
                requete(request, s != null);
                String str = "";
                for (Entry<Sommet, Integer> en
                        : model.shortestPath(s).entrySet()) {
                    str += en.getKey().getName() + "=" + en.getValue() + "\n";
                }
                requestTextArea.setText(str);
            }
        });
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String request = "Sauvegarde";
                boolean result = true;
                JFileChooser fc = new JFileChooser();
                if (fc.showSaveDialog(mainFrame) 
                        == JFileChooser.APPROVE_OPTION) {
                    try {
                        model.save(fc.getSelectedFile());
                    } catch (IOException e1) {
                        result = false;
                    } finally {
                        requete(request, result);
                    }
                }
            }
        });
        
        loadButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
    }
    
    // Ouvre une boite de dialogue d'entrée ayant request comme titre et field 
    //  comme message.
    private String textFieldDialog(String request, String field) {
        return (String) JOptionPane.showInputDialog(
                mainFrame,
                field,
                request,
                JOptionPane.QUESTION_MESSAGE);
    }
    
    // Affiche un message approprié à la requete selon son resultat
    private void requete(String request, boolean result) {
        if (!result) {
            requestTextArea.setText(request + ": Échec");
            JOptionPane.showMessageDialog(
                    mainFrame,
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
        stateLabel.setText(model.getNodeNb() + " sommets (dont "
                + l.get(0) + " utilisateur(s) & " + l.get(1) + " page(s)), "
                + model.getArcNb() + " arcs, " + model.meanUserAge()
                + " ans en moyenne");
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
