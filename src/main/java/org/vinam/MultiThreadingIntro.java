package org.vinam;

// Two threads will run simultaneously generate random output of which two choose.
public class MultiThreadingIntro {

    public static void main(String[] args){
        Runnable printNum = new PrintNum(5, 10);
        Runnable printChar = new PrintChar('v', 10);

        Thread threadNum = new Thread(printNum);
        Thread threadChar = new Thread(printChar);

        threadNum.start(); threadChar.start();
    }

}

class PrintNum extends Thread{
    private int num;
    private int times;

    @Override
    public void run(){
        int s_times = times;
        while (s_times-- > 0) System.out.print(num);
    }
    public PrintNum(int num,int times){
        this.num = num;
        this.times = times;
    }
}

class PrintChar implements Runnable{

    private char character;
    private int times;

    @Override
    public void run() {
        int s_times = times;
        while (s_times-- > 0) System.out.print(character);
    }

    public PrintChar(char c, int t){
        this.character = c;
        this.times = t;
    }
}
