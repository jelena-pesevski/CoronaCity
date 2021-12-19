package coronaCity.model.grad;

import coronaCity.appFrames.MainFrame;
import coronaCity.appFrames.StartFrame;
import coronaCity.model.polja.Ambulanta;
import coronaCity.model.polja.KontrolniPunkt;
import coronaCity.model.polja.Kuca;
import coronaCity.model.polja.Polje;
import coronaCity.model.polja.Put;
import coronaCity.model.stanovnici.Dijete;
import coronaCity.model.stanovnici.Odrasli;
import coronaCity.model.stanovnici.Stanovnik;
import coronaCity.model.stanovnici.Stari;
import coronaCity.model.temperatura.MjeracTemperature;
import coronaCity.model.transport.Alarm;
import coronaCity.model.transport.AmbulantnoVozilo;
import coronaCity.utils.FileWatcher;
import coronaCity.utils.Locker;
import coronaCity.utils.RandomUtils;
import coronaCity.utils.StatistickiPodaci;
import java.awt.GridLayout;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import javax.swing.JPanel;

public class CoronaCity extends JPanel implements Serializable{
    private static CoronaCity instance;
    
    public static CoronaCity getInstance() {
        return instance;
    }
    
    //moguce je kreirati samo jednu instancu CoronaCity-ja u toku simulacije
    public static CoronaCity napraviJedinuInstancu(int velicina, int brOdraslih, int brDjece, int brStarih, int brKuca, int brPunktova, int brVozila){
        instance = new CoronaCity(velicina, brOdraslih, brDjece, brStarih,  brKuca, brPunktova,  brVozila);
        
        //ovo je izdvojeno ovdje jer koristi metode instance
        //pa mora instanca biti inicijalizovana
        instance.dodajStanovnikeUKuce();
        for(KontrolniPunkt p: instance.getkPunktovi()){
            p.postaviGraniceZaSkeniranje();
        }
        
        return instance;
    }
    
    //update nakon deserijalizacije
    public static void updateInstancu(CoronaCity update){
        CoronaCity.instance=update;
    }
    
    //kad je pritisnuto dugme zapocni sim. sa MainFrame-a
    private static boolean simulacijaZapoceta;
    
    private static int velicina;
    
    private Map<Integer, Kuca> kuce =Collections.synchronizedMap(new HashMap<>()) ;
    private List<Ambulanta> ambulante=Collections.synchronizedList(new ArrayList<>());
    private List<KontrolniPunkt> kPunktovi=Collections.synchronizedList(new ArrayList<>());
    private List<Stanovnik> stanovnici=Collections.synchronizedList(new ArrayList<>());
    private List<AmbulantnoVozilo> vozila=Collections.synchronizedList(new ArrayList<>());
    private ArrayDeque<Alarm> alarmi=new ArrayDeque<>();
    
    //ne serijalizuju se-staticka su
    private static MjeracTemperature toplomjer;
    private static FileWatcher mojWatcher;
    
    private Polje[][] matrica;
      
    private CoronaCity(int velicina, int brOdraslih, int brDjece, int brStarih, int brKuca, int brPunktova, int brVozila) {
        super();
        CoronaCity.velicina=velicina;
        this.setLayout(new GridLayout(velicina, velicina,1,1));
        matrica = new Polje[velicina][velicina];
        dodajKuce(brKuca);
        napraviStanovnike(brOdraslih, brDjece, brStarih);
        StatistickiPodaci.postaviParametreStanovnistva(brOdraslih, brDjece, brStarih);
        dodajPunktove(brPunktova);
        postaviPocetneAmbulante();
        kreirajVozila(brVozila);
        popuniMatricu();
        
        mojWatcher=new FileWatcher(Ambulanta.getFajl().getParentFile().toPath());
        mojWatcher.pokreniFileWatcher();
    }
    
    
    public Polje getPoljeMatrice(int vrsta, int kolona){
        if(vrsta>=velicina || kolona>=velicina || vrsta<0 || kolona<0){
            return null;
        }else{
            return matrica[vrsta][kolona];
        }
    }

    public Polje[][] getMatrica() {
        return matrica;
    }
    
