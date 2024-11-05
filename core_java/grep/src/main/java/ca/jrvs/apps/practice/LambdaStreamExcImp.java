package ca.jrvs.apps.grep;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LambdaStreamExcImp implements LambdaStreamExc {

    @Override
    public Stream<String> createStrStream(String... strings) {
        return Stream.of(strings);
    }

    @Override
    public Stream<String> toUpperCase(String... strings) {
        return Stream.of(strings).map(String::toUpperCase);
    }

    @Override
    public Stream<String> filter(Stream<String> stringStream, String pattern) {
        return stringStream.filter(s -> s.contains(pattern));
    }

    @Override
    public IntStream createIntStream(int[] arr) {
        return IntStream.of(arr);
    }

    @Override
    public <E> List<E> toList(Stream<E> stream) {
        return stream.collect(Collectors.toList());
    }

    @Override
    public List<Integer> toList(IntStream intStream) {
        return intStream.boxed().collect(Collectors.toList());
    }

    @Override
    public IntStream createIntStream(int start, int end) {
        return IntStream.rangeClosed(start, end);
    }

    @Override
    public DoubleStream squareRootIntStream(IntStream intStream) {
        return intStream.mapToDouble(Math::sqrt);
    }

    @Override
    public IntStream getOdd(IntStream intStream) {
        return intStream.filter(n -> n % 2 != 0);
    }

    @Override
    public Consumer<String> getLambdaPrinter(String prefix, String suffix) {
        return message -> System.out.println(prefix + message + suffix);
    }

    @Override
    public void printMessage(String[] messages, Consumer<String> printer) {
        Stream.of(messages).forEach(printer);
    }

    @Override
    public void printOdd(IntStream intStream, Consumer<String> printer) {
        intStream.filter(n -> n % 2 != 0)
                .forEach(n -> printer.accept(String.valueOf(n)));
    }

    @Override
    public Stream<Integer> flatNestedInt(Stream<List<Integer>> ints) {
        return ints.flatMap(List::stream).map(n -> n * n);
    }

    public static void main(String[] args) {
        LambdaStreamExcImp lse = new LambdaStreamExcImp();

        // Test createStrStream and toUpperCase
        Stream<String> strStream = lse.createStrStream("hello", "world", "java");
        strStream.forEach(System.out::println); // Outputs: hello, world, java

        // Recreate the stream for the next operation
        strStream = lse.createStrStream("hello", "world", "java");
        Stream<String> upperStrStream = lse.toUpperCase("hello", "world", "java");
        upperStrStream.forEach(System.out::println); // Outputs: HELLO, WORLD, JAVA

        // Recreate the stream for the next operation
        strStream = lse.createStrStream("hello", "world", "java");
        // Test filter
        Stream<String> filteredStream = lse.filter(strStream, "o");
        filteredStream.forEach(System.out::println); // Outputs: hello, world

        // Test createIntStream and getOdd
        IntStream intStream = lse.createIntStream(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
        IntStream oddStream = lse.getOdd(intStream);
        oddStream.forEach(System.out::println); // Outputs: 1, 3, 5, 7, 9

        // Test squareRootIntStream
        IntStream intStream2 = lse.createIntStream(1, 5);
        DoubleStream sqrtStream = lse.squareRootIntStream(intStream2);
        sqrtStream.forEach(System.out::println); // Outputs: 1.0, 1.4142135623730951, 1.7320508075688772, 2.0,
                                                 // 2.23606797749979

        // Test getLambdaPrinter and printMessage
        Consumer<String> printer = lse.getLambdaPrinter("start>", "<end");
        lse.printMessage(new String[] { "hello", "world" }, printer);
        // Outputs:
        // start>hello<end
        // start>world<end

        // Test flatNestedInt
        Stream<List<Integer>> nestedIntStream = Stream.of(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9));
        Stream<Integer> flatStream = lse.flatNestedInt(nestedIntStream);
        flatStream.forEach(System.out::println); // Outputs: 1, 4, 9, 16, 25, 36, 49, 64, 81
    }
}