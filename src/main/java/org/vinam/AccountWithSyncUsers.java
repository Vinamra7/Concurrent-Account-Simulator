package org.vinam;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


//using lock and sync between threads but still not condition between threads
public class AccountWithSyncUsers {

    private static final UserAccount userAccount = new UserAccount();

    public static void main(String[] args){

        try(ExecutorService executorService = Executors.newCachedThreadPool()) {
            for(int i=0;i<100;i++)
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
        }
        System.out.println("Current Balance: " + userAccount.getBalance());

    }

    private static class AddMoney implements Runnable{

        @Override
        public void run(){
            synchronized (userAccount){
                userAccount.addBalance(1);
            }
        }
        /*OR we can have synchronized method

            public synchronized void run(){
                userAccount.addBalance(1);
            }

       */
    }

    private static class UserAccount{
        private static final Lock lock = new ReentrantLock(true);
        private int balance;

        public void addBalance(int amount){
            lock.lock();
            try {balance += amount;}
            finally {lock.unlock();}
        }

        public int getBalance(){ return balance; }
    }

}