    public void setMatrica(Polje[][] matrica) {
        this.matrica = matrica;
    }
    
    public static void setVelicina(int velicina) {
        CoronaCity.velicina = velicina;
    }
    
    public static int getVelicina() {
        return velicina;
    }

    public Map<Integer, Kuca> getKuce() {
        return kuce;
    }

    public void setKuce(Map<Integer, Kuca> kuce) {
        this.kuce = kuce;
    }

    public List<Ambulanta> getAmbulante() {
        return ambulante;
    }

    public void setAmbulante(List<Ambulanta> ambulante) {
        this.ambulante = ambulante;
    }

    public List<KontrolniPunkt> getkPunktovi() {
        return kPunktovi;
    }

    public void setkPunktovi(List<KontrolniPunkt> kPunktovi) {
        this.kPunktovi = kPunktovi;
    }

    public List<Stanovnik> getStanovnici() {
        return stanovnici;
    }

    public void setStanovnici(List<Stanovnik> stanovnici) {
        this.stanovnici = stanovnici;
    }
  
    public List<AmbulantnoVozilo> getVozila() {
        return vozila;
    }

    public void setVozila(List<AmbulantnoVozilo> vozila) {
        this.vozila = vozila;
    }

    public ArrayDeque<Alarm> getAlarmi() {
        return alarmi;
    }

    public void setAlarmi(ArrayDeque<Alarm> alarmi) {
        this.alarmi = alarmi;
    }   

    public MjeracTemperature getToplomjer() {
        return toplomjer;
    }

    public void setToplomjer(MjeracTemperature toplomjer) {
        this.toplomjer = toplomjer;
    }
    
    
    public void dodajKuce(int brKuca){
        while(brKuca>0){
            int x=RandomUtils.randomInt(velicina-2)+1;
            int y=RandomUtils.randomInt(velicina-2)+1;
            if(matrica[x][y]==null){
               Kuca k=new Kuca(x,y);
               matrica[x][y]=k;
               kuce.put(k.getIdKuce(),k);
               brKuca--;
            }
        }
    }
    
    public void dodajPunktove(int brPunktova){
        while(brPunktova>0){
            int x=RandomUtils.randomInt(velicina-2)+1;
            int y=RandomUtils.randomInt(velicina-2)+1;
            if(matrica[x][y]==null){
                KontrolniPunkt p=new KontrolniPunkt(x,y);
                matrica[x][y]=p;
                kPunktovi.add(p);
     //           p.postaviGraniceZaSkeniranje();
                brPunktova--;
            }
        }
    }
    public void postaviPocetneAmbulante() {
        Ambulanta a1=new Ambulanta(0, 0);
        ambulante.add(a1);
        matrica[0][0]=a1;
        Ambulanta a2=new Ambulanta(0,velicina-1);
        ambulante.add(a2);
        matrica[0][velicina-1]=a2;
        Ambulanta a3=new Ambulanta(velicina-1,0);
        ambulante.add(a3);
        matrica[velicina-1][0]=a3;
        Ambulanta a4=new Ambulanta(velicina-1,velicina-1);
        ambulante.add(a4);
        matrica[velicina-1][velicina-1]=a4;
    }
    
    //popunjava ostatak matrice putem
    public void popuniMatricu(){
        for(int i=0; i<velicina; i++){
            for(int j=0; j<velicina; j++){
                if(matrica[i][j]==null){
                    matrica[i][j]=new Put(i,j);
                }
                this.add(matrica[i][j]);
            }
        }
    }   
    
    public void kreirajVozila(int brVozila){
        while(brVozila>0){
            vozila.add(new AmbulantnoVozilo());
            brVozila--;
        }
    }
    
    public void napraviStanovnike(int brOdraslih, int brDjece, int brStarih){
        while(brOdraslih>0){
            stanovnici.add(new Odrasli());
            brOdraslih--;
        }
        while(brStarih>0){
            stanovnici.add(new Stari());
            brStarih--;
        }
        while(brDjece>0){
            stanovnici.add(new Dijete());
            brDjece--;
        }
    }
    
