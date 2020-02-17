## 5.1 필터링
+ `filter` : Predicate(boolean type)를 인수로 받아서 프레디케이트와 일치하는 모든 요소를 포함하는 스트림 반환
```
 List<Dish> vegetarianMenu = menu.stream()
                                 .filter(Dish::isVegetarian)
                                 .collect(toList());
```

+ `distinct` : 고유 요소로 이루어진 스트림 반환(고유 여부는 `hashCode`, `equals`로 결정)
```
 List<Integer> numbers = Arrays.asList(1,2,1,3,3,2,6);
 numbers.stream()
        .filter(i->i%2 == 0)
        .distinct()
        .forEach(System.out::println);
```
 
 
## 5.2 스트림 슬라이싱(Java 9)
+ `takewhile` : 무한 스트림을 포함한 모든 스트림에 프레디케이트를 적용해 스트림을 슬라이스 할 수 있다.
```
 List<Dish> slicedMenu1 = specialMenu.stream()
                                     .takeWhile(dish -> dish.getCalories() < 320)
                                     .collect(toList());
```

+ `dropwhile` : 프레디케이트가 처음으로 거짓이 되는 지점까지 발견된 요소를 버린다.
```
 List<Dish> slicedMenu2 = specialMenu.stream()
                                     .dropWhile(dish -> dish.getCalories() < 320)
                                     .collect(toList());
```

+ `limit(n)` : 주어진 값 이하의 크기를 갖는 새로운 스트림을 반환
  + 정렬되지 않은 스트림에도 limit 사용 가능(정렬되지 않은 상태로 반환됨)
```
 List<Dish> slicedMenu3 = specialMenu.stream()
                                     .filter(dish -> dish.getCalories() > 300)
                                     .limit(3)
                                     .collect(toList());
```

+ `skip(n)` : 처음 n개 요소를 제외한 스트림을 반환
```
 List<Dish> slicedMenu4 = specialMenu.stream()
                                     .filter(dish -> dish.getCalories() > 300)
                                     .skip(2)
                                     .collect(toList());
```

## 5.3 매핑
+ 스트림 각 요소에 함수 적용하기
  + 변환에 가까운 매핑 : 스트림은 함수를 인수로 받는 map 메서드를 지원한다. 인수로 제공된 함수는 각 요소에 적용되며 함수를 적용한 결과가 새로운 요소로 매핑된다.
```
// map 메서드의 출력 스트림은 Stream<String>
List<String> dishNames = menu.stream()
                             .map(Dish::getName)
                             .collect(toList());
```

+ `Arrays.stream` : 문자열을 받아 스트림을 만든다.
```
 String[] arrayOfWords = {"Hello", "World"};
 Stream<String> streamOfWords = Arrays.stream(arrayOfWords);
 
 words.stream()
      .map(word -> word.split("");
      .map(Arrays::stream)
      .distinct()
      .collect(toList();
```

+ `flapMap` : 각 배열을 스트림의 콘텐츠로 매핑하여 하나의 평면화된 스트림을 반환
```
 List<String> uniqueCharacters = words.stream()
                                      .map(word -> word.split("");
                                      .flatMap(Arrays::stream)
                                      .distinct()
                                      .collect(toList());
```

## 5.4 검색과 매칭
+ `anyMatch` : 프레디케이트가 주어진 스트림에서 적어도 한 요소와 일치하는지 확인. 최종 연산
```
 if(menu.stream().anyMatch(Dish::isVegetarian)
    System.out.println("The menu is (somewhat) vegetarian friendly!!");
```

+ `allMatch` : 스트림의 모든 요소가 주어진 프레디케이트와 일치하는지 검사
```
 boolean isHealthy = menu.stream()
                         .allMatch(dish -> dish.getCalories() < 1000);
```

+ `noneMatch` : 주어진 프레디케이트와 일치하는 요소가 없는지 확인
```
 boolean isHealthy = menu.stream()
                         .noneMatch(dish -> dish.getCalories() >= 1000);
```

+ 쇼트서킷 : 전체 스트림을 처리하지 않았더라도 결과를 반환하는 것
  + &&, ||, `allMatch`, `noneMatch`, `findFirst`, `findAny`...
  
