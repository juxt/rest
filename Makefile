.PHONY: all doc clean watch

all: 	doc

doc:
	bb build.clj

deploy:
	aws s3 cp --acl public-read target/index.html s3://rest-guide/index.html
	aws s3 cp --acl public-read target/README-clojure-spin.html s3://rest-guide/README-clojure-spin.html

watch:
	ls README.adoc | entr make

clean:
	rm -r target
