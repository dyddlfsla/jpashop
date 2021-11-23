package jpabook.jpashop.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

  private final EntityManager entityManager;

  public void save(Order order) {
    entityManager.persist(order);
  }

  public Order findOne(Long id) {
    return entityManager.find(Order.class, id);
  }

  public List<Order> findAllByString(OrderSearch orderSearch) {
/*    검색 조건에 항상 값이 있는 경우에 해당하는 코드이다.
    return entityManager.createQuery("select o from Order o join o.member m where o.status = :status and m.name like :name", Order.class)
        .setParameter("status", orderSearch.getOrderStatus())
        .setParameter("name", orderSearch.getMemberName())
        .setMaxResults(1000)
        .getResultList();
  }*/

    /*
     * 하지만 만약 값이 있거나 없거나 변하는 상황이라면 그에 맞게 동적 쿼리를 생성하도록 해야 한다.
     * 방법은 크게 1. 조건문을 이용한 JPQL 처리 2. JPA 표준 Criteria 이용 3.querydsl 이용
     * 가 있는데 우선 1번을 사용하고 3번의 방식도 배워본다.
     * */

    //language=JPAQL
    String jpql = "select o From Order o join o.member m";
    boolean isFirstCondition = true;

    //주문 상태 검색
    if (orderSearch.getOrderStatus() != null) {
      if (isFirstCondition) {
        jpql += " where";
        isFirstCondition = false;
      } else {
        jpql += " and";
      }
      jpql += " o.status = :status";
    }

    //회원 이름 검색
    if (StringUtils.hasText(orderSearch.getMemberName())) {
      if (isFirstCondition) {
        jpql += " where";
        isFirstCondition = false;
      } else {
        jpql += " and";
      }
      jpql += " m.name like :name";
    }

    TypedQuery<Order> query = entityManager.createQuery(jpql, Order.class).setMaxResults(1000);

    if (orderSearch.getOrderStatus() != null) {
      query = query.setParameter("status", orderSearch.getOrderStatus());
    }
    if (StringUtils.hasText(orderSearch.getMemberName())) {
      query = query.setParameter("name", orderSearch.getMemberName());
    }
    return query.getResultList();
  }
}
