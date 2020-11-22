package goroutine

import (
	"bytes"
	"fmt"
	"math/rand"
	"os"
	"sync"
	"testing"
	"time"
)

func TestBufferChannel(b *testing.T) {

	var wg sync.WaitGroup
	var stdoutBuff bytes.Buffer
	defer stdoutBuff.WriteTo(os.Stdout)

	wg.Add(2)
	instream := make(chan int, 4)
	go func() {
		defer wg.Done()
		defer close(instream)
		defer fmt.Fprintln(&stdoutBuff, "Producer Done.")
		for i := 0; i < 5; i++ {
			fmt.Fprintf(&stdoutBuff, "Sending: %d\n", i)
			instream <- i
		}
	}()

	go func() {
		defer wg.Done()
		for integer := range instream {
			fmt.Fprintf(&stdoutBuff, "Received %v.\n", integer)
		}
		//for {
		//	select {
		//	case integer := <-instream:
		//		fmt.Fprintf(&stdoutBuff, "Received %v.\n", integer)
		//		//fmt.Printf("Received! %v.\n", integer)
		//	default:
		//	}
		//}
	}()

	defer fmt.Println("-> 1")
	defer fmt.Println("-> 2")
	defer fmt.Println("-> 3")
	wg.Wait()
}

func TestSelectChannel(t *testing.T) {

	c1 := make(chan interface{})
	close(c1)
	c2 := make(chan interface{})
	close(c2)

	var c1Count, c2Count int
	for i := 1000; i > 0; i-- {
		select {
		case <-c1:
			c1Count++
		case <-c2:
			c2Count++
			//case <-time.After(1 * time.Second):
			//	fmt.Printf("Time out.\n")
			//	break
		}
	}
	fmt.Printf("c1:%d - c2:%d", c1Count, c2Count)
}

func TestReadDoneWork(t *testing.T) {

	doWork := func(
		done <-chan interface{},
		strings <-chan string) <-chan interface{} {
		terminated := make(chan interface{})
		go func() {
			defer fmt.Println("doWork exited.")
			defer close(terminated)
			for {
				select {
				case s := <-strings:
					fmt.Println(s)
				case <-done:
					return
				}
			}
		}()
		return terminated
	}
	done := make(chan interface{})
	terminated := doWork(done, nil)
	go func() {
		time.Sleep(10 * time.Second)
		fmt.Println("Canceling doWork goroutine...")
		close(done)
	}()
	<-terminated
	fmt.Println("Done.")
}

func TestWriteDoneWork(t *testing.T) {

	newRandomStream := func(done <-chan interface{}) <-chan int {
		randStream := make(chan int)
		go func() {
			defer fmt.Println("newRandStream closure exited.")
			defer close(randStream)
			for {
				select {
				case randStream <- rand.Int():
				case <-done:
					return
				}
			}
		}()
		return randStream
	}

	done := make(chan interface{})
	randStream := newRandomStream(done)
	fmt.Println("3 random ints:")

	for i := 0; i <= 3; i++ {
		fmt.Printf("%d: %d \n", i, <-randStream)
	}
	close(done)
	time.Sleep(1 * time.Second)
}
