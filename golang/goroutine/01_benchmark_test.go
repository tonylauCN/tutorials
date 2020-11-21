package goroutine

import (
	"fmt"
	"sync"
	"testing"
)

func BenchmarkContextSwitch(b *testing.B) {

	var wg sync.WaitGroup
	begin := make(chan struct{})
	c := make(chan struct{})

	var token struct{}
	sender := func() {
		defer wg.Done()
		<-begin
		for i := 0; i < b.N; i++ {
			c <- token
		}
	}

	receiver := func() {
		defer wg.Done()
		<-begin
		for i := 0; i < b.N; i++ {
			<-c
		}
	}

	wg.Add(2)
	go sender()
	go receiver()
	fmt.Printf("-> %d\n", b.N)
	b.StartTimer()
	// trigger 2 goroutine start
	close(begin)
	wg.Wait()
}
