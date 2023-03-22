package domain

type User struct {
	Name     string `json:"name"`
	LastName string `json:"lastName,omitempty"`
	Email    string `json:"email,omitempty"`
}
