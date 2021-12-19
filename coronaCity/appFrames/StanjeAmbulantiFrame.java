/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coronaCity.appFrames;

import coronaCity.model.grad.CoronaCity;
import coronaCity.model.polja.Ambulanta;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author Korisnik
 */
public class StanjeAmbulantiFrame extends JFrame{
    
    private static final String PORUKA="Ambulanta uspjesno kreirana.";
    private final JButton dugme=new JButton("KREIRAJ NOVU AMBULANTU");
    
    private JTextArea ispisStanja=new JTextArea();
    private JScrollPane scroll;
    
    private CoronaCity mojGrad;
     
    public StanjeAmbulantiFrame(CoronaCity grad){
        this.setLayout(new BorderLayout());
        ispisStanja.setEditable(false);
        scroll=new JScrollPane(ispisStanja, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setPreferredSize(new Dimension(80, 80));
        
        DefaultCaret caret = (DefaultCaret)ispisStanja.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        this.add(ispisStanja, BorderLayout.CENTER);
        this.add(dugme, BorderLayout.SOUTH);
     
        mojGrad=grad;
        
        dodajOsluskivace();
        
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setBounds(100, 100, 500, 220);
        this.setTitle("Stanje Ambulanti");
        this.setVisible(true);
        
        int i=1;
        for(Ambulanta a:mojGrad.getAmbulante()){
            ispisStanja.append(i+"."+a+"\n");
            i++;
        }        
    }
    
    private void dodajOsluskivace(){
        dugme.addActionListener(e->{
            if(!MainFrame.simulacijaZaustavljena){
                boolean done=mojGrad.kreirajNovuAmbulantu();
                if(done){
                    JOptionPane.showMessageDialog(this, PORUKA);
                }
                
            }
        });
    }
}