    //uz napomenu da dijete ne moze biti samo u kuci
    //postavlja prvo odrasle, i stare redom po jedno, koliko puta je potrebno
    //da prodje kroz niz kuca, kad dodje do kraja ponovo ide od prve kuce
    public void dodajStanovnikeUKuce(){
        int brojStanovnika=stanovnici.size();
        int brKuca=kuce.size();
        for(int i=0, j=0; i<brojStanovnika; i++, j++){
            Kuca k=kuce.get(j);
            Stanovnik s=stanovnici.get(i);
            if(!(s instanceof Dijete && k.getBrojUkucana()==0)){
                k.dodajUListuUkucana(s);
                s.setIdentifikatorKuce(k.getIdKuce());
                k.dodajStanovnikaNaPolje(s);
                s.setPolje(k);

                if(s instanceof Stari){
                    ((Stari) s).postaviGraniceZaKretanje();
                }else if(s instanceof Odrasli){
                    ((Odrasli) s).postaviGraniceZaKretanje();
                }
            }else{
                i--;
            }
            if(j==brKuca-1){
                j=-1;
            }
        }
    }
    
    public int getBrojStanovnika(){
        return stanovnici.size();
    }
    
    public Kuca pronadjiKucuPoId(int id){
        return kuce.get(id);
    }
    
   /* public void ispisi(){
        int brS=kuce.size();
        for(int i=0; i<brS; i++){
            System.out.println("Kuca broj:"+(i+1));
            kuce.get(i).ispisiUkucane();
        }
    }*/
    
    public void pisiNaFrejm(String tekst){
        MainFrame.getInstance().dodajTekst(tekst);
    }
    
    //za alarme 
    public void obavijestiOAlarmu(Alarm alarm){
        MainFrame.getInstance().ispisiAlarm(alarm.getAlarmTekst()); 
    }
    
    
    public void dodajAlarm(Alarm alarm){
        synchronized(Locker.getLockZaAlarme()){
            if(alarm!=null){
              alarmi.push(alarm);
              obavijestiOAlarmu(alarm);
            }
        }
    }
    
    public Alarm ukloniAlarm(){
        synchronized(Locker.getLockZaAlarme()){
                MainFrame.getInstance().obrisiRedAlarma(); //brise alarm vizuelno
                return alarmi.pop();
            }
    }
    
    public boolean isAlarmiEmpty(){
        synchronized(Locker.getLockZaAlarme()){
            return alarmi.isEmpty();
        }
    }
    
    //za punktove
    public void ukljuciPunktove(){
        kPunktovi.forEach(k -> {
            k.pokreniPunkt();
        });
    }
    
    public void iskljuciPunktove(){
        kPunktovi.forEach(k -> {
            k.zaustaviPunkt();
        });
    }
    
    //za vozila
    //vraca trenutni broj slobodnih
    public int brojSlobodnihVozila(){
        int brojac=0;
        for(AmbulantnoVozilo v: vozila){
            if(v.isSlobodnoVozilo())
                brojac++;
        }
        return brojac;
    }
    
    public AmbulantnoVozilo getSlobodnoVozilo(){ //vraca prvo slobodno vozilo
        for(AmbulantnoVozilo v: vozila){
            if(v.isSlobodnoVozilo()){
                v.setSlobodnoVozilo(false);
                return v;
            }    
        }
        return null;
    }
    
    public void oslobodiVozila(){
        vozila.stream().filter(v -> (!v.isSlobodnoVozilo())).forEachOrdered(v -> {
            v.setSlobodnoVozilo(true);
        });
    }
    
    public Ambulanta dohvatiAmbulantuSaSlobodnimMjestom(){
      //  synchronized(ambulante){
        for(Ambulanta a: ambulante){
            if(a.getBrojSlobodnihMjesta()>0){
                return a;
            }
        }
        return null;
    }
    
    //skida alarme i salje vozila na zadate lokacije
    //po zavrsetku oslobadja vozila
    //da vise nisu zauzeta
    public void obradiAlarme(){
        Ambulanta ambulanta;
        while(!isAlarmiEmpty() && brojSlobodnihVozila()>0
                && (ambulanta=dohvatiAmbulantuSaSlobodnimMjestom())!=null){
            Alarm a=ukloniAlarm();
            AmbulantnoVozilo v=getSlobodnoVozilo();
            v.posaljiVozilo(a.getOsoba(), ambulanta);
            a.obavijestiUkucane(); 
        }
        oslobodiVozila(); 
    }
    
