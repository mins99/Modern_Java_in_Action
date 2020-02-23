## 6.5 Collector 인터페이스
+ Collector 인터페이스는 리듀싱 연산을 어떻게 구현할지 제공하는 메서드 집합으로 구성
```
 public interface Collector<T, A, R> {
    Supplier<A> supplier();
    BiConsumer<A, T> accumulator();
    Function<A, R> finisher();
    BinaryOperator<A> combiner();
    Set<Characteristics> characteristics();
 }
```
+ T : 수집될 스트림 항목의 제네릭 형식
+ A : 누적자, 수집 과정에서 중간 결과를 누적하는 객체의 형식
+ R : 수집 연산 결과 객체의 형식(대부분 컬렉션 형식)

### 1) supplier - 새로운 결과 컨테이너 만들기
+ 빈 결과로 이루어진 Supplier를 반환. 수집 과정에서 빈 누적자 인스턴스를 만드는 파라미터가 없는 함수.
```
 public Supplier<List<T>> supplier() {
    return () -> new ArrayList<T>();
 }
```
+ 생성자 참조를 전달하는 방법도 있다
```
 public Supplier<List<T>> supplier() {
    return ArrayList::new;
 }
```

### 2) accumulator - 결과 컨테이너에 요소 추가하기
+ 리듀싱 연산을 수행하는 함수를 반환
```
 public BiConsumer<List<T>, T> accumulator() {
    return (list, item) -> list.add(item);
 }
```
+ 메서드 참조 사용
```
 public BiConsumer<List<T>, T> accumulator() {
    return List::add;
 }
```

### 3) finisher - 최종 변환값을 결과 컨테이너로 적용
+ 스트림 탐색을 끝내고 누적자 객체를 최종 결과로 반환하면서 누적 과정을 끝낼 때 호출할 함수를 반환
+ 누적자 객체가 이미 최종 결과인 상황에는 항등 함수를 반환
```
 public Function<List<T>, List<T>> finisher() {
    return Function.identity();
 }
```

### 4) combiner - 두 결과 컨테이너 병합
+ 스트림의 서로 다른 서브파트를 병렬로 처리할 때 누적자가 결과를 어떻게 처리할지 정의
+ toList의 combiner는 두번째 서브파트에서 수집한 항목 리스트를 첫번째 서브파트 결과 리스트의 뒤에 추가하는 방법으로 구현
```
 public BinaryOperator<List<T>> combiner() {
    return (list1, list2) -> { list1.addAll(list2); return list1; };
 }
```

### 5) characteristics
+ Characteristics 형식의 불변 집합을 반환
+ 스트림을 병렬로 리듀스할 것인지, 한다면 어떤 최적화를 선택해야 할지 힌트를 제공
  + UNORDERED
  + CONCURRENT
  + IDENTITY_FINISH