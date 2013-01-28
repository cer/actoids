package net.chrisrichardson.asyncpojos.actoids.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;

import org.springframework.core.task.AsyncTaskExecutor;

public class Actoid {

  private final AsyncTaskExecutor executorService;
  private final Mailbox mailbox;
  private final ActoidRequestExecutor requestExecutor;
  
  public Actoid(Object actoidImplementation, ActoidSystem actoidSystem) {
    this.executorService = actoidSystem.getExecutorService();
    this.requestExecutor = new ActoidRequestExecutor(actoidImplementation, actoidSystem);
    this.mailbox = new Mailbox(requestExecutor);
  }

  public void handle(ActoidRequest request) {
    if (mailbox.queue(request)) {
      try {
        executorService.submit(mailbox);
      } catch (RejectedExecutionException e) {
        mailbox.setIdle();
        throw e;
      }
    } else {

    }
  }

  public void setSelf(Object self) {
    this.requestExecutor.setSelf(self);
  }


  public BlockingQueue<ActoidRequest> getMailboxQueue() {
    return mailbox.getQueue();
  }


  public void setMailboxQueue(BlockingQueue<ActoidRequest> sharedQueue) {
    mailbox.setQueue(sharedQueue);
  }
  
}
