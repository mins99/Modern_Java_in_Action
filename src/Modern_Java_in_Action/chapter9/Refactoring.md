## 9.1 가독성과 유연성을 개선하는 리팩터링
### 1. 코드 가독성 개선
+ 코드 가독성이 좋다 : 어떤 코드를 다른 사람도 쉽게 이해할 수 있음

### 2. 익명 클래스 -> 람다 표현식
+ 간결하고, 가독성이 좋은 코드 구현 가능
+ 모든 익명 클래스를 람다 표현식으로 바꿀 수 있는 것은 아님
  + 익명 클래스의 this는 익명 클래스 자신, 람다의 this는 람다를 감싸는 클래스
  + 익명 클래스는 감싸고 있는 클래스의 변수를 가릴 수 있지만, 람다 표현식으로는 변수를 가릴 수 없다
  + 콘텍스트 오버로딩에 따른 모호함이 초래될 수 있다(익명 클래스는 명시적으로 형식이 정해짐, 람다는 콘텍스트에 따라 달라짐)
```
 // 익명 클래스
 Runnable r1 = new Runnable() {
    public void run() {
        System.out.println("Hello");
    }
 };
 
 // 람다 표현식
 Runnable r2 = () -> System.out.println("Hello");
```
```
 // 람다 표현식
 int a = 10;
 Runnable r1 = () -> {
    int a = 2;
    System.out.println(a);     // 컴파일 에러
 };
 
 // 익명 클래스
 Runnable r2 = new Runnable() {
   public void run() {
   int a = 2;
   System.out.println(a);
 };
```
```
 interface Task { public void execute(); }
 
 public static void doSomething(Runnable r) { r.run(); }
 public static void doSomething(Task a) { r.execute(); }
 
 // 익명 클래스
 doSomething(new Task() {
    public void execute() {
        System.out.println("Danger!");
    }
 });
 
 // 람다 표현식, Runnable, Task 모두 대상 형식이 될 수 있으므로 문제
 doSomething(() -> System.out.println("Danger!!");
 doSomething((Task)() -> System.out.println("Danger!!!");   // 명시적 형변환으로 모호함 제거
```
 
### 3. 람다 표현식 -> 메서드 참조
+ 가독성을 높일 수 있다.
```
 // 람다 표현식
 Map<CaloricLevel, List<Dish>> dishesByCaloricLevel 
    = menu.stream().collect(groupingBy(dish -> { if(dish.getCalories() <= 400) return CaloricLevel.DIET;
                                                 else if(dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                                                 else return CaloricLevel.FAT; }));
                                                 
 // 메서드 참조
 Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream().collect(groupingBy(Dish::getCaloricLevel));
 
 public class Dish {
    ...
    public CaloricLevel getCaloricLevel() {
        if(dish.getCalories() <= 400) return CaloricLevel.DIET;
        else if(dish.getCalories() <= 700) return CaloricLevel.NORMAL;
        else return CaloricLevel.FAT; 
    }
 }
```
+ 최댓값이나 합계를 계산할 때 Collectors API를 사용하면 코드의 의도가 더 명확해진다
```
 // 람다 표현식
 int totalCalories = menu.stream().map(Dish::getCalories).reduce(0, (c1, c2) -> c1 + c2);
 
 // 메서드 참조
 int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));
```

### 4. 명령형 데이터 처리 -> 스트림
+ 기존의 반복자를 이용한 컬렉션 처리 코드를 스트림 API로 
  + 스트림 API는 데이터 처리 파이프라인의 의도를 명확하게 보여줌
  + 쇼트서킷과 게으름, 멀티코어 아키텍처 활용
```
 // 필터링과 추출 사용
 List<String> dishNames = new ArrayList<>();
 for(Dish dish : menu) {
    if(dish.getCalories() > 300)
        dishNames.add(dish.getName());
    }
 }
 
 // 스트림 API 사용
 menu.parallelStream().filter(d -> d.getCalories() > 300).map(Dish::getName).collect(toList());
```

### 5. 코드 유연성 개선
+ 조건부 연기 실행
  + 람다를 이용하면 특정 조건에서만 메시지가 생성될 수 있도록 메시지 생성 과정을 연기(defer) 할 수 있다
  + 클라이언트 코드에서 객체 상태를 자주 확인하거나, 객체의 일부 메서드를 호출하는 상황 -> 내부적으로 객체 상태를 확인한 후 메서드를 호출하도록 구현하는 것이 좋다(가독성, 캡슐화 측면)
```
 // log 메서드는 logger의 수준이 적절하게 설정되어 있을 때만 인수로 넘겨진 람다를 내부적으로 실행
 public void log(Level level, Supplier<String> msgSupplier) {
    if(logger.isLoggable(level)) {
        log(level, msgSupplier.get());
    }
 }
```
+ 실행 어라운드
  + 준비, 종료 과정을 반복적으로 수행하는 코드를 람다로 변환하여 코드 중복을 줄일 수 있다.