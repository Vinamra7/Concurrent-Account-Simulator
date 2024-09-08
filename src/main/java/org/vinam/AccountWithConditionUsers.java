package org.vinam;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AccountWithConditionUsers {

    private static final UserAccount userAccount = new UserAccount();

    public static void main(String[] args){

        try(ExecutorService executorService = Executors.newFixedThreadPool(2)){
            executorService.execute(new AddMoney());
            executorService.execute(new RemoveMoney());
            executorService.shutdown(); // immediately call shutDown so it will go to .awaitTermination
            try {
                if(!executorService.awaitTermination(60, TimeUnit.SECONDS)) //program will run for 1 minute
                    executorService.shutdownNow();

            }catch (InterruptedException ex){
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

    }

    //thread 1
    private static class AddMoney implements Runnable{

        @Override
        public synchronized void run(){
            try {
                while (true){
                    userAccount.addMoney((int) (Math.random() * 10) + 1);
                    Thread.sleep(2000);
                }
            }
            catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }

    //thread 2
    private static class RemoveMoney implements Runnable{

        @Override
        public synchronized void run(){
            try {
                while(true){
                    userAccount.removeMoney((int)(Math.random() * 10) + 1);
                    Thread.sleep(2000);
                }
            }
            catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }

    }

    private static class UserAccount{
        private int balance = 0;
        private static final Lock lock = new ReentrantLock(true);
        private static final Condition condition = lock.newCondition();
        public void addMoney(int amount){
            lock.lock();
            try{
                balance += amount;
                System.out.println("Added:   " + amount + "\t\tCurrent balance: " + getBalance());
                condition.signalAll();
            }
            finally {lock.unlock();}
        }

        public void removeMoney(int amount){
            lock.lock();
            try {
                while (balance < amount){
                    System.out.println("Insufficient Balance will removing: " + amount);
                    condition.await();
                }
                balance -= amount;
                System.out.println("Removed: "+ amount + "\t\tCurrent balance: " + getBalance());
            }
            catch (InterruptedException ex){ex.printStackTrace();}
            finally {
                lock.unlock();
            }
        }

        public int getBalance(){ return balance;}
    }

}
