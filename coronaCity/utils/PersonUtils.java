package coronaCity.utils;

public class PersonUtils {
    private static final int GODINA=2020;
    private static final int DOB_ODRASLI=18;
    private static final int DOB_STARI=65;
    public static final String MUSKO="musko";
    
    private static final String[] imenaMuska =
        { "Vlade", "Marko", "Nikola", "Goran", "Jovan", "Ognjen" };
    private static final String[] imenaZenska =
        { "Jelena", "Jovana", "Sandra", "Tanja", "Milica", "Jana" };
    private static final String[] prezimena = 
        { "Mikic", "Vulovic", "Tadic", "Jovanovic", "Stefanovic", "Markovic" };
    private static final String[] pol= {"musko", "zensko"};
    private static final String[] smjerKretanja= { "gore", "dolje", "lijevo", "desno"};
  
    public static String randomImeMusko() {
        return(RandomUtils.randomElement(imenaMuska));
    }
    
    public static String randomImeZensko() {
        return(RandomUtils.randomElement(imenaZenska));
    }
   
    public static String randomPrezime() {
        return(RandomUtils.randomElement(prezimena));
    }
  
    public static String randomPol() {
        return(RandomUtils.randomElement(pol));
    }
  
    public static String randomSmjerKretanja(int index) {
        return smjerKretanja[index];
    }
  
    public static int randomGodineOdrasli(){
        return(RandomUtils.randomInt(47)+DOB_ODRASLI);
    }
  
    public static int randomGodineDjeca(){
        return(RandomUtils.randomInt(18));
    }
  
    public static int randomGodineStari(){
        return(RandomUtils.randomInt(36)+DOB_STARI);
    }
  
    public static int randomGodiste(int brojGodina){
        return GODINA-brojGodina;
    }

    public static double randomTemperatura() {
        double pom=RandomUtils.randomDouble();      //pomocna varijabla
        if(pom<0.5){
            return 36.00+2*pom;    //vraca opseg 36-37
        }else{
            return 33.00+8*pom;     //vraca opseg 37-41
        }
    }

}
