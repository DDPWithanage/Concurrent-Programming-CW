import java.util.Random;

public class PaperTechnician extends Thread{
    private String name;
    private ThreadGroup threadGroup;
    private ServicePrinter printer;
    private int usedPaperPackCount;

    public PaperTechnician( String name, ThreadGroup group, ServicePrinter printer){
        super();
        this.name = name;
        this.threadGroup = group;
        this.printer = printer;

    }

  //Attempt to refill the paper tray
    @Override
    public void run(){
        Random random = new Random();
        int noOfAttemptsToRefill = 3;

        for(int i=0; i < noOfAttemptsToRefill; i++){
            printer.refillPaper();
            System.out.println("[PAPER TECHNICIAN] - Printer status after refill the paper tray " + Thread.currentThread().getName()+ ": "+ printer.toString());
            usedPaperPackCount++;

            int randomDuration = random.nextInt(101);
            try {
                Thread.sleep(randomDuration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Paper Technician Finished, packs of paper used: "+ usedPaperPackCount);
        }



    }
}
