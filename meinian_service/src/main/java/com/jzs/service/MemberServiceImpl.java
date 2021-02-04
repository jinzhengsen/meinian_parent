package com.jzs.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.jzs.dao.MemberDao;
import com.jzs.pojo.Member;
import com.jzs.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/30  22:30
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService{
    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findMemberByTelephone(String telephone) {
        return memberDao.findMemberByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMonth(List<String> months) {
        List<Integer> memberCountList=new ArrayList<>();
        if (months!=null&&months.size()>0){
            for (String month : months) {
                //获取当前月份的最后一天
                String lastDayOfMonth = DateUtils.getLastDayOfMonth(month);
                Integer memberCount=memberDao.findMemberCountByMonth(lastDayOfMonth);
                memberCountList.add(memberCount);
            }
        }
        return memberCountList;
    }
}
