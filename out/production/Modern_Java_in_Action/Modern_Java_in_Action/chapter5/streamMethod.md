## 5.1 필터링
+ filter : Predicate(boolean type)를 인수로 받아서 프레디케이트와 일치하는 모든 요소를 포함하는 스트림 반환
```
 List<Dish> vegetarianMenu = menu.stream()
                                 .filter(Dish::isVegetarian)
                                 .collect(toList());
```

+ distinct : 고유 요소로 이루어진 스트림 반환(고유 여부는 hashCode, equals로 결정)
```
 List<Integer> numbers = Arrays.asList(1,2,1,3,3,2,6);
 numbers.stream()
        .filter(i->i%2 == 0)
        .distinct()
        .forEach(System.out::println);
```
 
 
## 5.2 스트림 슬라이싱(Java 9)
+ takewhile : 무한 스트림을 포함한 모든 스트림에 프레디케이트를 적용해 스트림을 슬라이스 할 수 있다.
```
 List<Dish> slicedMenu1 = specialMenu.stream()
                                     .takeWhile(dish -> dish.getCalories() < 320)
                                     .collect(toList());
```

+ dropwhile : 프레디케이트가 처음으로 거짓이 되는 지점까지 발견된 요소를 버린다.
```
 List<Dish> slicedMenu2 = specialMenu.stream()
                                     .dropWhile(dish -> dish.getCalories() < 320)
                                     .collect(toList());
```

+ limit(n) : 주어진 값 이하의 크기를 갖는 새로운 스트림을 반환
  + 정렬되지 않은 스트림에도 limit 사용 가능(정렬되지 않은 상태로 반환됨)
```
 List<Dish> slicedMenu3 = specialMenu.stream()
                                     .filter(dish -> dish.getCalories() > 300)
                                     .limit(3)
                                     .collect(toList());
```

+ skip(n) : 처음 n개 요소를 제외한 스트림을 반환
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

+ Arrays.stream : 문자열을 받아 스트림을 만든다.
```
 String[] arrayOfWords = {"Hello", "World"};
 Stream<String> streamOfWords = Arrays.stream(arrayOfWords);
 
 words.stream()
      .map(word -> word.split("");
      .map(Arrays::stream)
      .distinct()
      .collect(toList();
```

+ flapMap : 각 배열을 스트림의 콘텐츠로 매핑하여 하나의 평면화된 스트림을 반환
```
 List<String> uniqueCharacters = words.stream()
                                      .map(word -> word.split("");
                                      .flatMap(Arrays::stream)
                                      .distinct()
                                      .collect(toList());
```

## 5.4 검색과 매칭
+ 