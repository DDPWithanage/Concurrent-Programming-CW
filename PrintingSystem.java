public class PrintingSystem {

    public static void main (String[] args) throws InterruptedException {
        ThreadGroup studentThreadGroup = new ThreadGroup("Students");
        ThreadGroup technicianThreadGroup = new ThreadGroup("Technicians");

        ServicePrinter servicePrinter = new LaserPrinter("EPSON", "101",250, 500, studentThreadGroup);

        Student studentRunnable_1 = new Student("Alison", studentThreadGroup, servicePrinter);
        Student studentRunnable_2 = new Student("Mike", studentThreadGroup, servicePrinter);
        Student studentRunnable_3 = new Student("Harry", studentThreadGroup, servicePrinter);
        Student studentRunnable_4 = new Student("Jimmy", studentThreadGroup, servicePrinter);

        Thread studentThread_1 = new Thread(studentThreadGroup, studentRunnable_1, "Alison");
        Thread studentThread_2 = new Thread(studentThreadGroup, studentRunnable_2, "Mike");
        Thread studentThread_3 = new Thread(studentThreadGroup, studentRunnable_3, "Harry");
        Thread studentThread_4 = new Thread(studentThreadGroup, studentRunnable_4, "Jimmy");

        Runnable paperTechnicianRunnable = new PaperTechnician("PaperTech", technicianThreadGroup, servicePrinter);
        Runnable tonerTechnicianRunnable = new TonerTechnician("TonerTech", technicianThreadGroup, servicePrinter);

        Thread paperTechnicianThread = new Thread(technicianThreadGroup, paperTechnicianRunnable, "Paper Technician");
        Thread tonerTechnicianThread = new Thread(technicianThreadGroup, tonerTechnicianRunnable, "Toner Technician");

        studentThread_1.start();
        studentThread_2.start();
        studentThread_3.start();
        studentThread_4.start();

        paperTechnicianThread.start();
        tonerTechnicianThread.start();

        studentThread_1.join();
       // System.out.println(studentThread_1.getName()+ " completed execution");
        studentThread_2.join();
       // System.out.println(studentThread_2.getName()+ " completed execution");
        studentThread_3.join();
        //System.out.println(studentThread_3.getName()+ " completed execution");
        studentThread_4.join();
       // System.out.println(studentThread_4.getName()+ " completed execution");
        paperTechnicianThread.join();
       // System.out.println(paperTechnicianThread.getName()+ " completed execution");
        tonerTechnicianThread.join();
       // System.out.println(tonerTechnicianThread.getName()+ " completed execution");

        System.out.println("\nAll tasks are completed. Printing the summary  ...\n");

        System.out.print("==================================================\n" +
                "                PRINTER SUMMARY                  \n" +
                "=================================================\n");
        System.out.println(servicePrinter.toString());
    }
}
