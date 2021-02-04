package com.jzs.dao;

import com.jzs.pojo.Member;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/30  0:24
 */
public interface MemberDao {

    // 根据手机号查询会员信息（唯一）
    Member findMemberByTelephone(String telephone);

    //新增会员
    void add(Member member);

    Integer findMemberCountByMonth(String lastDayOfMonth);

    int getTodayNewMember(String today);

    int getTotalMember();

    int getThisWeekAndMonthNewMember(String weekMonday);
}
