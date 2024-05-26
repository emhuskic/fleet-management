package org.trg.core.service;

import java.io.Serializable;

public interface MessageProducerService<T extends Serializable> {
   /**
    * Sends a message to Messaging Queue
    * @param message
    */
   void produce(T message);
}
