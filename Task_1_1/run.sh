rm -rf build docs jar
mkdir -p build docs jar
javac -d build app/src/main/java/HeapSort.java
javadoc -d docs app/src/main/java/HeapSort.java
jar -cfe jar/HeapSort.jar HeapSort -C build .
java -jar jar/HeapSort.jar