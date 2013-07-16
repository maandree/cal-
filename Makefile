# Copying and distribution of this file, with or without modification,
# are permitted in any medium without royalty provided the copyright
# notice and this notice are preserved.  This file is offered as-is,
# without any warranty.


SRC = Program Schedule


all:
	@mkdir -p bin
	javac -s src -d bin $(foreach FILE, $(SRC), "src/$(FILE).java")


clean:
	-rm -r bin 2>/dev/null


.PHONY: clean all

