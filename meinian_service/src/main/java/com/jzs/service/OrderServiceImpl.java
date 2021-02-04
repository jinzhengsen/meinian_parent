package com.jzs.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.jzs.constant.MessageConstant;
import com.jzs.dao.MemberDao;
import com.jzs.dao.OrderDao;
import com.jzs.dao.OrderSettingDao;
import com.jzs.entity.Result;
import com.jzs.pojo.Member;
import com.jzs.pojo.Order;
import com.jzs.pojo.OrderSetting;
import com.jzs.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/29  23:14
 */

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService{


    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public Result order(Map map) throws Exception {
    /*
       1. 判断当前的日期是否可以预约(根据orderDate查询t_ordersetting, 能查询出来可以预约;查询不出来,不能预约)
        2. 判断当前日期是否预约已满(判断reservations（已经预约人数）是否等于number（最多预约人数）)
        3. 判断是否是会员(根据手机号码查询t_member)
        - 如果是会员(能够查询出来), 防止重复预约(根据member_id,orderDate,setmeal_id查询t_order)
          如果不是会员(不能够查询出来),自动注册为会员(直接向t_member插入一条记录)
     4.进行预约
      - 向t_order表插入一条记录
      - t_ordersetting表里面预约的人数reservations+1
                */

/*
        1. 判断当前的日期是否可以预约(根据orderDate查询t_ordersetting, 能查询出来可以预约;查询不出来,不能预约)
*/
        //获取用户预约的时间
        String orderDate = map.get("orderDate").toString();
        // 因为数据库预约设置表里面的时间是date类型，http协议传递的是字符串类型，所以需要转换
        Date date=DateUtils.parseString2Date(orderDate);
        // 使用预约时间查询预约设置表，看看是否可以 进行预约
         //（1）使用预约时间，查询预约设置表，判断是否有该记录
        OrderSetting orderSetting=orderSettingDao.findOrderSettingByOrderDate(date);
        // 如果预约设置表等于null，说明不能进行预约，压根就没有开团
        if (orderSetting==null){//如果没查到数据说明当前日期没有设置
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }else {
            // 2. 判断当前日期是否预约已满(判断reservations（已经预约人数）是否等于number（最多预约人数）)
            //获取当前日期的可预约人数和已预约人数
            int number = orderSetting.getNumber();
            int reservations = orderSetting.getReservations();
             //如果预约人数大于等于最大预约数，此时不能预约，提示“预约已满”
            if (reservations>=number){
             return new Result(false,MessageConstant.ORDER_FULL);
            }
        }
         /*     3. 判断是否是会员(根据手机号码查询t_member)
                - 如果是会员(能够查询出来), 防止重复预约(根据member_id,orderDate,setmeal_id查询t_order)
                - 如果不是会员(不能够查询出来),自动注册为会员(直接向t_member插入一条记录)
           */
         //获取手机号
        String telephone = map.get("telephone").toString();
        //查询会员信息
        Member member=memberDao.findMemberByTelephone(telephone);
        //如果是会员，防止重复预约（一个会员、一个时间、一个套餐不能重复，否则是重复预约）
        if (member!=null){//是会员
            // 如果是会员(能够查询出来), 防止重复预约(根据member_id,orderDate,setmeal_id查询t_order)
            Integer memberId= member.getId();
            // 获取套餐id
            int setmealId = Integer.parseInt(map.get("setmealId").toString());
            Order order = new Order();
            order.setMemberId(memberId);
            order.setOrderDate(date);
            order.setSetmealId(setmealId);
            // 根据预约信息查询是否已经预约
            List<Order> dbOrder=orderDao.findOrder(order);
            if (dbOrder!=null||dbOrder.size()>0){
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }else {//不是会员
            // 如果不是会员：注册会员，向会员表中添加数据
           member= new Member();
           member.setName(map.get("name").toString());
           member.setSex(map.get("sex").toString());
           member.setIdCard(map.get("idCard").toString());
           member.setPhoneNumber(map.get("telephone").toString());
           member.setRegTime(new Date());
           memberDao.add(member);
        }

         /*     4.进行预约
                - 向t_order表插入一条记录
                - t_ordersetting表里面预约的人数reservations+1*/
        //更新已经预约的人数
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        // 向t_order表插入一条记录 生成订单
        Order order=new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(date);
        order.setSetmealId(Integer.parseInt(map.get("setmealId").toString()));
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setOrderType(Order.ORDERTYPE_WEIXIN);
        orderDao.add(order);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order);
    }

//    根据id查询预约信息，包括人信息、套餐信息
    @Override
    public Map findById(Integer id) throws Exception {
        Map map=orderDao.findById(id);
        String orderDate=DateUtils.parseDate2String((Date) map.get("orderDate"));
        map.put("orderDate",orderDate);
        System.out.println("service中的map==>>>"+map);
        return map;
    }
}
