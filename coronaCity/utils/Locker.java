package coronaCity.utils;

public class Locker {
    
    private static final Object lock=new Object();
    private static final Object lockZaAlarme=new Object();
    private static final Object lockZaAmbulante=new Object();

    public static Object getLock() {
        return lock;
    }

    public static Object getLockZaAlarme() {
        return lockZaAlarme;
    }

    public static Object getLockZaAmbulante() {
        return lockZaAmbulante;
    }
    
    
}
