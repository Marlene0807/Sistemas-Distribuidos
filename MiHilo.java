//Thread

public class MiHilo extends Thread{
    @Override
    public void run(){
        for(int i=0; i<1000; i++){
            System.out.print("0-");
        }
    }

    public static void main(String [] args){
        MiHilo hilo = new MiHilo();
        hilo.start();
    }
}