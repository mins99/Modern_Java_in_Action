# Chapter 16 CompletableFuture : 안정적 비동기 프로그래밍
### 이장의 내용
- 비동기 작업을 만들고 결과 얻기
- 비블록 동작으로 생산성 높이기
- 비동기 API 설계와 구현
- 동기 API를 비동기적으로 소비하기
- 두 개 이상의 비동기 연산을 파이프라인으로 만들고 합치기
- 비동기 작업 완료에 대응하기

---
## 16.1 Future의 단순 활용
### Java5 -> Future
- 시간이 오래 걸리는 작업을 Callable 객체 내부로 감싼 다음 ExecutorService에 제출
  - ExecutorService에서 제공하는 스레드가 작업을 처리하는 동안 우리 스레드로 다른 작업을 동시에 실행
  - get 메서드를 통한 결과 회수
  - 오래 걸리는 작업이 영원히 끝나지 않을 수 있음 -> 최대 타임아웃 시간 설정

![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FevHoeA%2Fbtrs8z8HMHz%2FAkqk1XS3j8bJSuN3Rb2lv0%2Fimg.png)

+ Future가 제공하는 메서드 : 비동기 계산이 끝났는지 확인, 계산이 끝나길 기다림, 결과 회수
  + 여러 Future 결과가 있을때 의존성을 표현하기 어려움
+ Stream과 비슷하게 람다표현식과 파이프라이닝을 활용함. Future와 CompletableFuture는 Collection과 Stream의 관계에 비유할 수 있다.

### 16.1.2 CompletableFuture로 비동기 애플리케이션 만들기
+ 여러 온라인상점 중 가장 저렴한 가격을 제시하는 상점을 찾는 애플리케이션 예제
+ 배울 수 있는 것들
    1. 고객에게 비동기 API를 제공하는 방법
    2. 동기 API를 사용할 때 코드를 비블록으로 만드는 방법 <br> (두 개의 비동기 동작을 파이프라인으로 만들고, 두 개의 동작 결과를 하나의 비동기 계산으로 합치기)
    3. 비동기 동작의 완료에 대응하는 방법
    
### 동기 API와 비동기 API
+ 동기 API : 메서드를 호출한 다음 계산을 완료할때까지 기다렸다가 반환되면 다른 동작을 수행. 동기 API를 사용하는 상황을 **블록 호출** 이라고 함
+ 비동기 API : 메서드는 즉시 반환되며 끝나지 못한 나머지 작업은 다른 스레드에 할당함. 비동기 API를 사용하는 상황을 **비블록 호출** 이라고 함

## 16.2 비동기 API 구현
+ 제품명에 해당하는 가격을 반환하는 메서드 구현(Shop.java)
```
public class Shop {
    // getPrice : 가격 정보를 얻는 동시에 다른 외부 서비스에 접근하는 메서드
    public double getPrice(String product) {
        return calculatePrice(product);
    }

    public double calculatePrice(String product) {
        delay();
        return format(random.nextDouble() * product.charAt(0) + product.charAt(1));
    }
    
    // delay : 오래 걸리는 작업을 흉내내는 메서드
    public static void delay() {
      try {
          Thread.sleep(1000L);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
  }
}
```

### 16.2.1 동기 메서드를 비동기 메서드로 변환
+ 위 API를 사용자가 호출하는 경우 비동기 동작이 완료될때까지 1초 동안 블록됨
+ 동기 메소드 getPrice를 비동기 메서드로 변환하기(AsyncShop.java)
+ java.util.concurrent.Future :  비동기 계산의 결과를 표현할 수 있는 인터페이스. 계산이 완료되면 get 메서드로 결과를 얻을 수 있다
```
public class AsyncShop {
    // getPriceAsync : 메서드가 호출 즉시 반환되어 호출자 스레드가 다른 작업을 수행
    public Future<Double> getPriceAsync(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() -> {
              double price = calculatePrice(product);
              futurePrice.complete(price);
        }).start();
        return futurePrice;
    }
}
```

