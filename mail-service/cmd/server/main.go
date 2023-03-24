package main

import (
	"github.com/gin-gonic/gin"
	"github.com/joho/godotenv"
	"log"
	"mail-service/pkg/queue"
	"mail-service/pkg/sd"
	"os"
	"os/signal"
	"time"
)

func main() {

	err := godotenv.Load()
	if err != nil {
		log.Fatalln("Error loading .env file", err.Error())
	}

	er := sd.BuildFargoInstance()
	er.Register()

	c := make(chan os.Signal)
	signal.Notify(c, os.Interrupt)

	r := gin.New()
	r.Use(gin.Recovery(), gin.Logger())

	//r.Use(middleware.IsAuthorizedJWT())

	r.GET("/ping", func(ctx *gin.Context) {
		ctx.JSON(200, gin.H{
			"message": "pong",
		})
	})

	queue.Consume()

	go func() {
		select {
		case signal := <-c:
			_ = signal
			time.Sleep(2 * time.Second)
			er.Deregister()
			os.Exit(1)
		}
	}()

	r.Run("localhost:8090")
}
