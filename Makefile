.PHONY: all doc clean watch

all: 	doc

doc:
	bb build.clj

deploy: doc
	aws s3 cp --acl public-read target/index.html s3://rest-guide/index.html
	aws s3 cp --acl public-read target/README.html s3://rest-guide/README.html

watch:
	ls README.adoc | entr make

clean:
	rm -r target
