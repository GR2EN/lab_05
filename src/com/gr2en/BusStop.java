package com.gr2en;

import java.util.concurrent.Semaphore;

public class BusStop {
  private static final int BUS_AMOUNT = 5;
  private static final Semaphore SEMAPHORE = new Semaphore(2, true);

  public static void main(String[] args) throws InterruptedException {
    for (int i = 0; i < BUS_AMOUNT; i++) {
      new Thread(new Bus(i + 1)).start();
      Thread.sleep(1000); // Выезд автобусов каждую секунду
    }
  }

  public static class Bus implements Runnable {
    private int busNumber;

    Bus(int busNumber) {
      this.busNumber = busNumber;
    }

    @Override
    public void run() {
      try {
        // Запрашиваем разрешение
        SEMAPHORE.acquire();

        System.out.printf("Автобус №%d подъехал на остановку.\n", busNumber);
        Thread.sleep(3000);

      } catch (InterruptedException e) {
        throw new Error();
      }

      // Освобождаем ранее запрошенное разрешение
      SEMAPHORE.release();
      System.out.printf("Автобус №%d покинул остановку.\n", busNumber);
    }
  }
}