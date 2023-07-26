#!/bin/sh
#
#A precommit hook to render the plantuml for the documents
#
#
#
if ! command -v plantuml &> /dev/null
then
	echo "plantUML not installed. install it with brew install plantuml"
else
	echo "rendering images from markdown..."
	plantuml -tsvg ./docs/*.md
	git add ./docs/*.svg
fi


