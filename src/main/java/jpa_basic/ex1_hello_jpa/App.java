package jpa_basic.ex1_hello_jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	
    	// 하나만 생성해서 애플리케이션 전체 공유
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
    	// 트랜잭션 하나당 하나씩 만듦. 쓰레드간 공유 절대 X. 사용하고 버려야함
    	EntityManager em = emf.createEntityManager();
    	// 모든 데이터 변경은 트랜잭션 안에서만 실행해야함
    	EntityTransaction tx = em.getTransaction();
    	tx.begin();
    	
    	try {
    		//멤버 등록
//    		Member member = new Member();
//    		member.setId(1L);
//    		member.setName("hello");
//			
//    		em.persist(member);

    		//멤버 수정
//    		Member findMember = em.find(Member.class, 1L);
//    		findMember.setName("HelloJPA");
    		
    		
    		List<Member> result = em.createQuery("select m from Member as m", Member.class)
    				.setFirstResult(1)
    				.setMaxResults(10)
    				.getResultList();
    		
    		for(Member member : result) {
    			System.out.println(member.getName());
    		}
    		
    		tx.commit();
    		
    		// JPA 를 사용하면 엔티티 객체 중심으로 개발
    		// 모든 DB 데이터를 객체로 변환해서 검색하는 것을 불가능
    		// 애플리케이션이 필요한 데이터만 DB 에서 불러오려면 결국 검색조건이 필요한 SQL 이 필요
    		// JAP 는 SQL 을 추상화한 JPQL 이라는 객체 지향 쿼리 언어 제공
    		// 방언을 변경해도 JPA 코드는 변경하지 않아도 됨
    		// 객체 지향 SQL. SQL 에 의존 X.
    		
    		// ### 영속성 컨텍스트
    		// 영속성 컨텍스트 : 엔티티를 영구 저장하는 환경
    		// EntityManager.persist(entity);
    		// persist 는 DB에 저장하는 것이 아니라 영속석 컨텍스트에 저장한다
    		// 엔티티 매니저 하나에 영속성 컨텍스트 하나 생성
    		
    		// 엔티티의 생명주기
    		// 비영속(new/transient) : 영속성 컨텍스트와 전혀 관계가 없는 새로운 상태
    		// 영속(managed) : 영속성 컨텍스트에 관리되는 상태
    		// 준영속(detached) : 영속성 컨텍스트에 저장되었따가 분리된 상태
    		// 삭제(removed) : 삭제된 상태

    		// 영속성 컨텍스트의 이점
    		// 1. 1차 캐시
    		// 2. 동일성 보장
    		// 1차 캐시로 반복 가능한 읽기(repeateable read) 등급의 트랜잭션 격리 수준을 데이터 베이스가 아닌 애플리케이션 차원에서 제공
    		// 3. 트랜잭션을 지원하는 쓰기 지연
    		// 4. 변경 감지
    		// 값을 읽어온 최초시점을 스냅샷에 저장
    		// 커밋되는 시점에 엔티티와 스냡샷을 비교
    		// update query 를 쓰기 지연 sql 저장소에 생성
    		// 5. 지연 로딩
    		
    		// 플러시 
    		// 영속성 컨텍스트의 변경내용을 데이터베이스에 반영
    		// 영속성 컨텍스트를 비우지 않음
    		// 영속성 컨텍스트의 변경 내용을 데이터베이스에 동기화
    		// 트랜잭션이라는 작업 단위가 중요 -> 커밋 직전에만 동기화 하면 됨
    		// 플러시 발생
    		// 1. 변경 감지
    		// 2. 수정된 엔티티 쓰기 지연 sql 저장소에 등록
    		// 3. 쓰기 지연 sql 저장소의 쿼리를 데이터베이스에 전송 (등록, 수정, 삭제 쿼리)
    		// 영속성 컨텍스트를 플러시 하는 방법
    		// 직접 호출할 일은 드움
    		// 1. em.flush() - 직접호출
    		// flush() 를 해도 캐쉬는 유지됨
    		// 2. 트랜잭션 커밋 - 플러시 자동 호출
    		// 3. JPQL 쿼리 실행 - 플러시 자동 호출
    		// 한 트랜잭션 안에 persist() 직후 jpql 쿼리를 실행한 경우 
    		
    		// 플러시 모드 옵션
    		// em.setFlushMOde(FlushModeType.COMMIT)
    		// FlushModeType.AUTO : 커밋이나 쿼리를 실행할 떄 플러시 (기본값)
    		// FlushModeType.COMMIT : 커밋할 떄만 플러시
    		// 이점이 미비하기 떄문에 AUTO 추천
    		
    		
    		// 준영속 상태
    		// 영속 -> 준영속
    		// 준영속 상태로 만드는 방법
    		
    		// 비영속 
    		Member member = new Member();
    		member.setId(1L);
    		member.setName("hello");

    		// 영속
    		// 1차 캐시에 저장
    		// insert sql 을 데이터베이스에 보내지 않는다
    		// insert query를 쓰기 지연 sql 저장소에 쌓아둔다
    		em.persist(member);
    		
    		// DB 에서 조회하는 것이 아니라 1차 캐시에서 먼저 조회
    		Member findMember = em.find(Member.class, "1");
    		
    		Member findMember2 = em.find(Member.class, "2");
    		// 1. 1차 캐시에 없는 값을 조회
    		// 2. DB 조회
    		// 3. 1차 캐시에 저장
    		// 4. 1차 캐시에서 반환
    		
    		// 큰 이점은 없는데 트랜잭션 하나당 영속성 컨텍스트 하나라서 금방 종료 됨
    		// 성능 이점이 미미
    		
    		//영속성 컨텍스트에서 본리
    		//트랜잭션을 할 떄 아무 일도 일어나지 않음
    		//특정 엔티티만 준영속 상태로 만듦
    		em.detach(member);
    		
    		// 영속성 완전히 초기화
    		em.clear();
    		
    		// 영속성 컨텍스트를 종료
    		em.close();
    		
    		// 객체를 삭제한 상태(삭제)
    		// 실제 DB 에서 지움
    		em.remove(member);
    		
    		// commit 하는 순간에 insert sql 을 db에 보낸다
    		tx.commit();
    		
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
    	
    	
    	emf.close();
    }
}