+ `findAny` : 현재 스트림에서 임의의 요소를 반환. 다른 스트림 연산과 연결해서 사용할 수 있다
  + 스트림 파이프라인은 내부적으로 단일 과정으로 실행할 수 있도록 최적화된다
```
 Optional<Dish> dish = menu.stream()
                           .filter(Dish::isVegetarian)
                           .findAny();
```

+ `findFirst` : 현재 스트림에서 (논리적인 아이템 순서 중)첫번째 요소를 반환. 
```
 List<Integer> someNumbers = Arrays.asList(1,2,3,4,5);
 Optional<Integer> firstSquareDivisibleByThree 
 = someNumbers.stream()
              .map(n -> n*n)
              .filter(n -> n%3 == 0)
              .findFirst();
```

+ `Optional<T>` (java.util.Optional)
  + 값의 존재나 부재 여부를 표현하는 컨테이너 클래스
  + `findAny` 등의 메서드 사용시 아무 요소도 반환하지 않을 수 있다. 이런 경우들에 어떻게 처리할지 강제하는 기능을 제공
    + `isPresent()` : 값을 포함하는 true, 포함하지 않으면 false
    + `isPresent(Consumer<T> block)` : 값이 있으면 주어진 block 실행
    + `T get()` : 값이 존재하면 값을 반환, 값이 없으면 NoSuchElementException
    + `T orElse(T other)` : 값이 있으면 값을 반환, 값이 없으면 기본값을 반환
```
 menu.stream()
     .filter(Dish::isVegetarian)
     .findAny()     <- Optional<Dish> 반환
     .ifPresent(dish -> System.out.println(dish.getName());
```

## 5.5 리듀싱
+ 리듀싱 연산 : 모든 스트림 요소를 처리해서 값으로 처리하는 질의
+ `reduce`
  + 초깃값
  + 두 요소를 조합해서 새로운 값을 만드는 BinaryOperator<T>
```
 int product = numbers.stream().reduce(1, (a, b) -> a*b);
```
  + 자바8에서는 Integer 클래스에 두 숫자를 더하는 정적 `sum` 메소드 제공
  ```
   int sum = numbers.stream().reduce(0, Integer::sum);
  ```
+ 스트림 연산 : 상태 없음과 상태 있음
  + `map`, `filter` 등은 입력 스트림에서 각 요소를 받아 0 또는 결과를 출력 스트림으로 보낸다. 상태가 없는, 내부 상태를 갖지 않는 연산이다.
  + `reduce`, `sum`, `max` 같은 연산은 결과를 누적할 내부 상태가 필요하다. 스트림에서 처리하는 요소 수와 관계없이 내부 상태의 크기는 한정되어있다.
  
## 5.7 숫자형 스트림
+ 기본형 특화 스트림 : 스트림 api 숫자 스트림을 효율적으로 처리할 수 있도록 하는 스트림
  + 숫자 스트림으로 매핑(IntStream, DoubleStream, LongStream)
    + `reduce`와 같은 스트림에 숨어있는 박싱 비용을 피할 수 있도록 제공. 박싱 과정에서 일어나는 효율성과 관련 있으며 추가 기능을 제공하지는 않음
    + `mapToInt`, `mapToDouble`, `mapToLong`
    + `max`, `min`, `average` 등 다양한 유틸리티 메서드 지원
  + 객체 스트림으로 복원
    + `boxed` 메서드를 이용하여 특화 스트림을 일반 스트림으로 변환 할 수 있다
```
 int clories = menu.stream()    <- Stream<Dish> 반환 
                   .mapToInt(Dish::getcalories)      <- IntStream 반환
                   .sum();
```
```
 IntStream intStream = menu.stream().mapToInt(Dish::getCalories);       <- 스트림을 숫자 스트림으로 변환
 Stream<Integer> stream = intStream.boxed();            <- 숫자 스트림을 스트림으로 변환
```
+ 기본값 : OptionalInt
  + 스트림에 요소가 없을 때와 최댓값이 0인 상황에 대한 구별 방법
    : `OptionalInt`, `OptionalDouble`, `OptinalLong`
  + `orElse` : 값이 없을 때 기본 최댓값을 명시적으로 설정
