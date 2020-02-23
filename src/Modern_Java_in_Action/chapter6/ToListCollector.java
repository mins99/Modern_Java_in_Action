package Modern_Java_in_Action.chapter6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.*;
import java.util.stream.Collector;

public class ToListCollector<T> implements Collector<T, List<T>, List<T>> {
	@Override
	public Supplier<List<T>> supplier() {
		return ArrayList::new;			// 수집 연산의 시발점
	}
	
	@Override
	public BiConsumer<List<T>, T> accumulator() {
		return List::add;			// 탐색한 항목을 누적하고 바로 누적자를 고친다
	}
	
	@Override
	public Function<List<T>, List<T>> finisher() {
		return Function.identity();		// 항등 함수
	}
	
	@Override
	public BinaryOperator<List<T>> combiner() {
		return (list1, list2) -> { list1.addAll(list2); return list1; };			// 첫번째 누적자를 고친 후 변경된 첫번째 누적자 반환
	}
	
	@Override
	public Set<Characteristics> characteristics() {
		return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH, Characteristics.CONCURRENT));		// 컬렉터의 플래그 설정
	}
}
