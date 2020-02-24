## 8.1 컬렉션 팩토리
+ UnsupportedOperationException
  + 내부적으로 고정된 크기의 배열에 새로운 요소를 추가하려고 하면 발생하는 예외
  + 자바9에서는 작은 리스트, 집합, 맵을 쉽게 만들 수 있도록 팩토리 메서드를 제공
  
### 1) List.of
+ 리스트를 만드는 팩토리 메소드
```
 List<String> friends = List.of("Raphael", "Olivia", "Thibaut");
 System.out.println(friends);
 
 friends.add("Chih-Chun");     // error! 요소 추가는 불가능 하다
```

### 2) Set.of
+ `List.of`와 비슷한 방법으로 바꿀 수 없는 집합을 만들 수 있다
```
 Set<String> friends = Set.of("Raphael", "Olivia", "Thibaut");
 System.out.println(friends);
```

### 3) Map.of, Map.ofEntries
```
 Map<String, Integer> ageOfFriends = Map.of("Raphael", 30, "Olivia", 25, "Thibaut", 26);
 System.out.println(ageOfFriends);
 
 Map<String, Integer> ageOfFriends = Map.ofEntries(entry("Raphael", 30), entry("Olivia", 25), entry("Thibaut", 26));
 System.out.println(ageOfFriends);
```

## 8.2 리스트와 집합 처리
### 1) removeIf
+ 삭제할 요소를 가리키는 프레디케이트를 인수로 받는다
```
 transactions.removeIf(transaction -> Character.isDigit(transaction.getReferenceCode().charAt(0)));
```

### 2) replaceAll
+ 리스트의 각 요소를 새로운 요소로 바꿀 수 있다
```
 referenceCodes.replaceAll(code -> Character.toUpperCase(code.charAt(0)) + code.substring(1));
```

## 8.3 맵 처리
### 1) forEach
```
 ageOfFriends.forEach((friend, age) -> System.out.println(friend + " is " + age + " years old"));
```

### 2) 정렬 메서드
+ `Entry.comparingByValue` : 맵의 항목을 값을 기준으로 정렬
+ `Entry.comparingByKey` : 맵의 항목을 키를 기준으로 정렬
```
 Map<String, String> favouriteMovies = Map.ofEntries(entry("Raphael", "Star Wars"), entry("Cristina", "Matrix"), entry("Olivia", "James Bond"));
 favouriteMovies.entrySet().stream().sorted(Entry.comparingByKey()).forEachOrdered(System.out::println);
```

### 3) getOrDefault 메서드
+ 첫 번째 인수로 키, 두 번째 인수로 기본값
+ 맵에 키가 존재하지 않으면 두 번째 인수로 받은 기본값을 반환
+ NullPointerException 방지
```
 Map<String, String> favouriteMovies = Map.ofEntries(entry("Raphael", "Star Wars"), ntry("Olivia", "James Bond"));
 System.out.println(favouriteMovies.getOrDefault("Olivia", "Matrix"));      // James Bond 출력   
 System.out.println(favouriteMovies.getOrDefault("Thibaut", "Matrix"));     // Matrix 출력
```

### 4) 계산 패턴
+ `computeIfAbsent` : 제공된 키에 해당하는 값이 없으면 키를 이용해 새 값을 계산하고 맵에 추가
+ `computeIfPresent` : 제공된 키가 존재하면 새 값을 계산하고 맵에 추가
+ `compute` : 제공된 키로 새 값을 계산하고 맵에 저장
```
 String friend = "Raphael";
 List<String> movies = friendsToMovies.get(friend);
 if(movies == null) {
    movies = new ArrayList<>();
    friendsToMovies.put(friend, movies);
 }
 movies.add("Star Wars");
 
 System.out.println(friendsToMovies);
 
 friendsToMovies.computIfAbsent("Raphael", name -> new ArrayList<>()).add("Star Wars");
```

### 5) 삭제 패턴
+ `remove` : 키가 특정한 값과 연관되었을 때만 항목을 제거하는 오버로드 버전 메서드
```
 favouriteMovies.remove(key, value);
```

### 6) 교체 패턴
+ `replaceAll` : BiFunction을 적용한 결과로 각 항목의 값을 교체
+ `Replace` : 키가 존재하면 맵의 값을 바꾼다
```
 Map<String, String> favouriteMovies = new HashMap<>();
 favouriteMovies.add("Raphael", "Star Wars"); 
 favouriteMovies.add("Olivia", "James Bond");
 favouriteMovies.replaceAll((friend, movie) -> movie.toUpperCase());
 System.out.println(favouriteMovies);
```
  
### 7) 합침
+ `putAll` : 중복된 키를 어떻게 합칠지 결정하는 BiFuntion을 인수로 받는다
```
 Map<String, String> everyone = new HashMap<>(family);
 friends.forEach((k, v) -> everyone.merge(k, v, (movie1, movie2) -> movie1 + " & " + movie2));
 System.out.println(everyone);
 
 moviesToCount.merge(movieName, 1L, (key, count) -> count + 1L);
```

## 8.4 개선된 ConcurrentHashMap
+ ConcurrentHashMap : 동시성 친화적이며 최신 기술을 반영한 HashMap. 읽기 쓰기 연산 성능이 월등하다

### 1) 리듀스와 검색
+ `forEach`, `reduce`, `search`
  + 키&값, 키, 값, Map.Entry 네 가지 연산 형태를 지원
  + ConcurrentHashMap의 상태를 잠그지 않고 연산을 수행
  + 계산이 진행되는 동안 바뀔 수 있는 객체, 값, 순서 등에 의존하지 않아야 함
  + 병렬성 기준값(threshold)을 지정해야 함
    + 1이면 공통 쓰레드 풀을 이용해 병렬성 극대화, Long.MAX_VALUE이면 한 개의 스레드로 연산 실행
```
 ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();
 long parallelismThreshold = 1;
 Optional<Integer> maxValue = Optional.ofNullable(map.reduceValues(parallelismThreshold, Long::max));
```

### 2) 계수
+ `mappingCount` : 맵의 매핑 개수를 반환. int의 범위를 넘어서는 이후의 상황을 대처

### 3) 집합뷰
+ `keySet` : ConcurrentHashMap -> 집합 뷰
+ `newKeySet` : ConcurrentHashMap으로 유지