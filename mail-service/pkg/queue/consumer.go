package queue

import (
	"encoding/json"
	gorabbitmq "github.com/hadihammurabi/go-rabbitmq"
	"github.com/hadihammurabi/go-rabbitmq/exchange"
	"log"
	"mail-service/internal/domain"
	m "mail-service/internal/msg"
	"os"
)

func failOnError(err error, msg string) {
	if err != nil {
		log.Fatalf("%s: %s", msg, err)
	}
}

func Connect(urlConn, name string) (gorabbitmq.MQ, error) {
	mq, err := gorabbitmq.New(urlConn)
	failOnError(err, "Failed to create a MQ")

	err = mq.Exchange().
		WithName(name).
		WithType(exchange.TypeDirect).
		Declare()
	failOnError(err, "Failed to create a channel")

	q, err := mq.Queue().
		WithName(name).
		Declare()
	failOnError(err, "Failed to create a queue")

	err = q.Binding().
		WithExchange(name).
		Bind()
	failOnError(err, "Failed to bind queue")

	return *mq, nil
}

func Consume() {
	name := "mail-service"
	mq, err := Connect(os.Getenv("RABBIT_MQ_URL_CONN"), "mail-service")
	if err != nil {
		log.Println("Error establishing a connection with RabbitMQ: ", err)
	}
	messages, err := mq.Channel().Consume(name, name, true, false, false, false, nil)
	if err != nil {
		log.Fatalf("Failed to register a consumer: %s", err)
	}
	forever := make(chan bool)
	go func() {
		for msg := range messages {
			msgRequest := domain.MsgRequest{}
			err := json.Unmarshal(msg.Body, &msgRequest)
			if err == nil {
				log.Println("Sending msg...")
				m.SendMsg(msgRequest)
				log.Println("Message sent!")
			} else {
				log.Println("error converting msg received: ", err, msgRequest)
			}
		}
	}()
	<-forever
}
