package coronaCity.utils;

import coronaCity.model.stanovnici.Stanovnik;

public class StatistickiPodaci {
    private static int ukupanBrojStanovnika;
    private static int brojOdraslih;
    private static int brojStarih;
    private static int brojDjece;
    
    private static int brZarazenihOdrasli=0;
    private static int brZarazenihStari=0;
    private static int brZarazenihDjeca=0;
    
    private static int brojMuskihZarazenih=0;
    private static int brojZenskihZarazenih=0;

    private static int brojPotvrdjenihSlucajeva=0;
    
    private static int brojZarazenih=0;
    private static int brojOporavljenih=0;

    public static int getUkupanBrojStanovnika() {
        return ukupanBrojStanovnika;
    }
    
    public static void postaviParametreStanovnistva(int brO, int brD, int brS){
        ukupanBrojStanovnika=brO+brD+brS;
        brojOdraslih=brO;
        brojStarih=brS;
        brojDjece=brD;
    }
    
    public synchronized static void obradiZarazenog(Stanovnik s){
        brojPotvrdjenihSlucajeva++;
        brojZarazenih++;
        
        if(PersonUtils.MUSKO.equals(s.getPol())){
            brojMuskihZarazenih++;
        }else{
            brojZenskihZarazenih++;
        }
        
        if(s.getBrojGodina()<18){
            brZarazenihDjeca++;
        }else if(s.getBrojGodina()<65){
            brZarazenihOdrasli++;
        }else{
            brZarazenihStari++;
        }
    }
    
    public synchronized static void obradiOporavljenog(Stanovnik s){
        brojZarazenih--;
        brojOporavljenih++;
        
        if(PersonUtils.MUSKO.equals(s.getPol())){
            brojMuskihZarazenih--;
        }else{
            brojZenskihZarazenih--;
        }
        
        if(s.getBrojGodina()<18){
            brZarazenihDjeca--;
        }else if(s.getBrojGodina()<65){
            brZarazenihOdrasli--;
        }else{
            brZarazenihStari--;
        }
        
    }

    public static int getBrojPotvrdjenihSlucajeva() {
        return brojPotvrdjenihSlucajeva;
    }

    public static int getBrojZarazenih() {
        return brojZarazenih;
    }

    public static int getBrojOporavljenih() {
        return brojOporavljenih;
    }
    
    public static double getProcenatZarazenih(){
        if(ukupanBrojStanovnika!=0){
            return (double)brojZarazenih/ukupanBrojStanovnika*100.00;
        }else return 0;
    }
    
    public static double getProcenatOporavljenih(){
        if(brojPotvrdjenihSlucajeva!=0){
            return (double)brojOporavljenih/brojPotvrdjenihSlucajeva*100.00;
        }else return 0;
    }
    
    public static double getProcenatMuskihZarazenih(){
        if(brojZarazenih!=0){
            return (double)brojMuskihZarazenih/brojZarazenih*100.00;
        }else return 0;
    }
    
    public static double getProcenatZenskihZarazenih(){
        if(brojZarazenih!=0){
            return (double)brojZenskihZarazenih/brojZarazenih*100.00;
        }else return 0; 
    }
    
    public static double getProcenatZarazenihOdrasli(){
        if(brojZarazenih!=0){
            return (double)brZarazenihOdrasli/brojZarazenih*100.00;
        }else return 0;
    }
    
    public static double getProcenatZarazenihDjece(){
        if(brojZarazenih!=0){
            return (double)brZarazenihDjeca/brojZarazenih*100.00;
        }else return 0;
    }
    
    public static double getProcenatZarazenihStari(){
        if(brojZarazenih!=0){
            return (double)brZarazenihStari/brojZarazenih*100.00;
        }else return 0;
    }
   
}
