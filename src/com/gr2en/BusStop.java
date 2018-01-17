package com.gr2en;

import java.util.concurrent.Semaphore;

public class BusStop {
  // Место на остановке занято - true, свободно - false
  private static final boolean[] STOPPING_PLACES = new boolean[2];
  private static final Semaphore SEMAPHORE = new Semaphore(2, true);

  public static void main(String[] args) throws InterruptedException {
    for (int i = 1; i <= 5; i++) {
      new Thread(new Bus(i)).start();
      Thread.sleep(2000); // Выезд автобусов каждые 2 секунды
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
        SEMAPHORE.acquire();  // Запрашиваем разрешение

        int stoppingNumber = -1;

        // Ожидание свободного места на остановке
        synchronized (STOPPING_PLACES){
          for (int i = 0; i < 2; i++)
            if (!STOPPING_PLACES[i]) {  // Если место свободно
              STOPPING_PLACES[i] = true;  // подъезжаем за пассажирами
              stoppingNumber = i;
              System.out.printf("Автобус №%d подъехал на остановку.\n", busNumber);
              break;
            }
        }

        Thread.sleep(3000); // Время посадки пассажиров

        synchronized (STOPPING_PLACES) {
          STOPPING_PLACES[stoppingNumber] = false; // Освобождаем место
        }

        SEMAPHORE.release();  // Освобождаем ранее запрошенное разрешение
        System.out.printf("Автобус №%d покинул остановку.\n", busNumber);
      } catch (InterruptedException e) {
        throw new Error();
      }
    }
  }
}