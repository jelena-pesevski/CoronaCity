/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coronaCity.appFrames;

import coronaCity.model.grad.CoronaCity;
import coronaCity.model.polja.KontrolniPunkt;
import coronaCity.model.stanovnici.Stanovnik;
import coronaCity.model.temperatura.MjeracTemperature;
import coronaCity.utils.StatistickiPodaci;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author Korisnik
 */
public class MainFrame extends JFrame{
    
    public static boolean simulacijaZaustavljena=false;
    
    private final JButton dugme1=new JButton("OMOGUCI KRETANJE");
    private final JButton dugme2=new JButton("POSALJI AMBULANTNO VOZILO");
    private final JButton dugme3=new JButton("STANJE AMBULANTI");
    private final JButton dugme4=new JButton("STATISTICKI PODACI");
    private final JButton dugme5=new JButton("ZAUSTAVI SIMULACIJU");
    private final JButton dugme6=new JButton("NASTAVI SIMULACIJU");
    private final JButton dugme7=new JButton("ZAVRSI SIMULACIJU");
    
    private final JLabel tekstZ=new JLabel("Broj zarazenih: ");
    private final JLabel tekstO=new JLabel("Broj oporavljenih: ");
    
    private final JLabel brZarazenih= new JLabel("0");
    private final JLabel brOporavljenih= new JLabel("0");
    
    private final JTextArea ispisKretanje=new JTextArea();
    private JScrollPane scrollJedan;
    
    private final JTextArea alarmiIspis=new JTextArea();
    private JScrollPane scrollDva;
    
    private JPanel glavni;
    
    private static MainFrame instance;
    
    public static MainFrame getInstance() {
        return instance;
    }
    
    public static MainFrame napraviJedinuInstancu(int velicina, int brOdraslih, int brDjece, int brStarih, int brKuca, int brPunktova, int brVozila){
        instance = new MainFrame(velicina, brOdraslih, brDjece, brStarih,  brKuca, brPunktova,  brVozila);
        return instance;
    }
            