### 16.2.2 에러 처리 방법
+ 가격을 계산하는 동안 에러가 발생하면 get 메서드가 반환될 때까지 클라이언트가 기다릴 수도 있음
+ 블록 문제가 발생할 수 있는 상황에서 타임아웃을 활용 -> 에러 원인을 알 수 없음
+ completeExceptionally 메서드를 이용 -> 에러 원인을 알 수 있음
```
public class AsyncShop {
    // completeExceptionally : 도중에 문제가 발생하면 발생한 에러를 포함시켜 Future를 종료
    public Future<Double> getPriceAsync(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() -> {
            try {
                double price = calculatePrice(product);
                futurePrice.complete(price);
            } catch (Exception ex) {
                futurePrice.completeExceptionally(ex);
            }
        }).start();
        return futurePrice;
    }
}
```

#### 팩토리 메서드 supplyAsync로 CompletableFuture 만들기
+ Supplier를 실행해서 비동기적으로 결과를 생성
```
public class AsyncShop {
    public Future<Double> getPriceAsync(String product) {
        return CompletableFuture.supplyAsync(() -> calculatePrice(product));
    }
}
```

## 16.3 비블록 코드 만들기
+ 16.2의 동기 API를 이용해서 최저가격 검색 애플리케이션을 개발하는 상황을 가정해보자
+ 제품명을 입력하면 상점 이름과 제품가격 문자열 정보를 포함하는 List를 반환하는 메서드 구현(BestPriceFinder.java)
+ 상점에서 가격을 검색하는 동안 각각 1초의 대기시간 발생
```
public class BestPriceFinder {
    private final List<Shop> shops = Arrays.asList(
            new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("BuyItAll"),

    public List<String> findPricesSequential(String product) {
        return shops.stream()
                .map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(Collectors.toList());
    }
}
```

### 16.3.1 병렬 스트림으로 요청 병렬화하기
+ parallelStream을 이용해서 순차 계산을 병렬로 처리하여 성능을 개선

### 16.3.2 CompletableFuture로 비동기 호출 구현하기
+ 팩토리 메서드 supplyAsync로 CompletableFuture 만들기
+ CompletableFuture의 join 메서드는 Future 인터페이스의 get 메서드와 같은 의미로 모든 비동기 동작이 끝나면 결과를 반환
+ join 사용시 아무 예외도 발생하지 않으므로 try/catch 불필요
```
public class BestPriceFinder {
    public List<String> findPricesFuture(String product) {
        List<CompletableFuture<String>> priceFutures =
                shops.stream()
                        .map(shop -> CompletableFuture.supplyAsync(() -> shop.getName() + " price is " + shop.getPrice(product)))
                        .collect(Collectors.toList());

        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }
}
```
+ 스트림 연산은 게으른 특성이 있기 때문에 두 개의 파이프라인으로 연산을 나누어 처리
![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbGi5iw%2Fbtrs7nHUCX6%2FPaOW89XpEmtdbKaaE7Ne7K%2Fimg.png)

### 16.3.3 더 확장성이 좋은 해결 방법
+ 스레드 갯수를 최대로 사용하는 경우까지는 병렬 스트림과 CompletableFuture 결과가 비슷할 수 있으나 그 이상인 경우 CompletableFuture 사용시 다양한 Executor 지정으로 애플리케이션 최적화 가능

### 16.3.4 커스텀 Executor 사용하기
+ 애플리케이션이 실제로 필요한 작업량을 고려한 풀에서 관리하는 스레드 수에 맞게 Executor 만들기
```
public class BestPriceFinder {
    private final Executor executor = Executors.newFixedThreadPool(Math.min(shops.size(), 100), new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setDaemon(true);          // 데몬 스레드 : 자바 프로그램 종료시 강제로 실행 종료
                    return t;
                }
            });
}
```

