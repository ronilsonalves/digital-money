FROM bitnami/golang
WORKDIR /go/src/app
COPY . .
RUN go mod download
RUN go mod tidy
COPY .env /cmd/server/
RUN go build -o app ./cmd/server/main.go
EXPOSE 8090
CMD ["./app"]