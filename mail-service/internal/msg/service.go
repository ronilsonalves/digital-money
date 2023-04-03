package msg

import (
	"github.com/go-mail/mail"
	"log"
	"mail-service/internal/domain"
	"os"
)

type Service interface {
	SendMsg(msg domain.MsgRequest)
}

func SendMsg(msg domain.MsgRequest) {
	m := mail.NewMessage()
	m.SetHeader("From", "notify@digitalmoney.com")
	m.SetHeader("To", msg.User.Email)
	m.SetHeader("Subject", msg.Subject)
	m.SetBody("text/html", msg.Body)
	d := mail.NewDialer(
		os.Getenv("SMTP_HOST"),
		587,
		os.Getenv("SMTP_USERNAME"),
		os.Getenv("SMTP_PASSWORD"))
	if err := d.DialAndSend(m); err != nil {
		log.Printf("Error while trying to send msg: %s\n", err)
	}
}