    public void pokreniSimulaciju(){
        synchronized(Locker.getLock()){         //da bude atomicno
            getStanovnici().forEach(s -> {
                s.pokreniStanovnikaPrviPut();
            });
                
            setToplomjer(new MjeracTemperature());
            Timer timer=new Timer();
            timer.schedule(getToplomjer(),0, 30000); //!!! vrati na 30
                
            ukljuciPunktove();
            simulacijaZapoceta=true;
        }
    }
    
    public void zavrsiSimulaciju(){
        if(simulacijaZapoceta){
            getToplomjer().prekiniMjerenjaTemperatura();
            iskljuciPunktove();
            getStanovnici().forEach(s -> {
                s.trajnoZaustaviKretanje();
            });
            mojWatcher.zaustaviFileWatcher();
            
        }
    }
    
    //salje glavnoj formi znak da azurira brojeve zarazenih
    //i oporavljenih
    /*public void updateBrojeveZiO(){
        MainFrame.getInstance().updateBrojZarazenihIOporavljenih();
    }*/
    
    public boolean kreirajNovuAmbulantu(){
        boolean imaLiPopunjene=false;
        
        //kreira novu samo ako u nekoj trenutno nema mjesta
        synchronized(Locker.getLockZaAmbulante()){
            for(Ambulanta a: ambulante){
                if(a.getBrojSlobodnihMjesta()==0){
                    imaLiPopunjene=true;
                    break;
                }
            }
            if(!imaLiPopunjene)return false;
        }
        
        int vrsta;
        int kolona;
        boolean zavrseno=false;
        
        synchronized(Locker.getLockZaAmbulante()){
            while(!zavrseno){
                int pozicija=RandomUtils.randomInt(4);  // strana: gore, dolje, lijevo, desno
                switch(pozicija){
                    case 0:
                        vrsta=0;
                        kolona=RandomUtils.randomInt(velicina-2)+1;
                        if(matrica[vrsta][kolona] instanceof Put){
                            Ambulanta a=new Ambulanta(vrsta, kolona);
                            ambulante.add(a);
                            matrica[vrsta][kolona]=a;
                            this.remove(vrsta*velicina+kolona);
                            this.add(a,vrsta*velicina+kolona);
                            this.revalidate();
                            zavrseno=true;
                        }break;
                    case 1:
                        vrsta=velicina-1;
                        kolona=RandomUtils.randomInt(velicina-2)+1;
                        if(matrica[vrsta][kolona] instanceof Put){
                            Ambulanta a=new Ambulanta(vrsta, kolona);
                            ambulante.add(a);
                            matrica[vrsta][kolona]=a;
                            this.remove(vrsta*velicina+kolona);
                            this.add(a,vrsta*velicina+kolona);
                            this.revalidate();
                            zavrseno=true;
                        }break;
                    case 2:
                        kolona=0;
                        vrsta=RandomUtils.randomInt(velicina-2)+1;
                        if(matrica[vrsta][kolona] instanceof Put){
                            Ambulanta a=new Ambulanta(vrsta, kolona);
                            ambulante.add(a);
                            matrica[vrsta][kolona]=a;
                            this.remove(vrsta*velicina+kolona);
                            this.add(a,vrsta*velicina+kolona);
                            this.revalidate();
                            zavrseno=true;
                        }break;
                    case 3:
                        kolona=velicina-1;
                        vrsta=RandomUtils.randomInt(velicina-2)+1;
                        if(matrica[vrsta][kolona] instanceof Put){
                            Ambulanta a=new Ambulanta(vrsta, kolona);
                            ambulante.add(a);
                            matrica[vrsta][kolona]=a;
                            this.remove(vrsta*velicina+kolona);
                            this.add(a,vrsta*velicina+kolona);
                            this.revalidate();
                            zavrseno=true;
                        }break;  
                }
            }
        } return true;
    }
  
}
