import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LaserPrinter implements ServicePrinter{

    private String name;
    private String id;
    private int currentPaperLevel;
    private int currentTonerLevel;
    private int numOfDocsPrinted;
    private ThreadGroup threadGroup;

    //Create the reentrent lock that used to provide mutual exclusiveness to the all the threads (acquire the lck)
    ReentrantLock lock = new ReentrantLock(true);
    Condition lockCondition = lock.newCondition();

    public LaserPrinter(String name, String id, int currentPaperLevel, int currentTonerLevel, ThreadGroup threadGroup) {
        this.name = name;
        this.id = id;
        this.currentPaperLevel = currentPaperLevel;
        this.currentTonerLevel = currentTonerLevel;
        this.numOfDocsPrinted = 0;
        this.threadGroup = threadGroup;
    }

    @Override
    public void printDocument(Document document) {
        lock.lock();
        try {
            int pageCount = document.getNumberOfPages();
            //Check whether the resources are available before printing the document.
            while(currentPaperLevel < pageCount || currentTonerLevel < pageCount){
                if(currentPaperLevel < pageCount){
                    System.out.println("[PRINTER] - Current paper level : " + currentPaperLevel + ". Out of papers. Document cannot be printed. Have to wait until the papers " +
                            "are refilled.");
                }else if(currentTonerLevel < pageCount){
                    System.out.println("[PRINTER] - Current toner level : " + currentTonerLevel + ". Out of toner. Document cannot be printed. Have to wait until the toner " +
                            "cartridge is replaced.");

                }else{
                    System.out.println("[PRINTER] - Current paper level : " + currentPaperLevel + ", Current toner level : " + currentTonerLevel + ". Out of both papers and " +
                            "toners, Have to wait until they become available.");
                }

                lockCondition.await(); //wait until the printing completed.
            }
            //Print the document of the resources are available
            if (currentTonerLevel >= pageCount && currentPaperLevel >= pageCount) {
                currentTonerLevel -= pageCount;
                currentPaperLevel -= pageCount;
                numOfDocsPrinted++;
                //System.out.println(Thread.currentThread().getName()+ " document has been printed. Finished printing "+ numOfDocsPrinted + " documents. " +document);
            }
            lockCondition.signalAll(); //Notify both toner technician and the student

        } catch (InterruptedException e) {
            e.printStackTrace();
        }  finally {
            lock.unlock();
        }
    }


    @Override
    public void replaceTonerCartridge() {
        try {
            lock.lock();
            //check whether the toner needs to be replaced.
            while (currentTonerLevel >= Minimum_Toner_Level) {
                //printing can be completed if toner level is greater or equal to the minimum toner level.
                if (isPrintingCompleted()) {
                    System.out.println("[PRINTER] - Current Toner Level: " + currentTonerLevel + " Printing completed. No need to replace the toner cartridge.");
                    break;
                }else {
                    //wait for 5 seconds before checking if toner cartridge need to be refilled again.
                    System.out.println("[PRINTER] - Current toner level : " +currentTonerLevel+ " => So, minimum toner level has not reached to replace the toner " +
                            "cartridge. Waiting for check again.");
                    lockCondition.await(5000, TimeUnit.MILLISECONDS);
                }
            }

            //if current toner level is less than minimum toner level toner cartridge should be replaced.
            if(currentTonerLevel < Minimum_Toner_Level){
                currentTonerLevel += Full_Toner_Level;

                lockCondition.signalAll();
                System.out.println("[PRINTER] - Toner cartridge has been successfully replaced. New Toner level : "+ currentTonerLevel);
            }


        } catch (InterruptedException e) {
            e.printStackTrace();

        }finally{
            lock.unlock();
        }

    }

    @Override
    public void refillPaper() {
        try{
            lock.lock();
            //check whether the papers needs to be refilled.
            while((currentPaperLevel + SheetsPerPack) > Full_Paper_Tray){
                //printing can be completed if paper count is greater than full paper tray amount.
                if (isPrintingCompleted()) {
                    System.out.println("[PRINTER] - Current Paper Level: " + currentPaperLevel + "Printing completed. No need to refill papers");
                    break;
                }else {
                    //wait for 5 seconds before checking if papers needs to be refilled again.
                    System.out.println("[PRINTER] - Current paper level : " +currentPaperLevel+ " => So, no need to refill papers at the moment. Waiting for check " +
                            "again.");

                    lockCondition.await(5000, TimeUnit.MILLISECONDS);
                }
            }

            //if paper count is less than full paper tray amount papers should be refilled.
            if((currentPaperLevel + SheetsPerPack) <= Full_Paper_Tray){
                currentPaperLevel += SheetsPerPack;
                System.out.println("[PRINTER] - Papers has been refilled successfully. New paper level is " + currentPaperLevel);
                lockCondition.signalAll();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally{
            lock.unlock();
        }

    }

    @Override
    public String toString() {
        //[ PrinterID: lp-CG.24, Paper Level: 35, Toner Level: 310, Documents Printed: 4 ]
        return "[PrinterID: " + name +  "." + id + " , " + "Paper Level: "+ currentPaperLevel + " , " + "Toner Level: " +
                currentTonerLevel + " , " + " Documents Printed: " + numOfDocsPrinted + "]";

    }

    private boolean isPrintingCompleted() {
        return threadGroup.activeCount() == 0;
    }
}
