package domain

type Transaction struct {
	Id         string `json:"uuid"`
	Origin     string `json:"originAccountNumber"`
	CardEnding string `json:"cardEnding,omitempty"`
	Recipient  string `json:"recipientAccountNumber"`
	Amount     string `json:"transactionAmount"`
	Date       string `json:"transactionDate"`
	Type       string `json:"transactionType"`
}