```
  CompletableFuture.supplyAsync(() -> shop.getName() + " price is " + shop.getPrice(product), executor);
```
+ 비동기 동작을 많이 사용하는 상황에서는 Executor를 만들어 CompletableFuture를 활용하는 것이 바람직
+ 다음을 참고하면 어떤 병렬화 기법을 사용할 것인지 선택하는데 도움이 된다
  + I/O가 포함되지 않은 계산 중심의 동작을 실행할 때는 **스트림 인터페이스**가 가장 구현하기 간단하며 효율적일 수 있다
  + I/O를 기다리는 작업을 병렬로 실행할 때는 **CompletableFuture**가 더 많은 유연성을 제공한다. 스트림에서 I/O를 실제로 언제 처리할지 예측하기 어려운 문제도 있다.

## 16.4 비동기 작업 파이프라인 만들기
+ 선언형으로 여러 비동기 연산을 CompletableFuture로 파이프라인화 해보자
+ 계약을 맺은 모든 상점이 하나의 할인 서비스를 사용하기로 하였으며(Discount.java)
+ 상점에서 getPrice의 결과 형식도 변경하였다(Shop.java)
```
public class Shop {
    public String getPrice(String product) {
        double price = calculatePrice(product);
        Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];
        return name + ":" + price + ":" + code;
    }

    public double calculatePrice(String product) {
        delay();
        return format(random.nextDouble() * product.charAt(0) + product.charAt(1));
    }
}
```

### 16.4.1 할인 서비스 구현
+ 상점에서 제공한 문자열 파싱을 Quote 클래스로 캡슐화(Quote.java)

### 16.4.2 할인 서비스 사용
+ Discount는 원격 서비스이므로 1초의 지연을 추가함(delay)
+ 할인 전 가격을 얻은 후 -> 상점에서 반환된 문자열을 Quote 객체로 변환 후 -> Discount 서비스를 이용하여 할인을 적용
```
public List<String> findPrices(String product) {
    return shops.stream()
            .map(shop -> shop.getPrice(product))
            .map(Quote::parse)
            .map(Discount::applyDiscount)
            .collect(Collectors.toList());
}
```

### 16.4.3 동기 작업과 비동기 작업 조합하기
+ CompletableFuture 에서 제공하는 기능으로 findPrices 메서드를 비동기적으로 재구현
```
public List<String> findPricesFuture(String product) {
    List<CompletableFuture<String>> priceFutures =
            shops.stream()
                    .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product), executor))
                    .map(future -> future.thenApply(Quote::parse))
                    .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor)))
                    .collect(Collectors.toList());

    return priceFutures.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList());
}
```
![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fedo32u%2Fbtrs9ir5tpN%2FDL9jYy8SrGFUdRpI1fMZok%2Fimg.png)

#### 가격 정보 얻기
+ supplyAsync에 닮다 표현식을 전달하여 비동기적으로 상점에서 정보를 조회 
+ 첫 번째 변환의 반환 결과는 Stream<CompletableFuture\<String>>
+ 각 CompletableFuture는 작업이 끝났을 때 해당 상점에서 반환하는 문자열 정보 포함
+ 커스텀 executor 사용

#### Quote 파싱하기
+ 첫 번째 결과 문자열을 Quote로 변환 -> 원격 서비스나 I/O가 없으므로 지연 없이 동작 수행 가능
+ thenApply 메서드는 CompletableFuture가 끝날때가지 블록하지 않음
+ CompletableFuture가 동작을 완전히 완료한 다음에 thenApply 메서드로 전달된 람다 표현식을 적용

#### CompletableFuture를 조합하여 할인된 가격 계산하기
+ 첫 번째 map에서 받은 할인전 가격에 Discount로 할인된 가격을 계산
+ 원격 실행이 포함되어 동기적으로 작업을 수행해야 함 -> 람다표현식으로 supplyAsync에 전달하여 다른 CompletableFuture를 반환 가능
+ 두 가지 CompletableFuture로 이루어진 연쇄적으로 수행되는 비동기 동작을 만들 수 있다.
  + 상점에서 가격 정보를 얻어와서 Quote로 변환
  + 변환된 Quote를 Discount 서비스로 전달해서 할인된 최종가격 획득
