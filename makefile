CC=gcc
CFLAGS = -g 
# uncomment this for SunOS
# LIBS = -lsocket -lnsl

all: udp-send udp-recv

udp-send: udp-send.o 
	$(CC) -o udp-send udp-send.o $(LIBS)

udp-recv: udp-recv.o 
	$(CC) -o udp-recv udp-recv.o $(LIBS)

udp-send.o: udp-send.c

udp-recv.o: udp-recv.c

.PHONY: clean

clean:
	-rm -f *.o
	-rm -f udp-recv
	-rm -f udp-send
