package kr.ggang.openplaces.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.ggang.openplaces.dao.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberId(String memberId);
    Optional<Member> findByName(String Name);
}
