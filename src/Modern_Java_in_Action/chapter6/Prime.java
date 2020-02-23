package Modern_Java_in_Action.chapter6;

import static java.util.stream.Collectors.partitioningBy;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Prime {
	public boolean isPrime(int candidate) {
		return IntStream.range(2, candidate).noneMatch(i -> candidate % i == 0);
	}
	
	public boolean isPrime2(int candidate) {
		int candidateRoot = (int)Math.sqrt((double)candidate);
		return IntStream.rangeClosed(2, candidate).noneMatch(i -> candidate % i == 0);
	}
	
	// isPrime 메서드를 프레디케이트로 이용하고 partitioningBy 컬렉터로 리듀스하여 숫자를 분류
	public Map<Boolean, List<Integer>> partitionPrimes(int n) {
		return IntStream.rangeClosed(2,  n).boxed().collect(partitioningBy(candidate -> isPrime(candidate)));
	}
}
