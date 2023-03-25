import java.util.Random;

public class TonerTechnician implements Runnable{
    private String name;
    private ThreadGroup threadGroup;
    private ServicePrinter printer;
    private int replaceCartridgeCount;

    public TonerTechnician(String name, ThreadGroup group, ServicePrinter printer) {
        super();
        this.name = name;
        this.threadGroup = group;
        this.printer = printer;
    }

    //Attempt to replace the toner cartridge
    @Override
    public void run(){
        Random random = new Random();
        int noOfAttemptsToRefill = 3;

        for(int i=0; i < noOfAttemptsToRefill; i++){
            printer.replaceTonerCartridge();
            System.out.println("[TONER TECHNICIAN] - Printer status after replace the toner cartridge " + Thread.currentThread().getName()+ ": "+ printer.toString());
            replaceCartridgeCount++;

            int randomDuration = random.nextInt(101);
            try {
                Thread.sleep(randomDuration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println("Toner Technician Finished, cartridges replaced: "+ replaceCartridgeCount);

    }
}
