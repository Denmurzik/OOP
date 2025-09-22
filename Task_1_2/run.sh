rm -rf build docs jar

mkdir -p build docs jar

javac -d build -encoding UTF-8 app/src/main/java/*.java

javadoc -d docs -encoding UTF-8 -charset UTF-8 -docencoding UTF-8 app/src/main/java/*.java

jar -cfe jar/Blackjack.jar Main -C build .

java -jar jar/Blackjack.jar