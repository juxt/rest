.PHONY: all doc clean watch

all: 	doc

doc:
	bb build.clj

watch:
	ls README.adoc | entr make

clean:
	rm -r target
