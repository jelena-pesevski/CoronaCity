package coronaCity.model.polja;

import coronaCity.model.grad.CoronaCity;
import coronaCity.model.stanovnici.Stanovnik;
import coronaCity.utils.RandomUtils;
import coronaCity.utils.StatistickiPodaci;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Ambulanta extends Polje{
    private static final String ERROR_MSG="Nije moguce kreirati fajl zarazeni.txt.";
    private static BufferedImage slika;
    private static final String IMG_PATH="./slicice/ambulanta.png";
    private static final String IME_FAJLA="zarazeni.txt";
    private static final String DST_DIR="zarazeni";
    private static final File FAJL=new File(DST_DIR+File.separator+ IME_FAJLA);  //kad se dole dodijeli ne moze se mijenjati
    
    private int kapacitet;
    private int popunjenost;
    
    static{
        try{
            slika=ImageIO.read(new File(IMG_PATH));
        }catch(IOException ex){
            Logger.getLogger(Ambulanta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static{
        try {
            if(!FAJL.exists()){
                File dstDir = new File(DST_DIR);
                boolean folderExists = dstDir.exists();
                if (!folderExists) {
                    folderExists=dstDir.mkdir();
                }
                if(folderExists){
                    try {
                        FAJL.createNewFile();
                    } catch (IOException ex) {
                        Logger.getLogger(Ambulanta.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    JOptionPane.showMessageDialog (null, "Nije moguce kreirati direktorijum zarazeni.");
                }
                
            }else{
                //isprazni prethodni fajl
               // new RandomAccessFile(FAJL,"rw").setLength(0);
                FileOutputStream writer;
                writer = new FileOutputStream(FAJL);
                writer.write(("").getBytes());
                writer.close();  
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Ambulanta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Ambulanta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static File getFajl() {
        return FAJL;
    }
    
    public int getPopunjenost() {
        return popunjenost;
    }

    public void setPopunjenost(int popunjenost) {
        this.popunjenost = popunjenost;
    }
    
    public int getKapacitet() {
        return kapacitet;
    }

    public void setKapacitet(int kapacitet) {
        this.kapacitet = kapacitet;
    }
    
    public synchronized int getBrojSlobodnihMjesta(){
        return kapacitet-popunjenost;
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        g.drawImage(slika, 0, 0, this.getWidth(), this.getHeight(), null);
    }
    
    public Ambulanta(int vrsta, int kolona) {
        super(vrsta, kolona);
        int donjaGranica=(int)Math.ceil(StatistickiPodaci.getUkupanBrojStanovnika()*10.00/100);
        int gornjaGranica=(int)Math.ceil(StatistickiPodaci.getUkupanBrojStanovnika()*15.00/100);
        kapacitet=RandomUtils.randomInt(gornjaGranica-donjaGranica+1)+donjaGranica;
    }

    @Override
    public String toString(){
        return " ambulanta- kapacitet:"+kapacitet+", raspolozivo:"+this.getBrojSlobodnihMjesta()+super.toString();
    }
    
    public void upisiZarazenogUFajl(Stanovnik s){
        synchronized(FAJL){
            try {
                StatistickiPodaci.obradiZarazenog(s);
                PrintWriter pw=new PrintWriter(new FileOutputStream(FAJL, true), true);
                pw.println(s.getIme()+s.getIdOsobe()+" je zarazen. Temperatura:"+String.format("%.1f", s.getTrenutnaTemp()));
                pw.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Ambulanta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void brisiZarazenogIzFajla(Stanovnik s){
        synchronized(FAJL){
            StatistickiPodaci.obradiOporavljenog(s);
            try(Stream<String> sadrzajFajla=Files.lines(FAJL.toPath())){ 
                List<String> noviSadrzaj=sadrzajFajla
                    .filter(line->!(line.startsWith(s.getIme()+s.getIdOsobe())))
                    .collect(Collectors.toList());
                
                 //obrisi prethodno i dodaj novi redukovani sadrzaj
                RandomAccessFile rnd= new RandomAccessFile(FAJL,"rw");
                rnd.setLength(0);
              /*  for(String line: noviSadrzaj){
                    rnd.writeUTF(line);
                }*/
                rnd.close();
                Files.write(FAJL.toPath(),(Iterable<String>)noviSadrzaj::iterator); 

                /*FileOutputStream writer=new FileOutputStream(FAJL);
                writer.write(("").getBytes());
                writer.close();*/
                
            } catch (IOException ex) {
                Logger.getLogger(Ambulanta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public synchronized void smjestiStanovnika(Stanovnik s){
        this.dodajStanovnikaNaPolje(s);
        s.setPolje(this);
        this.popunjenost++;
        this.upisiZarazenogUFajl(s);
    }
    
    public synchronized void ukloniStanovnikaIzAmbulante(Stanovnik s){
        this.popunjenost--;
        this.brisiZarazenogIzFajla(s);
        s.setPolje(s.getMojaKuca());
        //System.out.println(s.getIme()+s.getIdOsobe()+ " ozdravio." +String.format("%.1f", s.aSredinaTemp()));
        CoronaCity.getInstance().pisiNaFrejm(s.getIme()+s.getIdOsobe()+ " ozdravio." +String.format("%.1f", s.aSredinaTemp()));
    }
    
    public static void ponovoPokreniUkucane(Stanovnik s){
        s.getMojaKuca().getListaUkucana().stream().filter(ukucan -> (ukucan!=s)).forEachOrdered(ukucan -> {
             ukucan.pustiStanovnikaIzIzolacije();                    //ako je ukucan isto u bolnici onda se ova funkcija nece ni pozvati
        });
    }
    
    public static boolean imaLiUkucanaUAmbulanti(Stanovnik s){
        for(Stanovnik ukucan: s.getMojaKuca().getListaUkucana()){
            if(ukucan!=s && ukucan.getPolje() instanceof Ambulanta){  
                return true;
            }
        }return false;
    }
    
    @Override
    public synchronized List<Stanovnik> getStanovniciNaPolju() {
        return super.getStanovniciNaPolju();
    }
    
    @Override
    public synchronized void dodajStanovnikaNaPolje(Stanovnik s){
        super.dodajStanovnikaNaPolje(s);
    }
    
    @Override
    public synchronized void ukloniStanovnikaSaPolja(Stanovnik s){
        super.ukloniStanovnikaSaPolja(s);
    }
    

}