+ thenCompose 메서드는 첫 번째 연산의 결과를 두 번째 연산으로 전달

### 16.4.4 독립 CompletableFuture와 비독립 CompletableFuture 합치기
+ 실전에서는 독립적으로 실행된 두 개의 CompletableFuture 결과를 합쳐야 하는 상황이 종종 발생함
+ thenCombine 메서드는 BiFunction을 두 번째 인수로 받아 두 개의 CompletableFuture 결과를 합친다
+ thenCombineAsync 메서드는 thenCombine의 Async 버전으로 BiFunction이 정의하는 조합 동작이 스레드 풀로 제출되면서 별도의 태스크에서 비동기적으로 수행
+ 유로(EUR) 가격 정보를 제공하는 온라인상점에서 달러(USD) 가격으로 변경하여 보여주기

![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fcp7ut4%2Fbtrs7lJ5Gyj%2F154b0slHDjUERewwYpJ4Xk%2Fimg.png)

### 16.4.5 Future의 리플렉션과 CompletableFuture의 리플렉션
+ Future와 ExecutorService 사용시 스레드 반환 필수

### 16.4.6 타임아웃 효과적으로 사용하기
+ Future의 계산 결과를 읽을 때는 무한정 기다리는 상황이 발생할 수 있으므로 블록을 하지 않는 것이 좋다
+ 자바9에서는 CompletableFuture에서 제공하는 메서드를 이용해 문제를 해결할 수 있다
  + orTimeout : 지정된 시간이 지난 후 CompletableFuture를 TimeoutException으로 완료하면서 또 다른 CompletableFuture를 반환(ScheduledThreadExecutor 활용)
  + completeOnTimeout : 작업이 완료되지 않으면 미리 지정된 값을 사용하도록 함

## 16.5 CompletableFuture의 종료에 대응하는 방법
+ 원격 메서드 응답을 1초의 지연으로 고정 -> 실전에서는 얼마나 지연될지 예측하기 어렵다
+ delay대신 0.5 ~ 2.5 사이의 지연을 만드는 randomDelay 사용

### 16.5.1 최저가격 검색 애플리케이션 리팩터링
+ 각 상점에서 가격 정보를 제공할 때마다 즉시 보여줄 수 있는 최저가격 검색 애플리케이션
+ thenAccept : CompletableFuture가 생성한 결과를 어떻게 소비할지 미리 지정
+ allOf : CompletableFuture 배열을 입력으로 받아 모두 완료되었을 때 CompletableFuture\<Void>를 반환
+ anyOf : CompletableFuture 배열을 입력으로 받아 하나라도 작업이 끝났을 때 완료한 CompletableFuture\<Object>를 반환

## 16.7 마치며
+ 한 개 이상의 원격 외부 서비스를 사용하는 긴 동작을 실행할 때는 비동기 방식으로 애플리케이션의 성능과 반응성을 향상시킬 수 있다.
+ 우리 고객에게 비동기 API를 제공하는 것을 고려해야 한다. CompletableFuture의 기능을 이용하면 쉽게 비동기 API를 구현할 수 있다.
+ CompletableFuture를 이용할 때 비동기 태스크에서 발생한 에러를 관리하고 전달할 수 있다.
+ 동기 API를 CompletableFuture로 감싸서 비동기적이로 소비할 수 있다.
+ 서로 독립적인 비동기 동작이든 아니면 하나의 비동기 동작이 다른 비동기 동작의 결과에 의존하는 상황이든 여러 비동기 동작을 조립하고 조합할 수 있다.
+ CompletableFuture에 콜백을 등록해서 Future가 동작을 끝내고 결과를 생산했을 때 어떤 코드를 실행하도록 지정할 수 있다.
+ CompletableFuture 리스트의 모든 값이 완료될 때까지 기다릴지 아니면 첫 값만 완료되길 기다릴지 선택할 수 있다.
+ 자바 9에서는 orTimeout, completeOnTimeout 메서드로 CompletableFuture에 비동기 타임아웃 기능을 추가했다.