```
 OptionalInt maxCalories = menu.stream()
                               .mapToInt(Dish::geCalories)
                               .max();
 int max = maxCalories.orElse(1);
```
+ 숫자 범위
  + `range` : 첫 번째 인수로 시작값, 두 번째 인수로 종료값. 시작값, 종료값은 결과에 포함되지 않음
  + `rangeClosed` : 첫 번째 인수로 시작값, 두 번째 인수로 종료값. 시작값, 종료값은 결과에 포함됨
```
 IntStream evenNumbers = IntStream.rangeClosed(1, 100)
                                  .filter(n -> n%2 == 0);
 System.out.println(evenNumbers.count());
```

## 5.8 스트림 만들기
+ 값으로 스트림 만들기
  + `Stream.of` : 임의의 수를 인수로 받는 정적 메서드
  + `Stream.empty` : 스트림을 비우는 메서드
```
 // 스트림의 모든 문자열을 대문자로 변환한 후 문자열을 하나씩 출력
 Stream<String> stream = Stream.of("Modern", "Java", "In", "Action");
 stream.map(String::toUpperCase).forEach(System.out::println);
 
 // 스트림을 비움
 Stream<String> emptyStream = Stream.empty();
```
+ null이 될 수 있는 객체로 스트림 만들기
  + `System.getProperty` : 제공된 키에 대응하는 속성이 없으면 null을 반환
  + `Stream.ofNullable` : null을 명시적으로 확인
```
 String homeValue = System.getProperty("home");
 Stream<String> homeValueStream = homeValue == null ? Stream.empty() : Stream.of(value);
 
 Stream<String> homeValueStream = Stream.ofNullable(System.getProperty("home"));
 
 Stream<String> values = Stream.of("config", "home", "user")
                               .flatMap(key -> Stream.ofNullable(System.getProperty(key)));
```
+ 배열로 스트림 만들기
  + `Arrays.stream` : 배열을 인수로 받는 정적 메서드. int로 이루어진 배열을 IntStream으로 변환
```
 int[] numbers = {2,3,5,7,11,13};
 int sum = Arrays.stream(numbers).sum();
```
+ 파일로 스트림 만들기
  + `Files.lines` : 주어진 파일의 행 스트림을 문자열로 반환
  + Stream 인터페이스는 AutoCloseable 인터페이스를 구현하여 try 블록 내의 자원이 자동으로 관리된다
```
 long uniqueWords = 0;
 try(Stream<String> lines = Files.lines(Paths.get("data.txt"), Charset.defaultCharset())) {
                                uniqueWords = lines.flatMap(line -> Arrays.stream(line.split(" ")))
                                                   .distinct()
                                                   .count();
 } catch(IOException e) {
                                
 }
```
+ 함수로 무한 스트림 만들기
  + `Stream.iterate` : 크기가 고정되지 않는 무한 스트림(언바운드 스트림) 생성. 자바9에서는 프레디케이트를 지원
  + `Stream.generate` : 크기가 고정되지 않는 무한 스트림(언바운드 스트림) 생성. Supplier<T>를 인수로 받아서 새로운 값을 생산.
  + 무한 스트림의 요소는 무한적으로 계산이 반복되므로 정렬하거나 리듀스할 수 없다.
```
 Stream.iterate(0, n -> n+2)
       .limit(10)
       .forEach(System.out::println);
```
```
 IntStream.iterate(0, n -> n < 100, n -> n+4)
          .forEach(System.out::println);
```
```
 IntStream.iterate(0, n -> n+4)
          .filter(n -> n < 100)     // 불가능!
          .forEach(System.out::println);
          
 IntStream.iterate(0, n -> n+4)
          .takeWhile(n -> n < 100)
          .forEach(System.out::println);
```
```
 Stream.generate(Math::random)
       .limit(5)
       .forEach(System.out::println);
```
```
 // IntStream의 generate 메서드는 Supplier<T> 대신에 IntSupplier를 인수로 받음
 IntStream ones = IntStream.generate(() -> 1);
 
 IntStream twos = IntStream.generate(new IntSupplier(){
    public int getAsInt(){
        return 2;
    }
 });

 IntSupplier fid = new IntSupplier() {
    private int previous = 0;
    private int current = 1;
    public int getAsInt() {
        int oldPrevious = this.previous;
        int nextValue = this.previous + this.current;
        this.previous = this.current;
        this.current = nextValue;
        return oldPrevious;
    }
 };
 IntStream.generate(fib)
          .limit(10)
          .forEach(System.out::println());
```