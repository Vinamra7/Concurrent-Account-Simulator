package org.vinam;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// When multiple thread update a location at simultaneously we get inconsistency.
public class AccountWithoutSyncUsers {
    private static final UserAccount user = new UserAccount();
    public static void main(String[] args){

        //thread pool to manage all threads
        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i=0;i<100;i++){
            executorService.execute(new AddMoney());
        }
        executorService.shutdown(); // ask all thread to shut down
        while (!executorService.isShutdown()) {}
        /* waiting the last remaining threads to shut down,
        but we keep checking the status causing unnecessary CPU usage the better way is shown in main2
        */
        System.out.print("Balance: " + user.getBalance());
    }

    //proper way to handle
    public static void main2(){
        try (ExecutorService executorService = Executors.newCachedThreadPool()){
            for (int i = 0; i < 100; i++)
                executorService.execute(new AddMoney());

            executorService.shutdown();
            try {
                if(!executorService.awaitTermination(20, TimeUnit.SECONDS))
                    executorService.shutdownNow();
            }
            catch (InterruptedException Iex){
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }catch (Exception ex){
            System.out.print(Arrays.toString(ex.getStackTrace()));
        }

    }

    private static class AddMoney implements Runnable{
        @Override
        public void run(){
            user.updateBalance(1);
        }
    }

    private static class UserAccount {
        private int balance;
        public void updateBalance(int adder){
            balance += adder;
            try {
                Thread.sleep(1);
            }
            catch (InterruptedException Iex){
                System.out.print(Arrays.toString(Iex.getStackTrace()));
            }
        }
        public int getBalance(){ return balance;}
    }
}
