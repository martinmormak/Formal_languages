CC=gcc
CFLAGS=-Wall -Wextra -std=c99
LDFLAGS=-lm
TESTS_DIR=tests

.PHONY: all clean test

all: parser test

parser: parser.o main.o
	$(CC) $(LDFLAGS) $^ -o $@

parser.o: parser.c parser.h
	$(CC) $(CFLAGS) -c $< -o $@

main.o: main.c
	$(CC) $(CFLAGS) -c $< -o $@

clean:
	rm -f parser parser.o main.o $(TESTS_DIR)/test_parser.o $(TESTS_DIR)/test_parser

test:
	gcc -Wall -Wextra -std=c99 -c parser.c -o parser.o
	gcc -Wall -Wextra -std=c99 -Itests -c tests/test_parser.c -o tests/test_parser.o
	gcc -lm tests/test_parser.o parser.o tests/greatest.h -o tests/test_parser
	./tests/test_parser
