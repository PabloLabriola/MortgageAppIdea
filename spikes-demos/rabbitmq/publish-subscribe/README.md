# RabbitMQ Hello World

Source:  https://www.rabbitmq.com/tutorials/tutorial-three-spring-amqp.html



In this part we'll implement the fanout pattern to deliver a message to multiple consumers. This pattern is also known as "publish/subscribe" and is implemented by configuring a number of beans in our config file.

Essentially, published messages are going to be broadcast to all the receivers.

##### Exchanges
In previous parts of the tutorial we sent and received messages to and from a queue. Now it's time to introduce the full messaging model in RabbitMQ.

Let's quickly go over what we covered in the previous tutorials:

* A producer is a user application that sends messages.
* A queue is a buffer that stores messages.
* A consumer is a user application that receives messages.

The core idea in the messaging model in RabbitMQ is that the producer never sends any messages directly to a queue. Actually, quite often the producer doesn't even know if a message will be delivered to any queue at all.

Instead, the producer can only send messages to an exchange. An exchange is a very simple thing. On one side it receives messages from producers, and the other side it pushes them to queues. The exchange must know exactly what to do with a message it receives. Should it be appended to a particular queue? Should it be appended to many queues? Or should it get discarded. The rules for that are defined by the exchange type.

There are a few exchange types available: direct, topic, headers and fanout. We'll focus on the last one -- the fanout. We'll configure a bean to describe an exchange of this type.
