package domain

type MsgRequest struct {
	User        User        `json:"user,omitempty"`
	Transaction Transaction `json:"transaction,omitempty"`
	Subject     string      `json:"subject,omitempty"`
	Body        string      `json:"body"`
}
