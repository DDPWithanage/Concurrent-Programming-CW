import java.util.Random;

public class Student implements Runnable{
    private String name;
    private ThreadGroup threadGroup;
    private Printer printer;
    private Document document;
    private int docCount;
    private int noOfPages;

    public Student(String name, ThreadGroup group, Printer printer) {
        super();
        this.name = name;
        this.threadGroup = group;
        this.printer = printer;
    }

    @Override
    public void run(){
        Random random = new Random();
            // creating five instances to represent documents that the student request the printer to print

        //Create 5 instances of the Document
        for(int i = 0; i < 5; i++){
            //To get different lengths of documents generate a random number between 1 to 20 as the page count
            int randomPageCount = new Random().nextInt(20) + 1;

            String nameOfDoc = "document_" + (i + 1);
            document = new Document(this.name, nameOfDoc, randomPageCount);
            System.out.println("[STUDENT] - " + this.name + " has requested to print documents. Doc name : " + nameOfDoc +" and no of pages : " + randomPageCount);
            printer.printDocument(document);

            noOfPages += document.getNumberOfPages();
            docCount++;

            //Sleep for random amount of time between each printing request
            int randomDuration = random.nextInt(101);

            try {
                Thread.sleep(randomDuration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println(Thread.currentThread().getName() + " Finished Printing: " + docCount+ " Documents, " + noOfPages+ " pages");
    }
}