    private MainFrame(int velicina, int brOdraslih, int brDjece, int brStarih, int brKuca, int brPunktova, int brVozila) {
        this.setLayout(new BorderLayout());
        
        glavni= new JPanel(new GridLayout(2, 1));       
        glavni.add(CoronaCity.napraviJedinuInstancu(velicina, brOdraslih, brDjece, brStarih, brKuca, brPunktova, brVozila));
        
        ispisKretanje.setEditable(false);
        scrollJedan=new JScrollPane(ispisKretanje, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollJedan.setPreferredSize(new Dimension(50, 50));
        glavni.add(scrollJedan);
        
        this.add(glavni);
         
        alarmiIspis.setEditable(false);
        scrollDva=new JScrollPane(alarmiIspis,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollDva.setPreferredSize(new Dimension(20, 60));
        this.add(scrollDva,BorderLayout.PAGE_END);
        
        JPanel meni=new JPanel(new GridLayout(7, 1));
        meni.add(dugme1);
        meni.add(dugme2);
        meni.add(dugme3);
        meni.add(dugme4);
        meni.add(dugme5);
        meni.add(dugme6);
        meni.add(dugme7);
        meni.setPreferredSize(new Dimension(200, 150));
        this.add(meni, BorderLayout.EAST);
        
        JPanel info= new JPanel(new GridLayout(1, 2, 10, 50));
        
        JPanel zarazeni=new JPanel(new GridLayout(1, 2, 10, 10));
        zarazeni.add(tekstZ);
        zarazeni.add(brZarazenih);
        
        JPanel oporavljeni=new JPanel(new GridLayout(1, 2, 10, 10));
        oporavljeni.add(tekstO);
        oporavljeni.add(brOporavljenih);
        
        info.add(zarazeni);
        info.add(oporavljeni);
        
        this.add(info, BorderLayout.NORTH);
        
        dodajOsluskivace();
        
        DefaultCaret caret1 = (DefaultCaret)ispisKretanje.getCaret();
        caret1.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        DefaultCaret caret2 = (DefaultCaret)alarmiIspis.getCaret();
        caret2.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(100, 100, 600, 600);
        this.setTitle("Corona City");
        this.setVisible(true);
    }

    private void dodajOsluskivace() {
        dugme1.addActionListener(e->{
            CoronaCity.getInstance().pokreniSimulaciju();
            dugme1.setEnabled(false);
        });
        dugme2.addActionListener(e->{
            if(!simulacijaZaustavljena){
               CoronaCity.getInstance().obradiAlarme();
            }
        });
        dugme3.addActionListener(e->{
             new StanjeAmbulantiFrame(CoronaCity.getInstance());

        });
        dugme4.addActionListener(e->{
            new StatistikaFrame();
        });
        dugme5.addActionListener(e->{
           if(!simulacijaZaustavljena){
               FileOutputStream pisac= null;
                try {
                    //ugasi stanovnike, punktove i mjeracTemperature
                    //da bi se brze zavrsila serijalizacija
                    //tj. da ne bi te niti uzimale procesorsko vrijeme
                    //i radile iako je onemoguceno kretanje
                    //bez gasenja je mnogo sporo zaustavljanje

                    simulacijaZaustavljena=true;
                    for(Stanovnik s:CoronaCity.getInstance().getStanovnici()){
                        s.trajnoZaustaviKretanje();
                    }
                    for(KontrolniPunkt p:CoronaCity.getInstance().getkPunktovi()){
                        p.zaustaviPunkt();
                    }

                    pisac = new FileOutputStream("coronaCity.ser");
                    ObjectOutputStream upisObjekta = new ObjectOutputStream(pisac);
                    upisObjekta.writeObject(CoronaCity.getInstance());
                    upisObjekta.close();
                    pisac.close();

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } /*finally {
                    try {
                        pisac.close();
                    } catch (IOException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }*/
           }
        });
        dugme6.addActionListener(e->{
            if(simulacijaZaustavljena){
                FileInputStream citac = null;
                try {
                    citac = new FileInputStream("coronaCity.ser");
                    ObjectInputStream citanjeObjekta = new ObjectInputStream(citac);
                    CoronaCity oldCity=CoronaCity.getInstance();
                    CoronaCity updatedCity=(CoronaCity)citanjeObjekta.readObject();
                    CoronaCity.updateInstancu(updatedCity);
                    citanjeObjekta.close();
                    citac.close();

                    this.remove(glavni);
                    glavni=new JPanel(new GridLayout(2,1));
                    glavni.add(updatedCity);
                    glavni.add(scrollJedan);
                    this.add(glavni);
                    this.revalidate();

                    //pokrecemo ponovo niti sa starim stanjima
                    //koja su ocuvana zbog flegova
                    
                    for(Stanovnik s:CoronaCity.getInstance().getStanovnici()){
                        s.pokreniStanovnikaPoslijeZaustavljanjaSimulacije();
                    }
                    for(KontrolniPunkt p:CoronaCity.getInstance().getkPunktovi()){
                        p.pokreniPunkt();
                    }
                    simulacijaZaustavljena=false;

                  //  System.out.println("poslije readO");
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } /*finally {
                    try {
                        citac.close();
                    } catch (IOException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }*/
            }
        });
        dugme7.addActionListener(e->{
            CoronaCity.getInstance().zavrsiSimulaciju();
            this.dispose();
           
            StartFrame.endTime = System.nanoTime();
            long vrijemeTrajanjaSimulacije = (StartFrame.endTime -  StartFrame.startTime) / 1_000_000_000;
            
            SimpleDateFormat formatter= new SimpleDateFormat("HH-mm-ss'--'dd-MM-yyyy");
            String imeFajla = "SIM-JavaKov-20-" + formatter.format(new Date())+ ".txt";
           
            FileWriter writer;
            try{
               writer=new FileWriter(imeFajla);
               DecimalFormat df = new DecimalFormat("#,##");
               
               writer.write("Ukupno vrijeme trajanja simulacije iznosi " + vrijemeTrajanjaSimulacije + " sekundi.\n");
               writer.write("Ukupan broj kreiranih objekata tipa Stanovnik je->" + StatistickiPodaci.getUkupanBrojStanovnika() + ".\n");
               writer.write("Ukupan broj kreiranih objekata tipa Ambulanta je->" + CoronaCity.getInstance().getAmbulante().size() + ".\n");
               writer.write("Ukupan broj kreiranih objekata tipa AmbulatnoVozilo je->" + CoronaCity.getInstance().getVozila().size() + ".\n");
               writer.write("Ukupan broj kreiranih objekata tipa Kuca je->" + CoronaCity.getInstance().getKuce().size() + ".\n");
               writer.write("Ukupan broj kreiranih objekata tipa KontrolniPunkt je->" + CoronaCity.getInstance().getkPunktovi().size() + ".\n");
               
               writer.write("Informacije o zarazenima virusom Korona->\n");
               writer.write("U toku simulacije zarazeno je "+StatistickiPodaci.getBrojPotvrdjenihSlucajeva()+"\n");
               writer.write("Trenuntno je zarazeno "+StatistickiPodaci.getBrojZarazenih()+", a oporavljeno "+StatistickiPodaci.getBrojOporavljenih()+"\n");
               writer.write("Procentualno:"+df.format(StatistickiPodaci.getProcenatZarazenih())+"%, "+
                       df.format(StatistickiPodaci.getProcenatOporavljenih())+"%\n");
               writer.write("Trenutno zarazeno muskih:"+df.format(StatistickiPodaci.getProcenatMuskihZarazenih())+"%\n");
               writer.write("Trenutno zarazeno zenskih:"+df.format(StatistickiPodaci.getProcenatZenskihZarazenih())+"%\n");
               writer.write("Odrasli:"+df.format(StatistickiPodaci.getProcenatZarazenihOdrasli())+"%"+
                       ", stari:"+df.format(StatistickiPodaci.getProcenatZarazenihStari())+"%"+
                       ", djeca:"+df.format(StatistickiPodaci.getProcenatZarazenihDjece())+"%\n");
               writer.close();
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.exit(0);
           
        });
    }
    
    public void dodajTekst(String tekst){
        ispisKretanje.append(tekst+"\n");
    }
    
    public void ispisiAlarm(String tekst){
        alarmiIspis.append(tekst+"\n");
                
    }
    
    public void obrisiRedAlarma(){
        String tekst=alarmiIspis.getText();
        String noviTekst=tekst.substring(0, tekst.lastIndexOf("!!!"));
        alarmiIspis.setText(noviTekst);
    }
    
    public void updateBrojZarazenihIOporavljenih(){
        brZarazenih.setText(Integer.toString(StatistickiPodaci.getBrojZarazenih()));
        brOporavljenih.setText(Integer.toString(StatistickiPodaci.getBrojOporavljenih()));
    }
    
}
