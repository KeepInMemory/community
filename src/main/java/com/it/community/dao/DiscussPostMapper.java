package com.it.community.dao;

import com.it.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    /*
       做的是分页查询功能，返回值是List集合
       这里的参数userId,帖子表里有userId这个字段
       在首页查询是不需要传入userId的
       用户个人主页功能里有一个"我发布的帖子",会传入userId
       如果不传入userId,默认是0默认显示主页,否则传入显示个人帖子

       SQL是动态sql,有时候要拼接userId,有时候不需要

       做分页需要两个参数offset这一页的起始行 limit这一页最多显示多少数据
       这两个参数要传到sql进行拼接
    */
    List<DiscussPost> selectDiscussPosts(int userId,int offset,int limit);

    /*
    分页一共有可能有多少页 = 一共有多少数据 / 每页多少数据
    每页多少数据可以自己固化下来,所以现在需要查询一共有多少数据
    查询表中一共有多少数据

    这里的userId和上面是一样的,没有默认首页,有则是个人主页
    @Param注解的作用是给这个参数取个别名,防止写到sql里太长了
    如果需要动态sql,有时候要拼接有时候不要,而参数只有一个的时候,参数一定要加@Param否则报错
    上面selectDiscussPosts方法有三个参数则不需要加
    */
    int selectDiscussPostRows(@Param("userId") int userId);

}
