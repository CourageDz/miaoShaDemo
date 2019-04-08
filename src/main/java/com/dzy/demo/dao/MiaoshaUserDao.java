package com.dzy.demo.dao;

import com.dzy.demo.domain.MiaoShaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MiaoshaUserDao {
    @Select("select * from miaosha_user where id =#{id}")
    public MiaoShaUser getById(@Param("id") long id);

    @Update("update miaosha_user set password=#{password} where id=#{id}")
    void updatePassword(MiaoShaUser newUser);
}
