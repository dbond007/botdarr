package com.botdarr.scheduling;

import com.botdarr.api.Api;
import com.botdarr.clients.ChatClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Scheduler {
  public static Scheduler getScheduler() {
    if (instance == null) {
      synchronized (Scheduler.class) {
        if (instance == null) {
          instance = new Scheduler();
        }
      }
    }
    return instance;
  }


  public void initApiNotifications(List<Api> apis, ChatClient chatClient) {
    if (notificationFuture == null) {
      notificationFuture = Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(() -> {
        try {
          for (Api api : apis) {
            api.sendPeriodicNotifications(chatClient);
          }
        } catch (Throwable e) {
          LOGGER.error("Error during api notification", e);
        }
      }, 0, 1, TimeUnit.HOURS);
    }
  }

  public void initApiCaching(List<Api> apis) {
    //cache initially
    for (Api api : apis) {
      api.cacheData();
    }

    //then cache on a schedule
    if (cacheFuture == null) {
      cacheFuture = Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(() -> {
        try {
          for (Api api : apis) {
            api.cacheData();
          }
        } catch (Throwable e) {
          LOGGER.error("Error during api cache", e);
        }
      }, 0, 2, TimeUnit.MINUTES);
    }
  }

  private ScheduledFuture notificationFuture;
  private ScheduledFuture cacheFuture;
  private static volatile Scheduler instance;
  private static final Logger LOGGER = LogManager.getLogger();
}
