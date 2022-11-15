# RabbitMQ Hello World

Source:  https://www.rabbitmq.com/tutorials/tutorial-four-spring-amqp.html



In the previous tutorial we built a simple fanout exchange. We were able to broadcast messages to many receivers.

In this tutorial we're going to add a feature to it - we're going to make it possible to subscribe only to a subset of the messages. For example, we will be able to direct only messages to the certain colors of interest ("orange", "black", "green"), while still being able to print all of the messages on the console.

##### Bindings
In previous examples we were already creating bindings. You may recall code like this in our Config file:

```text
@Bean
public Binding binding1(FanoutExchange fanout, 
    Queue autoDeleteQueue1) {
    return BindingBuilder.bind(autoDeleteQueue1).to(fanout);
}
```


A binding is a relationship between an exchange and a queue. This can be simply read as: the queue is interested in messages from this exchange.

Bindings can take an extra binding key parameter. Spring AMQP uses a fluent API to make this relationship very clear. We pass in the exchange and queue into the BindingBuilder and simply bind the queue "to" the exchange "with a binding key" as follows:

```text
@Bean
public Binding binding1a(DirectExchange direct, 
    Queue autoDeleteQueue1) {
    return BindingBuilder.bind(autoDeleteQueue1)
        .to(direct)
        .with("orange");
}
```

The meaning of a binding key depends on the exchange type. The fanout exchanges, which we used previously, simply ignored its value.
